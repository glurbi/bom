package bom.schema

import java.util.{HashMap => JHashMap}
import java.util.{HashSet => JHashSet}
import java.util.{Set => JSet}
import bom._
import bom.types._
import bom.bin._

case class BOMSchemaNumber(override val name: String,
                           override val parent: BOMSchemaElement,
                           override val size: BOMNode => Long)
  extends BOMSchemaElement {

  var numberType: BOMType = null
  val mappings = new JHashMap[Object, Object]
  val masks = new JHashMap[String, Long]
  var defaultMapping: Object = null

  def appendChild(schema: BOMSchemaElement) = throw new BOMException

  def childrenCount: int = 0

  override def children: List[BOMSchemaElement] = null

  def hasMapping: boolean = mappings.size() > 0

  def hasMasks: boolean = masks.size > 0

  def addMapping(value: Object, mappedValue: Object) =
    mappings.put(value, mappedValue)

  def addMask(name: String, value: long) = masks.put(name, value)
  
  def getMasks(n: long): JSet[String] = {
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
    
  def mappedValue(value: Object): Object = {
        val o = mappings.get(value.toString)
        if (o != null) o else defaultMapping
    }
    
  def createNode(parent: BOMContainer, index: int): BOMNode =
    new BOMNumber(this, parent, index)

}
