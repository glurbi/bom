package bom.schema

import java.util._
import bom._
import bom.bin._

case class BOMSchemaString extends BOMSchemaElement {

  var encoding: String = null
    
  def appendChild(schema: BOMSchemaElement) = throw new BOMException

  def childrenCount: int = 0

  override def children: List[BOMSchemaElement] = null

  override def createNode(parent: BOMContainer, index: int): BOMNode =
    new BOMString(this, parent, index)

}
