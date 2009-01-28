package bom.schema

import java.util.{HashMap => JHashMap}
import bom._
import bom.bin._

case class BOMSchemaSequence(override val name:String,
                             override val parent: BOMSchemaElement,
                             override val size: BOMNode => Long)
  extends BOMSchemaElement {

  private var seq: List[BOMSchemaElement] = Nil
  val name2element = new JHashMap[String, BOMSchemaElement]
  val name2index = new JHashMap[String, Int]

  override def appendChild(schema: BOMSchemaElement) {
    seq = seq ::: List(schema)
    name2element.put(schema.name, schema)
    name2index.put(schema.name, children.size - 1);
  }

  override def createNode(parent: BOMContainer, index: int): BOMNode =
    new BOMSequence(this, parent, index)

  def child(name: String): BOMSchemaElement = name2element.get(name)

  def childIndex(name: String): int = name2index.get(name)

  def children: List[BOMSchemaElement] = seq

}
