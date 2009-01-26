package bom.dom

import bom.BOMException
import bom.BOMNode

import org.w3c.dom.Node

class AttributeNamedNodeMap(node: BOMNode) extends AbstractNamedNodeMap {
    
  def getLength: int = 1

  override def getNamedItem(name: String): Node = {
    if (name.equals("index")) {
      new AttributeAdapter(node, "index", Integer.toString(node.index))
    }
    throw new BOMException
  }
    
  override def item(index: int): Node = {
    if (index == 0) new AttributeAdapter(node, "index", ""+node.index)
    else throw new BOMException
  }
    
}