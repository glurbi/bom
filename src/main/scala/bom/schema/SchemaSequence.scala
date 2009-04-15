package bom.schema

import java.util.{HashMap => JHashMap}
import bom._
import bom.bin._

case class SchemaSequence(

  override val name:String,

  override val parent: SchemaElement,

  override val depth: Int)

  extends SchemaElement {

  private var elements: List[SchemaElement] = Nil
  val name2element = new JHashMap[String, SchemaElement]
  val name2index = new JHashMap[String, Int]

  override def add(child: SchemaElement) {
    elements = elements ::: List(child)
    name2element.put(child.name, child)
    name2index.put(child.name, children.size - 1);
  }

  override def instance(parent: BOMContainer, index: Int): BOMNode =
    new BOMSequence(this, parent, index)

  def child(name: String): SchemaElement = name2element.get(name)

  def childIndex(name: String): Int = name2index.get(name)

  def children: List[SchemaElement] = elements

}