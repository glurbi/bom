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
                    val regular: boolean)
  extends BOMContainer(schema, parent, index) {

  val NO_LENGTH = -1

  var arrayLength = NO_LENGTH
  var arrayLengthExpression: String = null

  /**
   * @return the number of element in this array
   */
  def childrenCount: Int = {
    if (arrayLength == NO_LENGTH) {
      val xpath = schema.arrayLengthExpression
      val number = document.queryNumber(this, xpath);
      arrayLength = number.intValue
    }
    arrayLength;
  }

  override def size: long = {
    if (regular) {
      val n = schema.children.get(0).createNode(this, 0)
      childrenCount * n.size
    } else {
      if (sz == NO_SIZE) {
        sz = 0
        for (i <- 0 until childrenCount) {
          sz += child(i).size
        }
      }
      sz
    }
  }

  /**
   * @return the array element at the specified index
   */
  def child(index: Int): BOMNode = {
    if (regular) {
      if (index >= childrenCount) {
        throw new ArrayIndexOutOfBoundsException(index);
      }
      schema.children.get(0).createNode(this, index)
    } else {
      schema.children.get(0).createNode(this, index)
    }
  }

  def asDomNode: Node = new BOMArrayAdapter(this)

  def iterator: Iterator[BOMNode] = new ArrayIterator

  class ArrayIterator extends Iterator[BOMNode] {
    var index = -1
    def hasNext: boolean = index < childrenCount - 1;
    def next: BOMNode = {
      if (!hasNext) {
        throw new NoSuchElementException();
      }
      index += 1
      child(index)
    }
    def remove = throw new UnsupportedOperationException
  }

}