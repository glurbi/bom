package bom.dom

import java.util._
import org.w3c.dom._
import bom.dom._

class BOMArrayAdapter(bomArray: BOMArray) extends ElementAdapter(bomArray) {

  override def getAttributes: NamedNodeMap =
    new AttributeNamedNodeMap(bomArray) {
      override def getLength: Int = super.getLength + 1
      override def item(index: Int): Node = {
        if (index == getLength() - 1) {
          val value = if (bomArray.arrayLengthExpression == null)
            ""+bomArray.arrayLength else bomArray.arrayLengthExpression
          new AttributeAdapter(bomArray, "length", value)
        } else super.item(index)
      }
    }
    
  override def getChildNodes: NodeList =
    new NodeList {
      def getLength: Int = bomArray.childrenCount
      def item(index: Int): Node = bomArray.child(index).asDomNode
    }

}
