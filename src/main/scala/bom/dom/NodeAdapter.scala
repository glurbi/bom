package bom.dom

import bom._
import bom.schema._
import org.w3c.dom._

abstract class NodeAdapter(var node: BOMNode) extends Node {

  def hasChildNodes: boolean = node.schema.childrenCount > 0

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
        result = node.parent.asInstanceOf[BOMArray].child(node.index - 1).asDomNode
      } else if (node.parent.isInstanceOf[BOMSequence]) {
        val previousNodeName = node.parent.schema.asInstanceOf[BOMSchemaSequence].children.get(node.index - 1).name
        result = node.parent.asInstanceOf[BOMSequence].child(previousNodeName).asDomNode
      } else {
        throw new BOMException
      }
    }
    result
  }

  def getAttributes: NamedNodeMap = new EmptyNamedNodeMap
  
  def getLocalName: String = node.name

  def getNamespaceURI: String = null

  def getNodeName: String = node.name
  
  def setNodeValue(nodeValue: String) = throw new BOMException

  def appendChild(newChild: Node): Node = throw new BOMException

  def compareDocumentPosition(other: Node): short = throw new BOMException

  def setPrefix(prefix: String) = throw new BOMException

  def getTextContent: String = throw new BOMException

  def cloneNode(deep: boolean): Node = throw new BOMException

  def getBaseURI: String = throw new BOMException

  def getChildNodes: NodeList = throw new BOMException

  def getFeature(feature: String, version: String): Object =
    throw new BOMException

  def getFirstChild: Node = throw new BOMException

  def getLastChild: Node = throw new BOMException

  def getNextSibling: Node = throw new BOMException

  def getNodeValue: String = throw new BOMException

  def getUserData(key: String): Object = throw new BOMException

  def hasAttributes: boolean = throw new BOMException

  def insertBefore(newChild: Node , refChild: Node): Node =
    throw new BOMException

  def isDefaultNamespace(namespaceURI: String): boolean = throw new BOMException

  def isEqualNode(arg: Node): boolean = throw new BOMException

  def isSameNode(other: Node): boolean = throw new BOMException

  def isSupported(feature: String, version: String): boolean =
    throw new BOMException

  def lookupNamespaceURI(prefix: String): String = throw new BOMException

  def lookupPrefix(namespaceURI: String): String = throw new BOMException

  def normalize = throw new BOMException

  def removeChild(oldChild: Node): Node = throw new BOMException

  def replaceChild(newChild: Node, oldChild: Node): Node =
    throw new BOMException

  def setTextContent(textContent: String) = throw new BOMException

  def setUserData(key: String, data: Any, handler: UserDataHandler): Object =
    throw new BOMException

}
