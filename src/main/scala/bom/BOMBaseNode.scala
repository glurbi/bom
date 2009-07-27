package bom

import bom.BOM._
import bom.bin._
import bom.schema._

abstract class BOMBaseNode(
  val schema: SchemaElement,
  val parent: BOMContainer,
  val index: Int)
extends BOMNode {

  private lazy val lazyIdentifier = index :: parent.identifier
  private lazy val lazyPosition = schema.positionFun(this)
  
  def name: String = schema.name
  def document: BOMDocument = parent.document
  def depth: Int = schema.depth
  def value: Any = null
  def identifier: BOMIdentifier = lazyIdentifier
  def position: Long = lazyPosition
  def binarySpace: BinarySpace = document.binarySpace

}
