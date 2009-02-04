package bom.examples

import java.io._
import javax.swing._
import javax.swing.event._
import java.awt.event._

import org.jdesktop.swingx._
import org.jdesktop.swingx.treetable._

import bom.bin._
import bom.schema._
import bom.types._
import bom.stream._

object BomBrowser {

  def main(args: Array[String]) = {
    val schema = Class.forName(args(0)).getMethod("schema").
      invoke(null, null).asInstanceOf[BOMSchemaElement]
    val bspace = new MemoryBinarySpace(new FileInputStream(args(1)))
    val doc = new BOMDocument(schema, bspace)
    val bomBrowser = new BomBrowserFoo(doc)
    bomBrowser.show
  }

}

class BomBrowserFoo(val doc: BOMDocument) {

  val dataTreeModel = new AbstractTreeTableModel(doc) {

    def getColumnCount: Int = 4

    def getValueAt(node: Object, column: Int): Object = column match {
      case 0 => node.asInstanceOf[BOMNode].name
      case 1 => translateValue(node.asInstanceOf[BOMNode])
      case 2 => "" + node.asInstanceOf[BOMNode].position / 8
      case 3 => translateSchema(node.asInstanceOf[BOMNode].schema)
    }

    def getChild(parent: Object, index: Int): Object =
      parent.asInstanceOf[BOMNode].child(index)

    def getChildCount(parent: Object): Int =
      parent.asInstanceOf[BOMNode].childCount

    def getIndexOfChild(parent: Object, child: Object): Int =
      child.asInstanceOf[BOMNode].index

    override def isLeaf(node: Object): Boolean = node.isInstanceOf[BOMLeaf]

    private def translateValue(node: BOMNode): String =
      node match {
        case BOMBlob(_, _, _, _) => "..."
        case BOMContainer(_, _, _) => ""
        case _ => node.value.toString
      }

    private def translateSchema(schema: BOMSchemaElement): String = schema match {
      case BOMSchemaSequence(_, _, _, _) => "sequence"
      case BOMSchemaArray(_, _, _, _) => "array"
      case BOMSchemaBlob(_, _, _, _) => "blob"
      case BOMSchemaNumber(_, _, _, _) => "number"
      case BOMSchemaString(_, _, _, _) => "string"
      case BOMSchemaVirtual(_, _, _) => "virtual"
      case _ => "unknown"
    }

  }

  val dataTreePanel = new JXTreeTable(dataTreeModel)
  dataTreePanel.getTableHeader.getColumnModel.getColumn(0).setHeaderValue("Node")
  dataTreePanel.getTableHeader.getColumnModel.getColumn(1).setHeaderValue("Value")
  dataTreePanel.getTableHeader.getColumnModel.getColumn(2).setHeaderValue("Position")
  dataTreePanel.getTableHeader.getColumnModel.getColumn(3).setHeaderValue("Type")
  dataTreePanel.addMouseListener(new MouseAdapter {
    override def mousePressed(e: MouseEvent) = {
      if (e.isPopupTrigger) {
        val clickedElement = dataTreePanel.getPathForLocation(e.getX, e.getY)
        println
      }
    }
  })

  val scrollPane = new JScrollPane(dataTreePanel)

  val frame = new JFrame
  frame.add(scrollPane)
  frame.setTitle("BOM Browser")
  frame.setSize(800, 600)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  def show = frame.show

}
