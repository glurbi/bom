package bom

import bom.dom._
import bom.schema._
import bom.types._
import org.w3c.dom._
import bom.types._

/**
 * The <code>BOMNumber</code> class defines a leaf node that can is interpreted
 * as a number.
 */
case class BOMNumber(
  override val schema: BOMSchemaNumber,
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

  override lazy val size: Long = numberType.size

  def asDomNode: Node = new BOMLeafAdapter(this)

  override def toString: String = name + " " + value

}
