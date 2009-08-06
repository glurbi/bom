package bom.schema

import bom._
import bom.bin._

/**
 * The common contract for all schema elements.
 */
abstract class SchemaElement {

  //TODO: should use pattern matching
  var positionFun: BOMNode => Long =
    node => {
      if (node.index == 0) {
        node.parent.position;
      } else if (node.parent.isInstanceOf[BOMSequence]) {
        val previousSibling = node.parent.schema.children(node.index - 1).instance(node.parent, node.index - 1)
        previousSibling.position + previousSibling.size
      } else if (node.parent.isInstanceOf[BOMArray] && node.parent.schema.asInstanceOf[SchemaArray].regular) {
        node.parent.position + node.index * node.parent.schema.children(0).instance(node.parent, 0).size
      } else if (node.parent.isInstanceOf[BOMArray]) {
        (node.parent.asInstanceOf[BOMArray]/(node.index - 1)).position +
        (node.parent.asInstanceOf[BOMArray]/(node.index - 1)).size
      } else {
        error("unreachable statement?")
      }
    }

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
