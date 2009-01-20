package bom.dom

import org.w3c.dom._

class BOMBlobAdapter(bomBlob: BOMBlob) extends ElementAdapter(bomBlob) {

  override def getChildNodes: NodeList =
    new NodeList {
      def getLength: int = 1
      def item(index: int): Node = {
        if (index != 0) {
          throw new BOMException("Index out of bound!")
        }
        new TextAdapter(bomBlob)
      }
    }

  override def getFirstChild: Node = new TextAdapter(bomBlob)
  
}
