package bom.schema

import java.util._
import bom._
import bom.bin._

case class BOMSchema extends BOMSchemaElement {

  private val types = new HashMap[String, BOMSchemaDefinition]

  def addDefinition(schemaType: BOMSchemaDefinition) =
    types.put(schemaType.name, schemaType)

  def definition(name: String): BOMSchemaDefinition = types.get(name)

  def definitions: Collection[BOMSchemaDefinition] = types.values

  def appendChild(schemaElement: BOMSchemaElement) {
    if (schemaElement.isInstanceOf[BOMSchemaDefinition]) {
      addDefinition(schemaElement.asInstanceOf[BOMSchemaDefinition]);
    } else {
      throw new BOMException("Only a SchemaType object is accepted for appending")
    }
  }

  def createNode(parent: BOMContainer, index: int): BOMNode
    = throw new BOMException

  def children: List[BOMSchemaElement] = throw new BOMException

  def childrenCount: int = throw new BOMException

}