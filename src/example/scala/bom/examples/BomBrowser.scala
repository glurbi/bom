package bom.examples

import scala.collection.jcl._

import java.io._
import javax.swing._
import javax.swing.event._
import javax.swing.border._
import java.awt._
import java.awt.event._
import java.awt.geom._

import org.jdesktop.swingx._
import org.jdesktop.swingx.treetable._

import bom._
import bom.bin._
import bom.schema._
import bom.types._
import bom.stream._

import BomBrowser._

/**
 * A simple GUI application for opening binary documents and browse them.
 */
object BomBrowser {

  /**
   * Convenient way to create an action listener with a closure
   */
  def buildActionListener(fun: (ActionEvent) => Unit): ActionListener =
    new ActionListener {
      def actionPerformed(event: ActionEvent) = fun(event)
    }

  object EmptyDocument extends BOMDocument(null, null) {
    override def length = 0
  }
  
  /**
   * Creates the initial BOMDocument object.
   */
  def createInitialDocument = {
    (BomBrowserSetup.currentFile, BomBrowserSetup.currentSchema) match {
      case (null, _) => EmptyDocument
      case (_, null) => EmptyDocument
      case (file, schema) => {
        val bspace = new FileBinarySpace(BomBrowserSetup.currentFile)
        new BOMDocument(BomBrowserSetup.currentSchema.schema, bspace)
      }
    }
  }

  def main(args: Array[String]) {

    var docHolder = new DocumentHolder(createInitialDocument)

    val frame = new JFrame
    val statusBar = new JPanel
    val fileMenu = new JMenu("File")
    val openMenuItem = new JMenuItem("Open...")
    val exitMenuItem = new JMenuItem("Exit")
    val schemaMenu = new JMenu("Schema")
    val setSchemaClassMenuItem = new JMenuItem("Set class...")
    val setSchemaFileMenuItem = new JMenuItem("Set file...")
    val menuBar = new JMenuBar

    openMenuItem.addActionListener(buildActionListener { _ =>
      val fileChooser = new JFileChooser
      if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        BomBrowserSetup.currentFile = fileChooser.getSelectedFile.getAbsoluteFile
        val bspace = new FileBinarySpace(BomBrowserSetup.currentFile)
        val doc = new BOMDocument(BomBrowserSetup.currentSchema.schema, bspace)
        val newDocHolder = new DocumentHolder(doc)
        frame.remove(docHolder.scrollPane)
        frame.add(newDocHolder.scrollPane)
        frame.setVisible(true)
        docHolder = newDocHolder
      }
    })
    fileMenu.add(openMenuItem)
    exitMenuItem.addActionListener(buildActionListener { _ => System.exit(0) })
    fileMenu.add(exitMenuItem)
    setSchemaClassMenuItem.addActionListener(buildActionListener { _ =>
      val className = JOptionPane.showInputDialog("Please give the schema class name")
      var schema: Schema = null
      try {
        if (schema != null) {
          schema = Class.forName(className).newInstance.asInstanceOf[Schema]
          BomBrowserSetup.currentSchema = schema
        }
      } catch {
        case e: ClassNotFoundException =>
          JOptionPane.showMessageDialog(null, "The class '" + className + "' could not be instanciated.")
      }
    })
    schemaMenu.add(setSchemaClassMenuItem)
    setSchemaFileMenuItem.addActionListener(buildActionListener { _ =>
      val fileChooser = new JFileChooser
      if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        val schema = BomSchemaCompiler.compile(fileChooser.getSelectedFile.getAbsoluteFile)
        if (schema != null) {
          BomBrowserSetup.currentSchema = schema
        }
      }
    })
    schemaMenu.add(setSchemaFileMenuItem)
    frame.setLayout(new BorderLayout)
    statusBar.setLayout(new BorderLayout)
    val fileName = if (BomBrowserSetup.currentFile == null) "NO FILE"
                   else BomBrowserSetup.currentFile.toString
    statusBar.add(new JLabel(fileName), BorderLayout.CENTER)
    statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED))
    frame.add(statusBar, BorderLayout.SOUTH);

    menuBar.add(fileMenu)
    menuBar.add(schemaMenu)
    frame.setJMenuBar(menuBar)
    frame.add(docHolder.scrollPane)
    frame.setTitle("BOM Browser")
    frame.setSize(1280, 600)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setVisible(true)
  }

}

/**
 * Provides a Swing GUI for browsing binary document with help of a tree table.
 */
class DocumentHolder(val doc: BOMDocument) {

