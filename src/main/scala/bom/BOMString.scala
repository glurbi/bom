package bom

import java.nio._

import bom.schema._

/**
 * The <code>BOMString</code> class defines a node that is interpreted as a
 * character string.
 */
case class BOMString(
  override val schema: SchemaString,
  override val parent: BOMContainer,
  override val index: Int)
extends BOMLeaf(schema, parent, index) {

  /**
   * @return the value of this character string in the binary space
   */
  override def value: String = {
    binarySpace.position(position.intValue)
    val ba = new Array[byte](size.intValue / 8)
    binarySpace.getBytes(ba)
    schema.charset.decode(ByteBuffer.wrap(ba)).toString
  }
 
  override def size: Long = schema.sizeFun(this)

}
