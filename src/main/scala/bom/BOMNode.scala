package bom

import org.w3c.dom._
import bom.bin._
import bom.schema._

/**
 * The <code>BOMNode</code> class is the primary data type for the entire
 * Binary Object Model. It represents a single node in the document tree.
 */
abstract class BOMNode(val schema: BOMSchemaElement,
                       val parent: BOMContainer,
                       val index: Int) {

  // TODO: make it more functional
  val NO_POSITION = -1L
  val NO_SIZE = -1L

  protected var pos = NO_POSITION
  protected var sz = NO_SIZE
  
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
    if (pos == NO_POSITION) {
      if (index == 0) {
        pos = parent.position;
      } else if (parent.isInstanceOf[BOMSequence]) {
        val previousSibling = parent.schema.children(index - 1).
          instantiate(parent, index - 1)
        pos = previousSibling.position + previousSibling.size
      } else if (parent.isInstanceOf[BOMArray]) {
        if (parent.schema.asInstanceOf[BOMSchemaArray].regular) {
          pos = parent.position + index * parent.schema.children(0).instantiate(parent, 0).size;
        } else {
          pos = parent.asInstanceOf[BOMArray].child(index - 1).position +
            parent.asInstanceOf[BOMArray].child(index - 1).size
        }
      }
    }
    pos
  }

  /**
   * @return the DOM node corresponding to this BOM node
   */
  def asDomNode: Node

  /**
   * @return the binary space associated with this node
   */
  def binarySpace: BOMBinarySpace = document.binarySpace

  override def equals(that: Any): Boolean = that match {
      case other: BOMNode
        => this.index == other.index && this.parent.equals(other.parent)
      case _
        => false
    }

  override def hashCode: Int = index

  override def toString: String = name
  
}
