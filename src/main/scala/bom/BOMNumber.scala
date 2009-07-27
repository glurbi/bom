package bom

import bom.schema._
import bom.types._
import bom.types._

/**
 * The <code>BOMNumber</code> class defines a leaf node that can is interpreted
 * as a number.
 */
case class BOMNumber(
  override val schema: SchemaNumber,
  override val parent: BOMContainer,
  override val index: Int)
extends BOMLeaf(schema, parent, index) {

  /**
   * @return the data type of the instance
   */
  def numberType: BOMType = schema.numberType

  /**
   * @return the value of this number in the binary space
   */
  override def value: Number = {
    binarySpace.position(position)
    numberType.read(binarySpace).asInstanceOf[Number]
  }

  override def size: Long = numberType.size

}
