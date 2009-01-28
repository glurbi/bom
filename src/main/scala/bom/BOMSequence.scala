package bom

import java.util._
import bom._
import bom.dom._
import bom.schema._
import org.w3c.dom._

/**
 * The <code>BOMSequence</code> interface represents a container node where children can be
 * accessed by name.
 */
case class BOMSequence(override val schema: BOMSchemaSequence,
                       override val parent: BOMContainer,
                       override val index: Int)
  extends BOMContainer(schema, parent, index) {

  /**
   * @return the child with the name given in parameter
   */
  def child(name: String): BOMNode = {
    val childSchema = schema.child(name)
    if (childSchema == null) {
      null
    }
    val index = schema.childIndex(name)
    schema.instantiate(this, index)
  }

  def asDomNode: Node = new BOMSequenceAdapter(this)

  def iterator: Iterator[BOMNode] = new SequenceIterator

  override def size: Long = {
    if (sz == NO_SIZE) {
      sz = 0
      val it = iterator
      while (it.hasNext) {
        sz += it.next.size
      }
    }
    sz
  }

  override def toString: String = name

  class SequenceIterator extends Iterator[BOMNode] {
    var curr: BOMNode = null
    def hasNext: Boolean = {
      if (curr == null) {
        BOMSequence.this.schema.children.size != 0
      } else {
        curr.index < schema.children.size - 1
      }
    }
    def next: BOMNode = {
      if (!hasNext) {
        throw new NoSuchElementException
      }
      if (curr == null) {
        curr = schema.children(0).instantiate(BOMSequence.this, 0)
      } else {
        curr = schema.children(curr.index + 1).instantiate(BOMSequence.this, curr.index + 1)
      }
      curr
    }
    def remove = throw new UnsupportedOperationException
  }

}
