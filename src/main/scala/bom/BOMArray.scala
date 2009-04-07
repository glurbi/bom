package bom

import java.util._
import org.w3c.dom._
import bom.dom._
import bom.schema._

/**
 * The <code>BOMArray</code> class represents a container node where children
 * are indexed.
 */
case class BOMArray(override val schema: BOMSchemaArray,
                    override val parent: BOMContainer,
                    override val index: Int,
                    val regular: Boolean)
  extends BOMContainer(schema, parent, index) {

  // TODO: lazyfy
  private var arrayLength = -1

  def length: Long = {
    if (arrayLength == -1) {
      arrayLength = schema.lengthFun(this).asInstanceOf[Int]
    }
    arrayLength;
  }

  override lazy val size: Long =
    if (schema.sizeFun != null)  {
      schema.sizeFun(this)
    } else if (regular) {
      val n = schema.children(0).instance(this, 0)
      length * n.size
    } else {
      var sz = 0L
      for (i <- 0 until length.toInt) {
        sz += (this/i).size
      }
      sz
    }

  /**
   * @return the array element at the specified index
   */
  def /(index: Int): BOMNode = index match {
        case -1 => parent
        case _ => schema.children(0).instance(this, index)
  }

  def /(name: String): BOMNode = null

  def asDomNode: Node = new BOMArrayAdapter(this)

  def iterator: Iterator[BOMNode] = new ArrayIterator

  class ArrayIterator extends Iterator[BOMNode] {
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
