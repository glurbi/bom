package bom.schema

import java.util.{HashMap => JHashMap}
import java.util.{HashSet => JHashSet}
import java.util.{Set => JSet}
import bom._
import bom.types._
import bom.bin._

case class SchemaNumber(

  override val name: String,

  override val parent: SchemaElement,

  override val depth: Int)

  extends SchemaLeaf {

  var numberType: BOMType = null
  val masks = new JHashMap[String, Long]

  def add(child: SchemaElement) = error("A number cannot have a child element.")

  override def children: List[SchemaElement] = Nil

  def hasMasks: Boolean = masks.size > 0

  def addMask(name: String, value: Long) = masks.put(name, value)
  
  def getMasks(n: Long): JSet[String] = {
    val result = new JHashSet[String]
    val it = masks.entrySet.iterator
    while (it.hasNext) {
      val mask = it.next
      if ((n.longValue & mask.getValue.longValue) > 0) {
        result.add(mask.getKey)
      }
    }
    result
  }
    
  override def createNode(parent: BOMContainer, index: Int): BOMNode =
    new BOMNumber(this, parent, index)

}
