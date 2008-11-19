package bom.dom

import org.w3c.dom._

class BOMNumberAdapter(bomNumber: BOMNumber) extends ElementAdapter(bomNumber) {

  override def getChildNodes: NodeList =
    new NodeList {
      def getLength: int = 1
      def item(index: int): Node = {
        if (index != 0) {
          throw new BOMException("Index out of bound!")
        }
        new TextAdapter(bomNumber)
      }
    }

  override def getFirstChild: Node = new TextAdapter(bomNumber)
  
}
