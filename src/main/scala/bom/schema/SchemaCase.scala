package bom.schema

import bom._
import bom.bin._

case class SchemaCase(

  override val parent: SchemaElement,

  override val depth: Int)

  extends SchemaElement {

  override val name: String = null

  private var element: SchemaElement = null
  
  var caseValue: Object = null

  def add(child: SchemaElement) = element = child

  override def createNode(parent: BOMContainer, index: Int): BOMNode = element.instance(parent, index)

  def children: List[SchemaElement] = element :: Nil

}
