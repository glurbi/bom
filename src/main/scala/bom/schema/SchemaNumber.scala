package bom.schema

import scala.collection.mutable.Set
import scala.collection.mutable.HashSet
import scala.collection.mutable.HashMap

import bom._
import bom.types._
import bom.bin._

case class SchemaNumber(
  override val name: String,
  override val parent: SchemaElement,
  override val depth: Int)
extends SchemaLeaf {

  var numberType: BOMType = null
  val masks = HashMap.empty[String, Long]

  def add(child: SchemaElement) = error("A number cannot have a child element.")

  override def children: List[SchemaElement] = Nil

  def hasMasks: Boolean = masks.size > 0

  def addMask(name: String, value: Long) = masks.put(name, value)
  
  def getMasks(n: Long): Set[String] = {
    val result = new HashSet[String]
    masks.foreach { mask =>
      if ((n.longValue & mask._2) > 0) {
        result += mask._1
      }
    }
    result
  }
    
  override def createNode(parent: BOMContainer, index: Int): BOMNode =
    new BOMNumber(this, parent, index)

}