  /**
   * Adapts the BOM tree to a JXTreeTableModel
   */
  val dataTreeModel = new AbstractTreeTableModel(doc) {
    def getColumnCount: Int = 7
    def getValueAt(node: Object, column: Int): Object = column match {
      case 0 => node.asInstanceOf[BOMNode].name
      case 1 => translateValue(node.asInstanceOf[BOMNode])
      case 2 => "" + node.asInstanceOf[BOMNode].position / 8
      case 3 => "" + node.asInstanceOf[BOMNode].position
      case 4 => "" + node.asInstanceOf[BOMNode].size / 8
      case 5 => translateSchema(node.asInstanceOf[BOMNode].schema)
      case 6 => "" + node.asInstanceOf[BOMNode].index
    }
    def getChild(parent: Object, index: Int): Object =
      parent.asInstanceOf[BOMNode]/index
    def getChildCount(parent: Object): Int =
      parent.asInstanceOf[BOMNode].length.toInt
    def getIndexOfChild(parent: Object, child: Object): Int =
      child.asInstanceOf[BOMNode].index
    override def isLeaf(node: Object): Boolean = node.isInstanceOf[BOMLeaf]
    private def translateValue(node: BOMNode): String =
      node match {
        case BOMBlob(_, _, _) => "..."
        case BOMContainer(_, _, _) => ""
        case _ => node.value.toString
      }
    private def translateSchema(schema: SchemaElement): String = schema match {
      case SchemaSequence(_, _, _) => "sequence"
      case SchemaArray(_, _, _) => "array"
      case SchemaBlob(_, _, _) => "blob"
      case SchemaNumber(_, _, _) => "number"
      case SchemaString(_, _, _) => "string"
      case SchemaVirtual(_, _, _) => "virtual"
      case _ => "unknown"
    }
  }

  val dataTreePanel = new JXTreeTable(dataTreeModel)
  dataTreePanel.getTableHeader.getColumnModel.getColumn(0).setHeaderValue("Node")
  dataTreePanel.getTableHeader.getColumnModel.getColumn(1).setHeaderValue("Value")
  dataTreePanel.getTableHeader.getColumnModel.getColumn(2).setHeaderValue("Position (bytes)")
  dataTreePanel.getTableHeader.getColumnModel.getColumn(3).setHeaderValue("Position (bits)")
  dataTreePanel.getTableHeader.getColumnModel.getColumn(4).setHeaderValue("Size (bytes)")
  dataTreePanel.getTableHeader.getColumnModel.getColumn(5).setHeaderValue("Type")
  dataTreePanel.getTableHeader.getColumnModel.getColumn(6).setHeaderValue("Index")
  dataTreePanel.addMouseListener(new MouseAdapter {
    override def mousePressed(e: MouseEvent) = {
      if (e.isPopupTrigger) {
        val clickedElement = dataTreePanel.getPathForLocation(e.getX, e.getY)
        dataTreePanel.getTreeSelectionModel.clearSelection
        dataTreePanel.getTreeSelectionModel.addSelectionPath(clickedElement)
        popup.show(dataTreePanel, e.getX, e.getY)
      }
    }
  })

  val actionListener = buildActionListener { event =>
      event.getActionCommand match {
        case "Plot" => plotCurrentSelection
      }
  }

  def plotCurrentSelection {
    val node = dataTreePanel.getTreeSelectionModel.getSelectionPath.getLastPathComponent
    if (isPlotable(node.asInstanceOf[BOMNode])) {
      val array = node.asInstanceOf[BOMArray]
      val numbers = new ArrayList[Double]
      var min = (array/0).asInstanceOf[BOMNumber].value.doubleValue
      var max = min
      for (i <- 0 until array.length.toInt) {
        numbers.add((array/i).asInstanceOf[BOMNumber].value.doubleValue)
        if (numbers(i) > max) max = numbers(i)
        if (numbers(i) < min) min = numbers(i)
      }
      val plot = new JXGraph.Plot {
        def compute(x: Double) = {
          val index = x.toInt
          if (index >= 0 && index < array.length) {
            numbers(index)
          } else {
            0.0
          }
        }
      }
      val graph = new JXGraph
      val frame = new JFrame("Plot")
      graph.setOrigin(new Point2D.Double(0.0, 0.0))
      graph.setView(new Rectangle2D.Double(0.0, min, array.length, max - min))
      graph.setGridPainted(false)
      graph.setTextPainted(false) // if turned on, can be very slow
      graph.addPlots(Color.red, plot)
      frame.add(graph)
      frame.setSize(800, 600)
      frame.setVisible(true)
    } else {
      JOptionPane.showMessageDialog(null, "Choose an array with numbers...",
                                    null, JOptionPane.WARNING_MESSAGE)
    }
  }

