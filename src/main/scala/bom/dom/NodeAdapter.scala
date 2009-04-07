package bom.dom

import bom._
import bom.schema._
import org.w3c.dom._

abstract class NodeAdapter(var node: BOMNode) extends Node {

  def hasChildNodes: Boolean = node.schema.children.size > 0

  def getOwnerDocument: Document = {
    var e = node
    while (!(e.isInstanceOf[BOMDocument])) {
      e = e.parent
    }
    e.asDomNode.asInstanceOf[Document]
  }

  def getParentNode: Node = {
    if (node.parent == null)
      null
    else
      node.parent.asDomNode
  }

  def getPrefix: String = null

  def getPreviousSibling: Node = {
    var result: Node = null
    if (node.parent != null && node.index > 0) {
      if (node.parent.isInstanceOf[BOMArray]) {
        result = (node.parent.asInstanceOf[BOMArray]/(node.index - 1)).asDomNode
      } else if (node.parent.isInstanceOf[BOMSequence]) {
        val previousNodeName = node.parent.schema.asInstanceOf[BOMSchemaSequence].children(node.index - 1).name
        result = (node.parent.asInstanceOf[BOMSequence]/previousNodeName).asDomNode
      } else {
        error("Unreachable code?")
      }
    }
    result
  }

  def getAttributes: NamedNodeMap = new EmptyNamedNodeMap
  
  def getLocalName: String = node.name

  def getNamespaceURI: String = null

  def getNodeName: String = node.name
  
  def setNodeValue(nodeValue: String) = error("Not implemented!")

  def appendChild(newChild: Node): Node = error("Not implemented!")

  def compareDocumentPosition(other: Node): Short = error("Not implemented!")

  def setPrefix(prefix: String) = error("Not implemented!")

  def getTextContent: String = error("Not implemented!")

  def cloneNode(deep: Boolean): Node = error("Not implemented!")

  def getBaseURI: String = error("Not implemented!")

  def getChildNodes: NodeList = error("Not implemented!")

  def getFeature(feature: String, version: String): Object =
    error("Not implemented!")

  def getFirstChild: Node = error("Not implemented!")

  def getLastChild: Node = error("Not implemented!")

  def getNextSibling: Node = error("Not implemented!")

  def getNodeValue: String = error("Not implemented!")

  def getUserData(key: String): Object = error("Not implemented!")

  def hasAttributes: Boolean = error("Not implemented!")

  def insertBefore(newChild: Node , refChild: Node): Node =
    error("Not implemented!")

  def isDefaultNamespace(namespaceURI: String): Boolean = error("Not implemented!")

  def isEqualNode(arg: Node): Boolean = error("Not implemented!")

  def isSameNode(other: Node): Boolean = error("Not implemented!")

  def isSupported(feature: String, version: String): Boolean =
    error("Not implemented!")

  def lookupNamespaceURI(prefix: String): String = error("Not implemented!")

  def lookupPrefix(namespaceURI: String): String = error("Not implemented!")

  def normalize = error("Not implemented!")

  def removeChild(oldChild: Node): Node = error("Not implemented!")

  def replaceChild(newChild: Node, oldChild: Node): Node =
    error("Not implemented!")

  def setTextContent(textContent: String) = error("Not implemented!")

  def setUserData(key: String, data: Any, handler: UserDataHandler): Object =
    error("Not implemented!")

}
