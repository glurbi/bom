package bom.dom

import bom._
import org.w3c.dom._

class DocumentAdapter(node: BOMNode)
  extends NodeAdapter(node) with Document {

  def getNodeType: Short = Node.DOCUMENT_NODE
  
  def adoptNode(source: Node): Node = error("Not implemented!")

  def createAttribute(name: String): Attr = error("Not implemented!")

  def createAttributeNS(namespaceURI: String, qualifiedName: String): Attr =
    error("Not implemented!")

  def createCDATASection(data: String): CDATASection = error("Not implemented!")

  def createComment(data: String): Comment = error("Not implemented!")

  def createDocumentFragment: DocumentFragment = error("Not implemented!")

  def createElement(tagName: String): Element = error("Not implemented!")

  def createElementNS(namespaceURI: String, qualifiedName: String): Element =
    error("Not implemented!")

  def createEntityReference(name: String): EntityReference =
    error("Not implemented!")

  def createProcessingInstruction(target: String, data: String): ProcessingInstruction =
    error("Not implemented!")
  
  def createTextNode(data: String): Text = error("Not implemented!")

  def getDoctype: DocumentType = error("Not implemented!")

  def getDocumentElement: Element = error("Not implemented!")

  def getDocumentURI: String = error("Not implemented!")

  def getDomConfig: DOMConfiguration = error("Not implemented!")

  def getElementById(elementId: String): Element = error("Not implemented!")

  def getElementsByTagName(tagname: String): NodeList = error("Not implemented!")

  def getElementsByTagNameNS(namespaceURI: String, localName: String): NodeList =
    error("Not implemented!")

  def getImplementation: DOMImplementation = error("Not implemented!")

  def getInputEncoding: String = error("Not implemented!")

  def getStrictErrorChecking: Boolean = error("Not implemented!")

  def getXmlEncoding: String = error("Not implemented!")

  def getXmlStandalone: Boolean = error("Not implemented!")

  def getXmlVersion: String = error("Not implemented!")

  def importNode(importedNode: Node, deep: Boolean): Node =
    error("Not implemented!")

  def normalizeDocument = error("Not implemented!")

  def renameNode(n: Node, namespaceURI: String, qualifiedName: String): Node =
    error("Not implemented!")

  def setDocumentURI(documentURI: String) = error("Not implemented!")

  def setStrictErrorChecking(strictErrorChecking: Boolean) =
    error("Not implemented!")

  def setXmlStandalone(xmlStandalone: Boolean) = error("Not implemented!")

  def setXmlVersion(xmlVersion: String) = error("Not implemented!")

}
