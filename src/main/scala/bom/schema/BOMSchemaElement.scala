package bom.schema

import bom._
import bom.bin._

/**
 * The common contract for all BOM schema elements.
 */
abstract case class BOMSchemaElement() {

  var positionFun: BOMNode => Long = _

  var sizeFun: BOMNode => Long = _

  /**
   * @return the name of this schema element
   */
  def name: String

  /**
   * @return the schema element parent of this schema element
   */
  def parent: BOMSchemaElement

  /**
   * @return the depth of the schema element in the schema hierarchy
   */
  def depth: Int

  /**
   * @return the sequence of schema elements children of this schema element
   */
  def children: List[BOMSchemaElement]

  /**
   * Create an BOMNode instance corresponding to this schema element.
   */
  def instance(parent: BOMContainer, index: Int): BOMNode

  /**
   * Add a schema element child of this schema element.
   */
  def add(child: BOMSchemaElement)

}
