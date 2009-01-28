package bom.dom

import org.w3c.dom._

class BOMSequenceAdapter(bomSequence: BOMSequence) extends ElementAdapter(bomSequence) {

  override def getFirstChild: Node =
    bomSequence.schema.children(0).instance(bomSequence, 0).asDomNode

  override def getChildNodes: NodeList =
    new NodeList {
      def getLength: Int = bomSequence.schema.children.size
      def item(index: Int): Node =
        bomSequence.schema.children(index).instance(bomSequence, index).asDomNode
    }

}
