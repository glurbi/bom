package bom.schema

import bom._
import bom.bin._

case class BOMSchemaVirtual(override val name: String,
                            override val parent: BOMSchemaElement,
                            override val depth: Int)
  extends BOMSchemaElement {

  def size: BOMNode => Long = (n:BOMNode) => 0

  var xpath: String = _

  def add(child: BOMSchemaElement) = throw new BOMException

  override def children: List[BOMSchemaElement] = null

  override def instance(parent: BOMContainer, index: Int): BOMNode = {
    new BOMVirtual(this, parent, index, xpath)
  }

}
