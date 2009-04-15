package bom.schema

import bom._
import bom.bin._

case class BOMSchemaVirtual(

  override val name: String,

  override val parent: BOMSchemaElement,

  override val depth: Int)

  extends BOMSchemaElement {

  def size: BOMNode => Long = (n:BOMNode) => 0

  var valueFun: BOMNode => Any = _

  def add(child: BOMSchemaElement) = error("A virtual element cannot have a child.")

  override def children: List[BOMSchemaElement] = null

  override def instance(parent: BOMContainer, index: Int): BOMNode = {
    new BOMVirtual(this, parent, index)
  }

}
