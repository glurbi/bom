package bom.dom

import bom._
import org.w3c.dom._

class TextAdapter(node: BOMLeaf) extends NodeAdapter(node) with Text {

  def getNodeType: short = Node.TEXT_NODE

  override def getNodeValue: String = node.value.toString
  
  def getWholeText: String = throw new BOMException

  def isElementContentWhitespace: boolean = throw new BOMException

  def replaceWholeText(content: String): Text = throw new BOMException

  def splitText(offset: int): Text = throw new BOMException

  def appendData(arg: String) = throw new BOMException

  def deleteData(offset: int, count: int) = throw new BOMException

  def getData: String = throw new BOMException

  def getLength: int = throw new BOMException

  def insertData(offset: int, arg: String) = throw new BOMException

  def replaceData(offset: int, count: int, arg: String) = throw new BOMException

  def setData(data: String) = throw new BOMException

  def substringData(offset: int, count: int): String = throw new BOMException

}
