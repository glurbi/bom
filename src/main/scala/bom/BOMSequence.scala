package bom

import bom.schema._

/**
 * The <code>BOMSequence</code> class represents a container node of different
 * element type. The children can be looked up by name or index.
 */
case class BOMSequence(
  override val schema: SchemaSequence,
  override val parent: BOMContainer,
  override val index: Int)
extends BOMContainer(schema, parent, index) {

  def elements: Iterator[BOMNode] = new SequenceIterator

  override def size: Long =
    if (schema.sizeFun != null)  {
      schema.sizeFun(this)
    } else {
      foldLeft(0L)(_ + _.size)
    }
  
  override def / (name: String): BOMNode = {
    val childSchema = schema.child(name)
    if (childSchema == null) {
      BOMNil
    } else {
      val index = schema.childIndex(name)
      childSchema.instance(this, index)
    }
  }

  def length: Long = schema.children.size

  def / (index: Int): BOMNode = index match {
    case -1 => parent
    case _ => schema.children(index).instance(this, index)
  }

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
        curr = schema.children(0).instance(BOMSequence.this, 0)
      } else {
        curr = schema.children(curr.index + 1).instance(BOMSequence.this, curr.index + 1)
      }
      curr
    }
    def remove = throw new UnsupportedOperationException
  }

}
