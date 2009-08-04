package bom

import bom.BOM._
import bom.bin._
import bom.schema._

/**
 * The <code>BOMNode</code> class is the primary data type for the entire
 * Binary Object Model. It represents a single node in the document tree.
 */
trait BOMNode extends AnyRef with Iterable[BOMNode] {

  /**
   * @return the schema of this node
   */
  def schema: SchemaElement

  /**
   * @return the parent node of this node
   */
  def parent: BOMContainer

  /**
   * @return the index of this node
   */
  def index: Int

  /**
   * @return the name of this node
   */
  def name: String

  /**
   * @return the document node associated with this node
   */
  def document: BOMDocument

  /**
   * @return the depth of the node in the binary structure
   */
  def depth: Int

  /**
   *  The size is calculated as the number of bits of all this node children.
   * 
   * @return the node size (number of bits)
   */
  def size: Long

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
   * @param the name of the child node
   * @return the child of this node with the specified name or <code>null<code>
   *         if it doesn't exist
   */
  def / (name: String): BOMNode

  /**
   * @return the value of this node
   */
  def value: Any

  /**
   * @return this node identifier
   */
  def identifier: BOMIdentifier

  /**
   * @return the position (in bits) of this node in the binary space
   */
  def position: Long

  /**
   * @return the binary space associated with this node
   */
  def binarySpace: BinarySpace

}
