package bom

import bom.dom._
import bom.schema._
import bom.types._
import org.w3c.dom._
import bom.types._

/**
 * The <code>BOMBlob</code> class defines a leaf node that is interpreted as an
 * "opaque" byte array.
 */
case class BOMBlob(
  override val schema: BOMSchemaBlob,
  override val parent: BOMContainer,
  override val index: Int,
  //TODO: move into schema
  val sizeFun: BOMNode => Long)
  extends BOMLeaf(schema, parent, index) {

  //TODO: lazify
  private var bytes: Array[Byte] = _

  override def value: Array[Byte] = {
    if (bytes == null) {
      binarySpace.position(position)
      bytes = new Array[Byte](byteCount.intValue)
      binarySpace.getBytes(bytes)
    }
    bytes
  }

  override lazy val size: Long = sizeFun(this)

  def asDomNode: Node = new BOMLeafAdapter(this)

  def byteCount: Long = size / 8

}
