package bom.schema

import bom._
import bom.bin._

case class SchemaArray(
  
  override val name: String,

  override val parent: SchemaElement,

  override val depth: Int)

  extends SchemaElement {

  private var element: SchemaElement = null
  
  var lengthFun: BOMNode => Long = _

  var regular: Boolean = false

  def add(child: SchemaElement) =
    if (element == null) {
      element = child;
    } else {
      error("Only one child element can be added to an array.")
    }

  override def children: List[SchemaElement] = element :: Nil

  override def createNode(parent: BOMContainer, index: Int): BOMNode = new BOMArray(this, parent, index)

}
