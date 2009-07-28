package bom

import bom.BOM._
import bom.bin._
import bom.schema._

abstract class BOMBaseNode(
  val schema: SchemaElement,
  val parent: BOMContainer,
  val index: Int)
extends BOMNode {

  def name: String = schema.name
  def document: BOMDocument = parent.document
  def depth: Int = schema.depth
  def value: Any = null
  def identifier: BOMIdentifier = id
  def position: Long = pos
  def binarySpace: BinarySpace = document.binarySpace

  private lazy val id = index :: parent.identifier
  private lazy val pos = schema.positionFun(this)
  
}
