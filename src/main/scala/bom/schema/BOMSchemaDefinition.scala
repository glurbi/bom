package bom.schema

import java.util._
import bom._
import bom.bin._
import bom.types._


case class BOMSchemaDefinition(override val sizeFun: BOMNode => Long)
  extends BOMSchemaElement {

  override val name: String = null
  override val parent: BOMSchemaElement = null

  var byteOrder: ByteOrder = null
  private var child: BOMSchemaElement = null

  def createNode(parent: BOMContainer, index: int) = throw new UnsupportedOperationException
  
  def createNode(bspace: BOMBinarySpace, parent: BOMContainer, index: int): BOMNode = {
    val document = new BOMDocument(this, bspace)
    document
  }

  def createDocument(bspace: BOMBinarySpace): BOMDocument =
    createNode(bspace, null, 0).asInstanceOf[BOMDocument]

  override def depth: int = 0
  
  def appendChild(schemaElement: BOMSchemaElement) =
    if (child == null)
      child = schemaElement
    else
      throw new BOMException

  def childrenCount: int = if (child == null) 0 else 1

  override def children: List[BOMSchemaElement] = {
    val children = new ArrayList[BOMSchemaElement]
    children.add(child)
    children
  }

}
