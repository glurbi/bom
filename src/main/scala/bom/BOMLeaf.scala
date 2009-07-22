package bom

import bom.schema._

/**
 * The <code>BOMLeaf</code> class represents a BOM node that has zero children.
 * This node contains a value instead.
 */
abstract case class BOMLeaf(
  override val schema: SchemaElement,
  override val parent: BOMContainer,
  override val index: Int)
extends BOMBaseNode(schema, parent, index) {

  def length: Long = 0

  def / (index: Int): BOMNode = index match {
    case -1 => parent
    case _ => error("A leaf node doesn't have children.")
  }

  def / (name: String): BOMNode = null
  
  def elements: Iterator[BOMNode] = new Iterator[BOMNode]() {
    def hasNext: Boolean = false
    def next: BOMNode = throw new NoSuchElementException
    def remove = throw new UnsupportedOperationException
  }

}
