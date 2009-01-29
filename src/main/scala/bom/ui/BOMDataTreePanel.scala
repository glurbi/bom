package bom.ui

import javax.swing._
import org.jdesktop.swingx._
import org.jdesktop.swingx.treetable._

class BOMDataTreePanel(val doc: BOMDocument)
  extends JScrollPane(new JXTreeTable(new BOMDataTreeModel(doc)))
{

}
