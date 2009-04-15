package bom.schema

import bom._
import bom.bin._

case class SchemaBlob(

  override val name: String,

  override val parent: SchemaElement,

  override val depth: Int)

  extends SchemaElement {

  def add(child: SchemaElement) = error("A blob cannot a have a child.")

  override def children: List[SchemaElement] = null

  override def instance(parent: BOMContainer, index: Int): BOMNode =
    new BOMBlob(this, parent, index)

}
