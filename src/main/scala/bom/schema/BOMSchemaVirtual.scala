package bom.schema

import java.util._
import bom._
import bom.bin._

case class BOMSchemaVirtual(override val name: String,
                            override val parent: BOMSchemaElement)
  extends BOMSchemaElement {

  def sizeFun: BOMNode => Long = (n:BOMNode) => 0

  var xpath: String = _

  def appendChild(schema: BOMSchemaElement) = throw new BOMException

  def childrenCount: int = 0

  override def children: List[BOMSchemaElement] = null

  override def createNode(parent: BOMContainer, index: int): BOMNode = {
    new BOMVirtual(this, parent, index, xpath)
  }

}
