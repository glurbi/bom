package bom.schema

import java.util._
import bom.bin._
import bom._

case class BOMSchemaDocument extends BOMSchemaElement {

  val name: String = null

  val parent: BOMSchemaElement = null

  var root: BOMSchemaElement = _

  def createNode(bspace: BOMBinarySpace, parent: BOMContainer, index: int): BOMNode =
    new BOMDocument(this, bspace)

  override def depth: int = 0

  override def children: List[BOMSchemaElement] = {
    val children = new ArrayList[BOMSchemaElement]
    children.add(root)
    children
  }

  override def appendChild(schemaElement: BOMSchemaElement) = {
    if (root == null) {
      root = schemaElement
    } else {
      throw new BOMException("Cannot have more than one root in a document.")
    }
  }

  override def createNode(parent: BOMContainer, index: Int): BOMNode =
    throw new UnsupportedOperationException

  def createDocument(bspace: BOMBinarySpace): BOMDocument =
    createNode(bspace, null, 0).asInstanceOf[BOMDocument]

  def size: BOMNode => Long = (n:BOMNode) => n.size

}
