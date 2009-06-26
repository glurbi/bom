package bom.schema

import java.util.{HashMap => JHashMap}

abstract case class SchemaLeaf() extends SchemaElement {

  // using String makes sure we have a 'natural' behavior when comparing objects
  // of different classes that represent the same value (1 Integer and 1 Long for instance)
  private val mappings = new JHashMap[String, AnyRef]
  
  var defaultMapping: AnyRef = null

  def hasMapping: Boolean = mappings.size() > 0

  def addMapping(value: Any, mappedValue: Object) {
    mappings.put(value.toString, mappedValue)
  }

  def mappedValue(value: Any): AnyRef = {
    val o = mappings.get(value.toString)
    if (o != null) o else defaultMapping
  }
  
}
