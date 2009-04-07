package bom.dom

import bom._
import org.w3c.dom._

class ElementAdapter(node: BOMNode) extends NodeAdapter(node) with Element {

  override def getAttributes: NamedNodeMap = new AttributeNamedNodeMap(node)

  override def getNodeType: Short = Node.ELEMENT_NODE

  override def getNodeValue: String = null

  def getAttribute(name: String): String = error("Not implemented!")

  def getAttributeNS(namespaceURI: String, localName: String): String =
    error("Not implemented!")

  def getAttributeNode(name: String): Attr = error("Not implemented!")

  def getAttributeNodeNS(namespaceURI: String, localName: String): Attr =
    error("Not implemented!")

  def getElementsByTagName(name: String): NodeList = error("Not implemented!")

  def getElementsByTagNameNS(namespaceURI: String, localName: String): NodeList =
    error("Not implemented!")

  def getSchemaTypeInfo: TypeInfo = error("Not implemented!")

  def getTagName: String = error("Not implemented!")

  def hasAttribute(name: String): Boolean = error("Not implemented!")

  def hasAttributeNS(namespaceURI: String, localName: String): Boolean =
    error("Not implemented!")

  def removeAttribute(name: String) = error("Not implemented!")

  def removeAttributeNS(namespaceURI: String, localName: String) =
    error("Not implemented!")

  def removeAttributeNode(oldAttr: Attr): Attr = error("Not implemented!")

  def setAttribute(name: String, value: String) = error("Not implemented!")

  def setAttributeNS(namespaceURI: String, qualifiedName: String, value: String) =
    error("Not implemented!")

  def setAttributeNode(newAttr: Attr): Attr = error("Not implemented!")

  def setAttributeNodeNS(newAttr: Attr): Attr = error("Not implemented!")

  def setIdAttribute(name: String, isId: Boolean) = error("Not implemented!")

  def setIdAttributeNS(namespaceURI: String, localName: String, isId: Boolean) =
    error("Not implemented!")

  def setIdAttributeNode(idAttr: Attr, isId: Boolean) = error("Not implemented!")

}
