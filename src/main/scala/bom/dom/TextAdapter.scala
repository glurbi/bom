package bom.dom

import bom._
import org.w3c.dom._

class TextAdapter(node: BOMLeaf) extends NodeAdapter(node) with Text {

  def getNodeType: Short = Node.TEXT_NODE

  override def getNodeValue: String = node.value.toString
  
  def getWholeText: String = error("Not implemented!")

  def isElementContentWhitespace: Boolean = error("Not implemented!")

  def replaceWholeText(content: String): Text = error("Not implemented!")

  def splitText(offset: Int): Text = error("Not implemented!")

  def appendData(arg: String) = error("Not implemented!")

  def deleteData(offset: Int, count: Int) = error("Not implemented!")

  def getData: String = error("Not implemented!")

  def getLength: Int = error("Not implemented!")

  def insertData(offset: Int, arg: String) = error("Not implemented!")

  def replaceData(offset: Int, count: Int, arg: String) = error("Not implemented!")

  def setData(data: String) = error("Not implemented!")

  def substringData(offset: Int, count: Int): String = error("Not implemented!")

}
