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
  lazy val position: Long = schema.positionFun(this)
  def binarySpace: BinarySpace = document.binarySpace

}
