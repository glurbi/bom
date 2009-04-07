package bom.dom

import org.w3c.dom.DOMException
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node

abstract class AbstractNamedNodeMap extends NamedNodeMap {

  def getNamedItem(name: String): Node = error("Not implemented!")

  def getNamedItemNS(namespaceURI: String, localName: String): Node =
    error("Not implemented!")

  def item(index: Int): Node = error("Not implemented!")

  def removeNamedItem(name: String): Node = error("Not implemented!")

  def removeNamedItemNS(namespaceURI: String, localName: String): Node =
    error("Not implemented!")

  def setNamedItem(arg: Node): Node = error("Not implemented!")

  def setNamedItemNS(arg: Node): Node = error("Not implemented!")

}
