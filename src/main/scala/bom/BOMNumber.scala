package bom

import bom.dom._
import bom.schema._
import bom.types._
import org.w3c.dom._
import bom.types._

/**
 * The <code>BOMNumber</code> interface defines a concrete node in the Binary Object Model, that
 * can be interpreted as a number. It occupies physical space in the binary space.
 */
case class BOMNumber(override val schema: BOMSchemaNumber,
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
