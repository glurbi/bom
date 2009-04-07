package bom.dom

import org.w3c.dom._

class BOMLeafAdapter(bomLeaf: BOMLeaf) extends ElementAdapter(bomLeaf) {

  override def getChildNodes: NodeList =
    new NodeList {
      def getLength: Int = 1
      def item(index: Int): Node = {
        if (index != 0) {
          error("Invalid index: " + index)
        }
        new TextAdapter(bomLeaf)
      }
    }

  override def getFirstChild: Node = new TextAdapter(bomLeaf)
  
}
