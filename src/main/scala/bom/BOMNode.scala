package bom

import org.w3c.dom._
import bom.bin._
import bom.schema._

/**
 * The <code>BOMNode</code> class is the primary data type for the entire
 * Binary Object Model. It represents a single node in the document tree.
 */
// TODO: make it a case class
abstract case class BOMNode(val schema: BOMSchemaElement,
                            val parent: BOMContainer,
                            val index: Int) {

  protected var pos: Long = -1
  protected var sz: Long = -1
  
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
  def size: Long = sz

  /**
   * @return the number of children of this node
   */
  def childCount = 0

  /**
   * @return the child of this node at the specified index or <code>null<code>
   *         if it doesn't exist
   */
  // TODO: use the / operator
  // TODO: add support for -1 (parent)
  def apply(index: Int): BOMNode

  /**
   * @return the child of this node with the specified name or <code>null<code>
   *         if it doesn't exist
   */
  // TODO: use the / operator
  def apply(name: String): BOMNode = null

  // TODO: add a "children" method

  /**
   * @return the value of this node
   */
  def value: Any = null

  /**
   * @return this node identifier
   */
  def identifier: BOMIdentifier = {
    val ia = new Array[Int](depth)
    populateId(ia);
    new BOMIdentifier(ia);
  }

  private def populateId(id: Array[Int]): Unit = {
    if (!isInstanceOf[BOMDocument]) {
      id(depth - 1) = index
      parent.asInstanceOf[BOMNode].populateId(id);
    }
  }
  
  /**
   * @return the position (in bits) of this node in the binary space
   */
  def position: Long = {
    if (pos == -1) {
      if (schema.positionFun != null)  {
        pos = schema.positionFun(this)
      } else if (index == 0) {
        pos = parent.position;
      } else if (parent.isInstanceOf[BOMSequence]) {
        val previousSibling = parent.schema.children(index - 1).
          instance(parent, index - 1)
        pos = previousSibling.position + previousSibling.size
      } else if (parent.isInstanceOf[BOMArray]) {
        if (parent.schema.asInstanceOf[BOMSchemaArray].regular) {
          pos = parent.position + index * parent.schema.children(0).instance(parent, 0).size;
        } else {
          pos = parent.asInstanceOf[BOMArray](index - 1).position +
            parent.asInstanceOf[BOMArray](index - 1).size
        }
      }
    }
    pos
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

  override def toString: String = name
  
}
