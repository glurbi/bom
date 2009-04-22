package bom.examples

import scala.collection.jcl._

import java.io._
import javax.swing._
import javax.swing.event._
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

object BomBrowser {

  def buildActionListener(fun: (ActionEvent) => Unit): ActionListener =
    new ActionListener {
      def actionPerformed(event: ActionEvent) = fun(event)
    }

  def main(args: Array[String]) {

    val bspace = new MemoryBinarySpace(new FileInputStream(BomBrowserSetup.currentFile))
    val doc = new BOMDocument(BomBrowserSetup.currentSchema.schema, bspace)
    var docHolder = new DocumentHolder(doc)

    val frame = new JFrame

    val fileMenu = new JMenu("File")
    val openMenuItem = new JMenuItem("Open...")
    openMenuItem.addActionListener(buildActionListener { _ =>
      val fileChooser = new JFileChooser
      if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        BomBrowserSetup.currentFile = fileChooser.getSelectedFile.getAbsoluteFile
        val bspace = new MemoryBinarySpace(new FileInputStream(BomBrowserSetup.currentFile))
        val doc = new BOMDocument(BomBrowserSetup.currentSchema.schema, bspace)
        val newDocHolder = new DocumentHolder(doc)
        frame.remove(docHolder.scrollPane)
        frame.add(newDocHolder.scrollPane)
        frame.setVisible(true)
        docHolder = newDocHolder
      }
    })
    fileMenu.add(openMenuItem)
    val exitMenuItem = new JMenuItem("Exit")
    exitMenuItem.addActionListener(buildActionListener { _ => System.exit(0) })
    fileMenu.add(exitMenuItem)
    val schemaMenu = new JMenu("Schema")
    val setSchemaMenuItem = new JMenuItem("Set...")
    setSchemaMenuItem.addActionListener(buildActionListener { _ =>
      val className = JOptionPane.showInputDialog("Please give the schema class name")
      val schema = Class.forName(className).newInstance.asInstanceOf[Schema]
      BomBrowserSetup.currentSchema = schema
    })
    schemaMenu.add(setSchemaMenuItem)

    val menuBar = new JMenuBar
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
      <file>{currentFile.getAbsolutePath}</file>
      <schema>{currentSchema.getClass.getName}</schema>
    </BomBrowser>

  private def readSetup: Node = XML.loadFile(setupFile)
  private def writeSetup(setup: Node) {
    val writer = new FileWriter(setupFile)
    XML.write(writer, setup, "UTF-8", false, null)
    writer.close
  }

}