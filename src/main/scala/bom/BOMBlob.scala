package bom

import bom.schema._
import bom.types._
import bom.types._

/**
 * The <code>BOMBlob</code> class defines a leaf node that is interpreted as an
 * "opaque" byte array.
 */
case class BOMBlob(
  override val schema: SchemaBlob,
  override val parent: BOMContainer,
  override val index: Int)
  extends BOMLeaf(schema, parent, index) {

  override lazy val value: Array[Byte] = {
    binarySpace.position(position)
    val bytes = new Array[Byte](byteCount.intValue)
    binarySpace.getBytes(bytes)
    bytes
  }

  override lazy val size: Long = schema.sizeFun(this)

  def byteCount: Long = size / 8

}
