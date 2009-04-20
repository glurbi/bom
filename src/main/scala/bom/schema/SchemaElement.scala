package bom.schema

import bom._
import bom.bin._

/**
 * The common contract for all schema elements.
 */
abstract case class SchemaElement() {

  var positionFun: BOMNode => Long = _

  var sizeFun: BOMNode => Long = _

  /**
   * @return the name of this schema element
   */
  def name: String

  /**
   * @return the schema element parent of this schema element
   */
  def parent: SchemaElement

  /**
   * @return the depth of the schema element in the schema hierarchy
   */
  def depth: Int

  /**
   * @return the sequence of schema elements children of this schema element
   */
  def children: List[SchemaElement]

  /**
   * Retrieve a BOMNode instance corresponding to this schema element.
   */
  sealed def instance(parent: BOMContainer, index: Int): BOMNode = {
    parent.document.get(index :: parent.identifier) match {
      case Some(n: BOMNode) => n
      case None => {
        val n = createNode(parent, index)
        n.document.add(n)
        n
      }
    }
  }

  /**
   * Add a schema element child of this schema element.
   */
  def add(child: SchemaElement)

  /**
   * Create a BOMNode instance corresponding to this schema element.
   */
  protected def createNode(parent: BOMContainer, index: Int): BOMNode =
    error("not implemented!")

}