  def isPlotable(node: BOMNode): Boolean =
    node.schema.isInstanceOf[SchemaArray] &&
    node.schema.asInstanceOf[SchemaArray].children.head.isInstanceOf[SchemaNumber]

  val plotMenuItem = new JMenuItem("Plot")
  plotMenuItem.addActionListener(actionListener)

  val popup = new JPopupMenu
  popup.add(plotMenuItem)

  val scrollPane = new JScrollPane(dataTreePanel)

}

/**
 * Holds the setup of the BomBrowser and ensure persistent of it.
 */
object BomBrowserSetup {

  import scala.xml._

  private val bomDirectory = new File(System.getProperty("user.home") +
                                      System.getProperty("file.separator") +
                                      ".bom")
  if (!bomDirectory.exists) bomDirectory.mkdirs

  private val setupFile = new File(bomDirectory.getAbsolutePath +
                                   System.getProperty("file.separator") +
                                   "BomBrowserSetup.xml")

  private var currentBinFile = readCurrentFile
  def currentFile: File = currentBinFile
  def currentFile_=(file: File) = {
    currentBinFile = file
    writeSetup(setup)
  }

  object VoidSchema extends Schema { def schema = null }
  private var currentSchemaClass: Schema = readCurrentSchema
  def currentSchema: Schema = currentSchemaClass
  def currentSchema_=(schema: Schema) {
    currentSchemaClass = schema
    writeSetup(setup)
  }

  private def readCurrentFile: File = setupFile.exists match {
    case true => new File((readSetup \\ "file").text)
    case false => null
  }

  private def readCurrentSchema: Schema = setupFile.exists match {
    case true => {
        val className = (readSetup \\ "schema").text
        Class.forName(className).newInstance.asInstanceOf[Schema]
    }
    case false => VoidSchema
  }

  private def setup: Node =
    <BomBrowser>
      <file>{if (currentFile == null) "" else currentFile.getAbsolutePath}</file>
      <schema>{currentSchema.getClass.getName}</schema>
    </BomBrowser>

  private def readSetup: Node = XML.loadFile(setupFile)
  private def writeSetup(setup: Node) {
    val writer = new FileWriter(setupFile)
    XML.write(writer, setup, "UTF-8", false, null)
    writer.close
  }

}

/**
 * A classloader implementation that loads classes from an abstract directory structure.
 */
import scala.tools.nsc._
import scala.tools.nsc.io._
import scala.tools.nsc.util._
class AbstractFileClassLoader(abstractDir: AbstractFile) extends ClassLoader(getClass.getClassLoader) {

  override def findClass(name: String): Class[_] = {
    var file: AbstractFile = null
    var dir = abstractDir
    val names = name.split('.').foreach(x => {
      if (dir.lookupName(x, true) == null) {
        file = dir.lookupName(x+".class", false)
      } else {
        dir = dir.lookupName(x, true)
      }
    })
    val bytes = file.toByteArray
    defineClass(name, bytes, 0, bytes.length)
  }
  
}

/**
 * Utility object for compiling BOM schemas.
 */
object BomSchemaCompiler {

  /**
   * Compiles a scala file and returns an instance of the Schema class that has
   * been compiled if it exist.
   */
  def compile(file: File): Schema = {
    val fileToCompile = AbstractFile.getFile(file)
    val virtualDirectory = new BugFixVirtualDirectory("(memory)", None)
    val settings = {
      val sets = new Settings
      sets.classpath.value = System.getProperty("java.class.path")
      sets
    }
    val compiler: Global = {
      val comp = new Global(settings)
      comp.genJVM.outputDir = virtualDirectory
      comp
    }
    val cr = new compiler.Run
    cr.compileSources(new BatchSourceFile(fileToCompile) :: Nil)
    val cl = new AbstractFileClassLoader(virtualDirectory)
    findSchema(virtualDirectory, cl, "")
  }

  private def findSchema(f: AbstractFile, cl: ClassLoader, className: String): Schema = {
    f.elements.foreach( x => { 
      val newClassName = if (className == "") x.name else className + "." + x.name
      if (x.isDirectory) {
        val schema = findSchema(x, cl, newClassName)
        if (schema != null) {
          return schema
        }
      } else {
        val bytes = x.toByteArray
        val name = newClassName.take(newClassName.length - 6)
        val schemaClass = cl.loadClass(name)
        schemaClass.getInterfaces.foreach(interface => {
          if (interface.getName == "bom.schema.Schema") {
            return schemaClass.newInstance.asInstanceOf[bom.schema.Schema]
          }
        })
      }
    })
    null
  }
  
}

