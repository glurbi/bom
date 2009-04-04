package bom.dom

import java.util._
import org.w3c.dom._
import bom.dom._

class BOMArrayAdapter(bomArray: BOMArray) extends ElementAdapter(bomArray) {

  override def getChildNodes: NodeList =
    new NodeList {
      def getLength: Int = bomArray.childrenCount
      def item(index: Int): Node = bomArray(index).asDomNode
    }

}
