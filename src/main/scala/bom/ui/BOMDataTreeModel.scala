package bom.ui

import org.jdesktop.swingx.treetable._

class BOMDataTreeModel(val doc: BOMDocument) extends AbstractTreeTableModel(doc) {

  def getColumnCount: Int = 3

  def getValueAt(node: Object, column: Int): Object =
    node.asInstanceOf[BOMNode].name

  def getChild(parent: Object, index: Int): Object =
    parent.asInstanceOf[BOMNode].child(index)

  def getChildCount(parent: Object): Int =
    parent.asInstanceOf[BOMNode].childCount

  def getIndexOfChild(parent: Object, child: Object): Int =
    child.asInstanceOf[BOMNode].index

  override def isLeaf(node: Object): Boolean = node.isInstanceOf[BOMLeaf]

}
