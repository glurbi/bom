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
                   var localsz: Long)
  extends BOMLeaf(schema, parent, index) {

  /**
   * @return the value of this blob in the binary space
   */
  def value: Array[Byte] = {
    binarySpace.position(position)
    val bytes = new Array[Byte](size.intValue)
    binarySpace.getBytes(bytes)
    bytes
  }

  override def size: long = localsz

  def asDomNode: Node = new BOMBlobAdapter(this)

  override def toString: String = name + " " + value.length + " bytes"

}
