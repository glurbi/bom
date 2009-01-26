package bom.schema

import java.util._
import bom._
import bom.bin._

case class BOMSchemaBlob(override val name: String,
                         override val parent: BOMSchemaElement,
                         override val size: BOMNode => Long)
  extends BOMSchemaElement {

  def appendChild(schema: BOMSchemaElement) = throw new BOMException

  def childrenCount: int = 0

  override def children: List[BOMSchemaElement] = null

  override def createNode(parent: BOMContainer, index: int): BOMNode =
    new BOMBlob(this, parent, index, size)

}
