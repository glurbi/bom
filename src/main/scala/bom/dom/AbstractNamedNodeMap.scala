package bom.dom

import bom.BOMException

import org.w3c.dom.DOMException
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node

abstract class AbstractNamedNodeMap extends NamedNodeMap {

  def getNamedItem(name: String): Node = throw new BOMException

  def getNamedItemNS(namespaceURI: String, localName: String): Node =
    throw new BOMException

  def item(index: int): Node = throw new BOMException

  def removeNamedItem(name: String): Node = throw new BOMException

  def removeNamedItemNS(namespaceURI: String, localName: String): Node =
    throw new BOMException

  def setNamedItem(arg: Node): Node = throw new BOMException

  def setNamedItemNS(arg: Node): Node = throw new BOMException

}
