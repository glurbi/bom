package bom.ui

import javax.swing._

import org.jdesktop.swingx._

import bom._

class BOMDataTreePanel(val doc: BOMDocument) extends JScrollPane {

  private val treeTable = new JXTreeTable(new BOMDataTreeModel(doc))
  treeTable.getTableHeader.getColumnModel.getColumn(0).setHeaderValue("Node")
  treeTable.getTableHeader.getColumnModel.getColumn(1).setHeaderValue("Value")
  treeTable.getTableHeader.getColumnModel.getColumn(2).setHeaderValue("Position")
  treeTable.getTableHeader.getColumnModel.getColumn(3).setHeaderValue("Type")
  setViewportView(treeTable)
  
}
