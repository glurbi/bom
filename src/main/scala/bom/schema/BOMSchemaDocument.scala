package bom.schema

import bom.bin._
import bom._

case class BOMSchemaDocument(
  
  override val name:String)

  extends BOMSchemaSequence(name, null, 0) {

  def createNode(bspace: BOMBinarySpace, parent: BOMContainer, index: Int): BOMNode =
    new BOMDocument(this, bspace)

  override val depth: Int = 0

  override def instance(parent: BOMContainer, index: Int): BOMNode =
    throw new UnsupportedOperationException

  def createDocument(bspace: BOMBinarySpace): BOMDocument =
    createNode(bspace, null, 0).asInstanceOf[BOMDocument]

  def size: BOMNode => Long = (n:BOMNode) => n.size

}
