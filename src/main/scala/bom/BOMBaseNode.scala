package bom

import bom.BOM._
import bom.bin._
import bom.schema._

abstract case class BOMBaseNode(
  val schema: SchemaElement,
  val parent: BOMContainer,
  val index: Int)
extends BOMNode {
  
  def name: String = schema.name
  def document: BOMDocument = parent.document
  def depth: Int = schema.depth
  
  // Ideally, I would like to define an abstract method:
  // def size: Long
  // instead of the lazy val, but the tests fail (at least with version 2.7.3)
  lazy val size: Long = error("Not implemented!")

  def value: Any = null
  lazy val identifier: BOMIdentifier = index :: parent.identifier

  lazy val position: Long =
    if (schema.positionFun != null)  {
      schema.positionFun(this)
    } else if (index == 0) {
      parent.position;
    } else if (parent.isInstanceOf[BOMSequence]) {
      val previousSibling = parent.schema.children(index - 1).instance(parent, index - 1)
      previousSibling.position + previousSibling.size
    } else if (parent.isInstanceOf[BOMArray] && parent.schema.asInstanceOf[SchemaArray].regular) {
      parent.position + index * parent.schema.children(0).instance(parent, 0).size
    } else if (parent.isInstanceOf[BOMArray]) {
      (parent.asInstanceOf[BOMArray]/(index - 1)).position +
      (parent.asInstanceOf[BOMArray]/(index - 1)).size
    } else {
      error("unreachable statement?")
    }

  def binarySpace: BinarySpace = document.binarySpace

}
