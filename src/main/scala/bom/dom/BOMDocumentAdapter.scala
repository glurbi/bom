package bom.dom

import org.w3c.dom._

class BOMDocumentAdapter(bomDocument: BOMDocument) extends DocumentAdapter(bomDocument) {
  
  override def getFirstChild: Node = bomDocument.rootNode.asDomNode

  override def getChildNodes: NodeList =
    new NodeList {
      def getLength: Int = 1
      def item(index: Int): Node = {
        if (index != 0) {
          throw new IndexOutOfBoundsException("" + index)
        }
        getFirstChild
      }
    }

}
