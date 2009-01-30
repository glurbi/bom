package bom.ui

import org.jdesktop.swingx.treetable._

class BOMDataTreeModel(val doc: BOMDocument) extends AbstractTreeTableModel(doc) {

  def getColumnCount: Int = 4

  def getValueAt(node: Object, column: Int): Object = column match {
    case 0 => node.asInstanceOf[BOMNode].name
    case 1 => translate(node.asInstanceOf[BOMNode].value)
    case 2 => "" + node.asInstanceOf[BOMNode].position / 8
    case 3 => node.asInstanceOf[BOMNode].schema
  }

  def getChild(parent: Object, index: Int): Object =
    parent.asInstanceOf[BOMNode].child(index)

  def getChildCount(parent: Object): Int =
    parent.asInstanceOf[BOMNode].childCount

  def getIndexOfChild(parent: Object, child: Object): Int =
    child.asInstanceOf[BOMNode].index

  override def isLeaf(node: Object): Boolean = node.isInstanceOf[BOMLeaf]

  private def translate(value: Any): String =
    value match {
      case null => ""
      case _ => value.toString
    }

}
