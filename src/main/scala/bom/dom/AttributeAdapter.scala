package bom.dom

import bom.BOMNode;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

class AttributeAdapter(node: BOMNode, name: String, value: String)
  extends NodeAdapter(node) with Attr {

  def getNodeType: Short = Node.ATTRIBUTE_NODE

  def getName: String = name

  override def getNodeName: String = name

  override def getLocalName: String = null

  def getValue: String = value

  override def hasChildNodes: Boolean = false

  override def getNodeValue: String = value

  def getOwnerElement: Element = error("Not implemented!")

  def getSchemaTypeInfo: TypeInfo = error("Not implemented!")

  def getSpecified: Boolean = error("Not implemented!")

  def isId: Boolean = error("Not implemented!")

  def setValue(value: String) = error("Not implemented!")

}
