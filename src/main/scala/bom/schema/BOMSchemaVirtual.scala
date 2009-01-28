package bom.schema

import bom._
import bom.bin._

case class BOMSchemaVirtual(override val name: String,
                            override val parent: BOMSchemaElement)
  extends BOMSchemaElement {

  def size: BOMNode => Long = (n:BOMNode) => 0

  var xpath: String = _

  def add(child: BOMSchemaElement) = throw new BOMException

  def childrenCount: Int = 0

  override def children: List[BOMSchemaElement] = null

  override def createNode(parent: BOMContainer, index: Int): BOMNode = {
    new BOMVirtual(this, parent, index, xpath)
  }

}
