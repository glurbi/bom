package bom.dom

import org.w3c.dom._

class BOMSequenceAdapter(bomSequence: BOMSequence) extends ElementAdapter(bomSequence) {

  override def getFirstChild: Node =
    bomSequence.schema.children.get(0).createNode(bomSequence, 0).asDomNode

  override def getChildNodes: NodeList =
    new NodeList {
      def getLength: int = bomSequence.schema.childrenCount
      def item(index: int): Node =
        bomSequence.schema.children.get(index).createNode(bomSequence, index).asDomNode
    }

}
