package bom.schema

import bom.bin._
import bom._

case class BOMSchemaDocument() extends BOMSchemaElement {

  val name: String = null

  val parent: BOMSchemaElement = null

  var root: BOMSchemaElement = _

  def createNode(bspace: BOMBinarySpace, parent: BOMContainer, index: Int): BOMNode =
    new BOMDocument(this, bspace)

  override val depth: Int = 0

  override def children: List[BOMSchemaElement] = root :: Nil

  override def add(child: BOMSchemaElement) = {
    if (root == null) {
      root = child
    } else {
      throw new BOMException("Cannot have more than one root in a document.")
    }
  }

  override def instance(parent: BOMContainer, index: Int): BOMNode =
    throw new UnsupportedOperationException

  def createDocument(bspace: BOMBinarySpace): BOMDocument =
    createNode(bspace, null, 0).asInstanceOf[BOMDocument]

  def size: BOMNode => Long = (n:BOMNode) => n.size

}
