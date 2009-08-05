package bom

import bom.schema._

/**
 * The <code>BOMArray</code> class represents a container node of the same
 * element type. The children can be lookep up by index.
 */
case class BOMArray(
  override val schema: SchemaArray,
  override val parent: BOMContainer,
  override val index: Int)
extends BOMContainer(schema, parent, index) {

  def length: Long = schema.lengthFun(this).asInstanceOf[Int]
  
  override def size: Long =
    if (schema.sizeFun != null)  {
      schema.sizeFun(this)
    } else if (schema.regular) {
      val n = schema.children(0).instance(this, 0)
      length * n.size
    } else {
      foldLeft(0L)(_ + _.size)
    }

  def / (index: Int): BOMNode = index match {
        case -1 => parent
        case _ => schema.children(0).instance(this, index)
  }

  def / (name: String): BOMNode = null

  def elements: Iterator[BOMNode] = new Iterator[BOMNode] {
    var index = -1
    def hasNext: Boolean = index < length - 1;
    def next: BOMNode = {
      if (!hasNext) {
        throw new NoSuchElementException();
      }
      index += 1
      BOMArray.this/index
    }
    def remove = throw new UnsupportedOperationException
  }

}
