package bom.schema

import java.util._
import bom._
import bom.bin._

object BOMSchemaElement {

  def create: BOMSchemaElement = throw new BOMException
  
}

abstract class BOMSchemaElement {

  var name: String = null
  var sizeExpression: String = null
  var parent: BOMSchemaElement = null
  var size: long = 0
  
  def depth: int = 1 + parent.depth
  
  def childrenCount: int
  
  def children: List[BOMSchemaElement]
  
  def createNode(parent: BOMContainer, index: int): BOMNode
  
  def appendChild(schemaElement: BOMSchemaElement)

  def sizeExpression(expr: String): Unit = {
    sizeExpression = expr;
    try {
        size = expr.toLong
    } catch {
      case _ =>
    }
  }

}
