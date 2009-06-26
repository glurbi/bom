package bom.schema

import bom._
import bom.bin._

case class SchemaVirtual(
  override val name: String,
  override val parent: SchemaElement,
  override val depth: Int)
extends SchemaElement {

  def size: BOMNode => Long = (n:BOMNode) => 0

  var valueFun: BOMNode => Any = _

  def add(child: SchemaElement) = error("A virtual element cannot have a child.")

  override def children: List[SchemaElement] = null

  override def createNode(parent: BOMContainer, index: Int): BOMNode = {
    new BOMVirtual(this, parent, index)
  }

}
