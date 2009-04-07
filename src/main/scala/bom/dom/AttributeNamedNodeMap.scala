package bom.dom

import bom.BOMNode

import org.w3c.dom.Node

class AttributeNamedNodeMap(node: BOMNode) extends AbstractNamedNodeMap {
    
  def getLength: Int = 1

  override def getNamedItem(name: String): Node = {
    if (name.equals("index")) {
      new AttributeAdapter(node, "index", Integer.toString(node.index))
    } else {
      error("Invalid name: " + name)
    }
  }
    
  override def item(index: Int): Node = {
    if (index == 0) {
      // we return the XPath index (starts at 1...)
      new AttributeAdapter(node, "index", ""+(node.index+1))
    } else {
      error("Invalid index: " + index)
    }
  }
    
}
