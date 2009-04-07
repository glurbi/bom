package bom.schema

import bom._
import bom.bin._

case class BOMSchemaString(override val name: String,
                           override val parent: BOMSchemaElement,
                           override val depth: Int)
  extends BOMSchemaElement {

  var encoding: String = _
    
  def add(child: BOMSchemaElement) = error("A string cannot have a child element.")

  override def children: List[BOMSchemaElement] = null

  override def instance(parent: BOMContainer, index: Int): BOMNode =
    new BOMString(this, parent, index)

}
