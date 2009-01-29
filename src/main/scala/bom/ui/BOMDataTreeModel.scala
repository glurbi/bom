package bom.ui

import org.jdesktop.swingx.treetable._

class BOMDataTreeModel(val doc: BOMDocument) extends AbstractTreeTableModel(doc) {

  def getColumnCount: Int = 3

  def getValueAt(node: Object, column: Int): Object = {
    val bom = node.asInstanceOf[BOMNode]
    bom.name
  }

  def getChild(parent: Object, index: Int): Object = {
    val bom = parent.asInstanceOf[BOMNode]
    bom
  }
  
  def getChildCount(parent: Object): Int = {
    1
  }

  def getIndexOfChild(parent: Object, child: Object): Int = {
    1
  }

  override def isLeaf(node: Object): Boolean = false

}
