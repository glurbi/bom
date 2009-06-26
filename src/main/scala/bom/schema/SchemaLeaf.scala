package bom.schema

import java.util.{HashMap => JHashMap}

abstract case class SchemaLeaf() extends SchemaElement {

  private val mappings = new JHashMap[Object, Object]
  
  var defaultMapping: Object = null

  def hasMapping: Boolean = mappings.size() > 0

  def addMapping(value: Object, mappedValue: Object) {
    mappings.put(value, mappedValue)
  }

  def mappedValue(value: Object): Object = {
    val o = mappings.get(value.toString)
    if (o != null) o else defaultMapping
  }
  
}
