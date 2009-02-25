package bom.dom

import bom.BOMException
import bom.BOMNode

import org.w3c.dom.Node

class AttributeNamedNodeMap(node: BOMNode) extends AbstractNamedNodeMap {
    
  def getLength: Int = 1

  override def getNamedItem(name: String): Node = {
    if (name.equals("index")) {
      new AttributeAdapter(node, "index", Integer.toString(node.index))
    }
    throw new BOMException
  }
    
  override def item(index: Int): Node = {
    // FIXME: shouldn't it return the xpath index (i.e. starting from 1...) ?
    if (index == 0) new AttributeAdapter(node, "index", ""+node.index)
    else throw new BOMException
  }
    
}
