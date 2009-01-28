package bom.schema

import bom._
import bom.bin._

case class BOMSchemaBlob(override val name: String,
                         override val parent: BOMSchemaElement,
                         override val size: BOMNode => Long)
  extends BOMSchemaElement {

  def add(child: BOMSchemaElement) = throw new BOMException

  def childrenCount: Int = 0

  override def children: List[BOMSchemaElement] = null

  override def instantiate(parent: BOMContainer, index: Int): BOMNode =
    new BOMBlob(this, parent, index, size)

}
