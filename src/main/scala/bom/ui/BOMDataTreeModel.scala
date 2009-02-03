package bom.ui

import org.jdesktop.swingx.treetable._

import bom.schema._

class BOMDataTreeModel(val doc: BOMDocument) extends AbstractTreeTableModel(doc) {

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
