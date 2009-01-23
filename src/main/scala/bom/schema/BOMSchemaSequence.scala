package bom.schema

import java.util._
import bom._
import bom.bin._

case class BOMSchemaSequence(override val name:String,
                             override val parent: BOMSchemaElement,
                             override val sizeFun: BOMNode => Long)
  extends BOMSchemaElement {

  override val children = new ArrayList[BOMSchemaElement]
  val name2element = new HashMap[String, BOMSchemaElement]
  val name2index = new HashMap[String, Int]

  override def appendChild(schema: BOMSchemaElement) {
    children.add(schema)
    name2element.put(schema.name, schema)
    name2index.put(schema.name, children.size - 1);
  }

  override def createNode(parent: BOMContainer, index: int): BOMNode =
    new BOMSequence(this, parent, index)

  def child(name: String): BOMSchemaElement = name2element.get(name)

  def childIndex(name: String): int = name2index.get(name)

}
