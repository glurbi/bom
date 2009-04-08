package bom.schema

import bom._
import bom.bin._

case class BOMSchemaBlob(
  override val name: String,
  override val parent: BOMSchemaElement,
  override val depth: Int)
  extends BOMSchemaElement {

  def add(child: BOMSchemaElement) = error("A blob cannot a have a child.")

  override def children: List[BOMSchemaElement] = null

  override def instance(parent: BOMContainer, index: Int): BOMNode =
    new BOMBlob(this, parent, index)

}
