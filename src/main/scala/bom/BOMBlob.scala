package bom

import bom.dom._
import bom.schema._
import bom.types._
import org.w3c.dom._
import bom.types._

/**
 * The <code>BOMBlob</code> interface defines a concrete node in the Binary
 * Object Model, that is interpreted as an "opaque" byte array. It occupies
 * physical space in the binary space.
 */
case class BOMBlob(override val schema: BOMSchemaBlob,
                   override val parent: BOMContainer,
                   override val index: Int,
                   val sizeFun: BOMNode => Long)
  extends BOMLeaf(schema, parent, index) {

  private var bytes: Array[Byte] = _

  /**
   * @return the value of this blob in the binary space
   */
  def value: Array[Byte] = {
    if (bytes == null) {
      binarySpace.position(position)
      bytes = new Array[Byte](byteCount.intValue)
      binarySpace.getBytes(bytes)
    }
    bytes
  }

  override def size: Long = sizeFun(this)

  def asDomNode: Node = new BOMLeafAdapter(this)

  override def toString: String = name + " " + value.length + " bytes"

  def byteCount: Long = size / 8

}
