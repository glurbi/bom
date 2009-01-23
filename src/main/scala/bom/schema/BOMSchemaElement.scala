package bom.schema

import java.util._
import bom._
import bom.bin._

trait BOMSchemaElement {

  /**
   * @return the name of this schema element
   */
  def name: String

  /**
   * @return the schema element parent of this schema element
   */
  def parent: BOMSchemaElement

  /**
   * @return the function that will calculate the size of the BOMNode
   */
  def sizeFun: BOMNode => Long

  /**
   * @return the depth of the schema element in the schema hierarchy
   */
  def depth: int = 1 + parent.depth

  /**
   * the schema element children of this schema element
   */
  def children: List[BOMSchemaElement]

  /**
   * TODO: move responsability in BOMContainer?
   */
  def createNode(parent: BOMContainer, index: Int): BOMNode

  /**
   * Add a schema element child of this schema element.
   */
  def appendChild(schemaElement: BOMSchemaElement)

}
