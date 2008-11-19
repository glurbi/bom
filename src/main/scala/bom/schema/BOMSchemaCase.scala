package bom.schema

import java.util._
import bom._
import bom.bin._

case class BOMSchemaCase extends BOMSchemaElement {

  private var child: BOMSchemaElement = null
  
  var caseValue: Object = null

  def appendChild(schemaElement: BOMSchemaElement) = child = schemaElement

  def createNode(parent: BOMContainer, index: int): BOMNode =
    child.createNode(parent, index)

  def children: List[BOMSchemaElement] = {
    val result = new ArrayList[BOMSchemaElement](1)
    result.add(child);
    result
  }

  def childrenCount: int = 1

  override def depth: int = parent.depth

}
