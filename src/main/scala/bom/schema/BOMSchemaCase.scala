package bom.schema

import bom._
import bom.bin._

case class BOMSchemaCase(override val parent: BOMSchemaElement,
                         override val size: BOMNode => Long)
  extends BOMSchemaElement {

  override val name: String = null

  private var child: BOMSchemaElement = null
  
  var caseValue: Object = null

  def appendChild(schemaElement: BOMSchemaElement) = child = schemaElement

  def createNode(parent: BOMContainer, index: Int): BOMNode =
    child.createNode(parent, index)

  def children: List[BOMSchemaElement] = child :: Nil

  def childrenCount: Int = 1

  override def depth: Int = parent.depth

}
