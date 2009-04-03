package bom.schema

import bom._
import bom.bin._

case class BOMSchemaArray(override val name: String,
                          override val parent: BOMSchemaElement,
                          override val depth: Int)
  extends BOMSchemaElement {

  private var element: BOMSchemaElement = null
  
  var lengthFun: BOMNode => Long = _

  var regular: Boolean = false

  def add(child: BOMSchemaElement) =
    if (element == null) {
      element = child;
    } else {
      throw new BOMException
    }

  override def children: List[BOMSchemaElement] = element :: Nil

  def instance(parent: BOMContainer, index: Int): BOMNode = {
    val array = new BOMArray(this, parent, index, regular)
    array
  }

}
