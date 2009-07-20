package bom.schema

import bom._
import bom.bin._

import scala.collection.mutable.HashMap

case class SchemaSequence(
  override val name:String,
  override val parent: SchemaElement,
  override val depth: Int)
extends SchemaElement {

  private var elements: List[SchemaElement] = Nil
  var name2element = HashMap.empty[String, SchemaElement]
  var name2index = HashMap.empty[String, Int]

  override def add(child: SchemaElement) {
    elements = elements ::: List(child)
    if (child.name != null) {
      name2element += child.name -> child
      name2index += child.name -> (children.size - 1)
    }
  }

  override def createNode(parent: BOMContainer, index: Int): BOMNode =
    new BOMSequence(this, parent, index)

  def child(name: String): SchemaElement = name2element.getOrElse(name, null)

  def childIndex(name: String): Int = name2index(name)

  def children: List[SchemaElement] = elements

}
