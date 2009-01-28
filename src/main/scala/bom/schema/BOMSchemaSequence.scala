package bom.schema

import java.util.{HashMap => JHashMap}
import bom._
import bom.bin._

case class BOMSchemaSequence(override val name:String,
                             override val parent: BOMSchemaElement,
                             override val size: BOMNode => Long,
                             override val depth: Int)
  extends BOMSchemaElement {

  private var elements: List[BOMSchemaElement] = Nil
  val name2element = new JHashMap[String, BOMSchemaElement]
  val name2index = new JHashMap[String, Int]

  override def add(child: BOMSchemaElement) {
    elements = elements ::: List(child)
    name2element.put(child.name, child)
    name2index.put(child.name, children.size - 1);
  }

  override def instance(parent: BOMContainer, index: Int): BOMNode =
    new BOMSequence(this, parent, index)

  def child(name: String): BOMSchemaElement = name2element.get(name)

  def childIndex(name: String): Int = name2index.get(name)

  def children: List[BOMSchemaElement] = elements

}
