package bom

import bom.dom._
import bom.schema._
import bom.types._
import org.w3c.dom._
import bom.types._

/**
 * The <code>BOMVirtual</code> interface defines a virtual node in the Binary
 * Object Model. It doesn't occupy physical space in the binary space.
 */
case class BOMVirtual(override val schema: BOMSchemaVirtual,
                      override val parent: BOMContainer,
                      override val index: Int,
                      val xpath: String)
  extends BOMLeaf(schema, parent, index) {


  /**
   * @return the value of this virtual node
   */
  def value: Any = document.queryNumber(this, xpath)

  override def size: Long = 0

  def asDomNode: Node = new BOMLeafAdapter(this)

  override def toString: String = name + " " + value

}
