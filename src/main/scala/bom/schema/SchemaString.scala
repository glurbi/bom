package bom.schema

import bom._
import bom.bin._

case class SchemaString(

  override val name: String,

  override val parent: SchemaElement,

  override val depth: Int)

  extends SchemaElement {

  var encoding: String = _
    
  def add(child: SchemaElement) = error("A string cannot have a child element.")

  override def children: List[SchemaElement] = null

  override def instance(parent: BOMContainer, index: Int): BOMNode =
    new BOMString(this, parent, index)

}
