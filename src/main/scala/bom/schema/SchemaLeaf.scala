package bom.schema

abstract case class SchemaLeaf() extends SchemaElement {

  // using String makes sure we have a 'natural' behavior when comparing objects
  // of different classes that represent the same value (Integer and Long for instance)
  private val mappings = scala.collection.mutable.Map.empty[String, AnyRef]

  var defaultMapping: AnyRef = null

  def hasMapping: Boolean = mappings.size > 0

  def addMapping(value: Any, mappedValue: Object) {
    mappings += value.toString -> mappedValue
  }

  def mappedValue(value: Any): AnyRef =
    mappings.getOrElse(value.toString, defaultMapping)
  
}
