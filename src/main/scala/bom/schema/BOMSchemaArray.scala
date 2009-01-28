package bom.schema

import bom._
import bom.bin._

case class BOMSchemaArray(override val name: String,
                          override val parent: BOMSchemaElement,
                          override val size: BOMNode => Long,
                          override val depth: Int)
  extends BOMSchemaElement {

  private var element: BOMSchemaElement = null
  private var arrayLength: Int = -1
  
  private var lenExpr: String = null

  var regular: Boolean = false

  def add(child: BOMSchemaElement) =
    if (element == null) {
      element = child;
    } else {
      throw new BOMException
    }

  def arrayLengthExpression_=(expr: String): Unit = {
    lenExpr = expr
    try {
      arrayLength = lenExpr.toInt
    } catch {
      case _ =>
    }
  }

  def arrayLengthExpression: String = lenExpr

  def dynamic: Boolean = arrayLength == -1

  def childrenCount: Int = 1

  override def children: List[BOMSchemaElement] = element :: Nil

  def instance(parent: BOMContainer, index: Int): BOMNode = {
    val array = new BOMArray(this, parent, index, regular)
    if (dynamic)
      array.arrayLengthExpression = arrayLengthExpression
    else
      array.arrayLength = arrayLength
    array
  }

}
