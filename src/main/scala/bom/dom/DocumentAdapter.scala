package bom.dom

import bom._
import org.w3c.dom._

class DocumentAdapter(node: BOMNode)
  extends NodeAdapter(node) with Document {

  def getNodeType: Short = Node.DOCUMENT_NODE
  
  def adoptNode(source: Node): Node = throw new BOMException

  def createAttribute(name: String): Attr = throw new BOMException

  def createAttributeNS(namespaceURI: String, qualifiedName: String): Attr =
    throw new BOMException

  def createCDATASection(data: String): CDATASection = throw new BOMException

  def createComment(data: String): Comment = throw new BOMException

  def createDocumentFragment: DocumentFragment = throw new BOMException

  def createElement(tagName: String): Element = throw new BOMException

  def createElementNS(namespaceURI: String, qualifiedName: String): Element =
    throw new BOMException

  def createEntityReference(name: String): EntityReference =
    throw new BOMException

  def createProcessingInstruction(target: String, data: String): ProcessingInstruction =
    throw new BOMException
  
  def createTextNode(data: String): Text = throw new BOMException

  def getDoctype: DocumentType = throw new BOMException

  def getDocumentElement: Element = throw new BOMException

  def getDocumentURI: String = throw new BOMException

  def getDomConfig: DOMConfiguration = throw new BOMException

  def getElementById(elementId: String): Element = throw new BOMException

  def getElementsByTagName(tagname: String): NodeList = throw new BOMException

  def getElementsByTagNameNS(namespaceURI: String, localName: String): NodeList =
    throw new BOMException

  def getImplementation: DOMImplementation = throw new BOMException

  def getInputEncoding: String = throw new BOMException

  def getStrictErrorChecking: Boolean = throw new BOMException

  def getXmlEncoding: String = throw new BOMException

  def getXmlStandalone: Boolean = throw new BOMException

  def getXmlVersion: String = throw new BOMException

  def importNode(importedNode: Node, deep: Boolean): Node =
    throw new BOMException

  def normalizeDocument = throw new BOMException

  def renameNode(n: Node, namespaceURI: String, qualifiedName: String): Node =
    throw new BOMException

  def setDocumentURI(documentURI: String) = throw new BOMException

  def setStrictErrorChecking(strictErrorChecking: Boolean) =
    throw new BOMException

  def setXmlStandalone(xmlStandalone: Boolean) = throw new BOMException

  def setXmlVersion(xmlVersion: String) = throw new BOMException

}
