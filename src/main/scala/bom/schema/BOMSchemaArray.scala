package bom.schema

import java.util._
import bom._
import bom.bin._

case class BOMSchemaArray extends BOMSchemaElement {

  private var child: BOMSchemaElement = null
  private var arrayLength: int = -1
  
  private var lenExpr: String = null

  var regular: boolean = false

  def appendChild(schemaElement: BOMSchemaElement) =
    if (child == null) {
      child = schemaElement;
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

  def dynamic: boolean = arrayLength == -1

  def childrenCount: int =
    if (child == null)
      0
    else
      1

  override def children: List[BOMSchemaElement] = {
    val children = new ArrayList[BOMSchemaElement]
    children.add(child);
    children
  }

  def createNode(parent: BOMContainer, index: int): BOMNode = {
    val array = new BOMArray(this, parent, index, regular)
    if (dynamic)
      array.arrayLengthExpression = arrayLengthExpression
    else
      array.arrayLength = arrayLength
    array
  }

}
