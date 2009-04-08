package bom

import java.util._
import org.w3c.dom._
import bom.BOM._
import bom.bin._
import bom.schema._

/**
 * The <code>BOMNode</code> class is the primary data type for the entire
 * Binary Object Model. It represents a single node in the document tree.
 */
abstract case class BOMNode(

  /**
   * @return the schema of this node
   */
  val schema: BOMSchemaElement,

  /**
   * @return the parent node of this node
   */
  val parent: BOMContainer,

  /**
   * @return the index of this node
   */
  val index: Int) {

  /**
   * @return the name of this node
   */
  def name: String = schema.name

  /**
   * @return the document node associated with this node
   */
  def document: BOMDocument = parent.document

  /**
   * @return the depth of the node in the binary structure
   */
  def depth: Int = schema.depth

  /**
   *  The size is calculated as the number of bits of all this node children.
   * 
   * @return the node size (number of bits)
   */
  lazy val size: Long = error("Not implemented!")
  // Ideally, I would like to define an abstract method:
  // def size: Long
  // instead of the lazy val, but the tests fail (at least with version 2.7.3)

  /**
   * @return an iterator over the children elements of this container
   */
  def iterator: Iterator[BOMNode]

  /**
   * @return the number of children of this node
   */
  def length: Long

  /**
   * @param index the index of the child or -1 if
   * @return the parent of this node or the child at the specified index
   */
  def / (index: Int): BOMNode

  /**
   * @return the child of this node with the specified name or <code>null<code>
   *         if it doesn't exist
   */
  def / (name: String): BOMNode

  /**
   * @return the value of this node
   */
  def value: Any = null

  /**
   * @return this node identifier
   */
  lazy val identifier: BOMIdentifier = index :: parent.identifier

  /**
   * @return the position (in bits) of this node in the binary space
   */
  lazy val position: Long =
    if (schema.positionFun != null)  {
      schema.positionFun(this)
    } else if (index == 0) {
      parent.position;
    } else if (parent.isInstanceOf[BOMSequence]) {
      val previousSibling = parent.schema.children(index - 1).instance(parent, index - 1)
      previousSibling.position + previousSibling.size
    } else if (parent.isInstanceOf[BOMArray] && parent.schema.asInstanceOf[BOMSchemaArray].regular) {
      parent.position + index * parent.schema.children(0).instance(parent, 0).size
    } else if (parent.isInstanceOf[BOMArray]) {
      (parent.asInstanceOf[BOMArray]/(index - 1)).position +
      (parent.asInstanceOf[BOMArray]/(index - 1)).size
    } else {
      error("unreachable statement?")
    }

  /**
   * @return the DOM node corresponding to this BOM node
   */
  // TODO: remove the dependency to bom.dom
  def asDomNode: Node

  /**
   * @return the binary space associated with this node
   */
  def binarySpace: BOMBinarySpace = document.binarySpace

}
