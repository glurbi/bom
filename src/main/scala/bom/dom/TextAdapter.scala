package bom.dom

import bom._
import org.w3c.dom._

class TextAdapter(node: BOMLeaf) extends NodeAdapter(node) with Text {

  def getNodeType: Short = Node.TEXT_NODE

  override def getNodeValue: String = node.value.toString
  
  def getWholeText: String = throw new BOMException

  def isElementContentWhitespace: Boolean = throw new BOMException

  def replaceWholeText(content: String): Text = throw new BOMException

  def splitText(offset: Int): Text = throw new BOMException

  def appendData(arg: String) = throw new BOMException

  def deleteData(offset: Int, count: Int) = throw new BOMException

  def getData: String = throw new BOMException

  def getLength: Int = throw new BOMException

  def insertData(offset: Int, arg: String) = throw new BOMException

  def replaceData(offset: Int, count: Int, arg: String) = throw new BOMException

  def setData(data: String) = throw new BOMException

  def substringData(offset: Int, count: Int): String = throw new BOMException

}
