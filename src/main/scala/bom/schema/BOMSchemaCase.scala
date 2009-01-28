package bom.schema

import bom._
import bom.bin._

case class BOMSchemaCase(override val parent: BOMSchemaElement,
                         override val size: BOMNode => Long)
  extends BOMSchemaElement {

  override val name: String = null

  private var element: BOMSchemaElement = null
  
  var caseValue: Object = null

  def add(child: BOMSchemaElement) = element = child

  def instance(parent: BOMContainer, index: Int): BOMNode =
    element.instance(parent, index)

  def children: List[BOMSchemaElement] = element :: Nil

  def childrenCount: Int = 1

  override def depth: Int = parent.depth

}
