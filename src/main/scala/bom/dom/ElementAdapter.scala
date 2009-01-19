package bom.dom

import bom._
import org.w3c.dom._

class ElementAdapter(node: BOMNode) extends NodeAdapter(node) with Element {

  override def getAttributes: NamedNodeMap = new AttributeNamedNodeMap(node)

  override def getNodeType: short = Node.ELEMENT_NODE

  override def getNodeValue: String = null

  def getAttribute(name: String): String = throw new BOMException

  def getAttributeNS(namespaceURI: String, localName: String): String =
    throw new BOMException

  def getAttributeNode(name: String): Attr = throw new BOMException

  def getAttributeNodeNS(namespaceURI: String, localName: String): Attr =
    throw new BOMException

  def getElementsByTagName(name: String): NodeList = throw new BOMException

  def getElementsByTagNameNS(namespaceURI: String, localName: String): NodeList =
    throw new BOMException

  def getSchemaTypeInfo: TypeInfo = throw new BOMException

  def getTagName: String = throw new BOMException

  def hasAttribute(name: String): boolean = throw new BOMException

  def hasAttributeNS(namespaceURI: String, localName: String): boolean =
    throw new BOMException

  def removeAttribute(name: String) = throw new BOMException

  def removeAttributeNS(namespaceURI: String, localName: String) =
    throw new BOMException

  def removeAttributeNode(oldAttr: Attr): Attr = throw new BOMException

  def setAttribute(name: String, value: String) = throw new BOMException

  def setAttributeNS(namespaceURI: String, qualifiedName: String, value: String) =
    throw new BOMException

  def setAttributeNode(newAttr: Attr): Attr = throw new BOMException

  def setAttributeNodeNS(newAttr: Attr): Attr = throw new BOMException

  def setIdAttribute(name: String, isId: boolean) = throw new BOMException

  def setIdAttributeNS(namespaceURI: String, localName: String, isId: boolean) =
    throw new BOMException

  def setIdAttributeNode(idAttr: Attr , isId: boolean) = throw new BOMException

}
