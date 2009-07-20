package bom

import java.util._

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

  lazy val length: Long = schema.lengthFun(this).asInstanceOf[Int]

  override lazy val size: Long =
    if (schema.sizeFun != null)  {
      schema.sizeFun(this)
    } else if (schema.regular) {
      val n = schema.children(0).instance(this, 0)
      length * n.size
    } else {
      var sz = 0L
      for (i <- 0 until length.toInt) {
        sz += (this/i).size
      }
      sz
    }

  def / (index: Int): BOMNode = index match {
        case -1 => parent
        case _ => schema.children(0).instance(this, index)
  }

  def / (name: String): BOMNode = null

  def iterator: Iterator[BOMNode] = new Iterator[BOMNode] {
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
