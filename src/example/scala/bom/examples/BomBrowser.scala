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

object BomBrowser {

  def main(args: Array[String]) {
    val schema = Class.forName(args(0)).getMethod("schema").
      invoke(null, null).asInstanceOf[BOMSchemaDocument]
    val bspace = new MemoryBinarySpace(new FileInputStream(args(1)))
    val doc = new BOMDocument(schema, bspace)
    val bomBrowser = new BomBrowserFoo(doc)
    bomBrowser.show
  }

}

// NB: for some obscure reason, if the class has the same name as the
// companion object, netbeans complains with "java.lang.NoSuchMethodError: main"
class BomBrowserFoo(val doc: BOMDocument) {

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
    private def translateSchema(schema: BOMSchemaElement): String = schema match {
      case BOMSchemaSequence(_, _, _) => "sequence"
      case BOMSchemaArray(_, _, _) => "array"
      case BOMSchemaBlob(_, _, _) => "blob"
      case BOMSchemaNumber(_, _, _) => "number"
      case BOMSchemaString(_, _, _) => "string"
      case BOMSchemaVirtual(_, _, _) => "virtual"
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

  val actionListener = new ActionListener {
    def actionPerformed(event: ActionEvent) = {
      event.getActionCommand match {
        case "Plot" => plotCurrentSelection
      }
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
      JOptionPane.showMessageDialog(frame, "Choose an array with numbers...",
                                    null, JOptionPane.WARNING_MESSAGE)
    }
  }

  def isPlotable(node: BOMNode): Boolean = 
    node.schema.isInstanceOf[BOMSchemaArray] &&
    node.schema.asInstanceOf[BOMSchemaArray].children.head.isInstanceOf[BOMSchemaNumber]
  
  val plotMenuItem = new JMenuItem("Plot")
  plotMenuItem.addActionListener(actionListener)

  val popup = new JPopupMenu
  popup.add(plotMenuItem)

  val scrollPane = new JScrollPane(dataTreePanel)

  val frame = new JFrame
  frame.add(scrollPane)
  frame.setTitle("BOM Browser")
  frame.setSize(1280, 600)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  def show { frame.setVisible(true) }

}
