package bom.schema

import java.util._
import bom._
import bom.types._
import bom.bin._

case class BOMSchemaNumber extends BOMSchemaElement {

  var numberType: BOMType = null
  val mappings = new HashMap[Object, Object]
  val masks = new HashMap[String, Long]
  var defaultMapping: Object = null

  def appendChild(schema: BOMSchemaElement) = throw new BOMException

  def childrenCount: int = 0

  override def children: List[BOMSchemaElement] = null

  def hasMapping: boolean = mappings.size() > 0

  def hasMasks: boolean = masks.size > 0

  def addMapping(value: Object, mappedValue: Object) =
    mappings.put(value, mappedValue)

  def addMask(name: String, value: long) = masks.put(name, value)
  
  def getMasks(n: long): Set[String] = {
    val result = new HashSet[String]
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
