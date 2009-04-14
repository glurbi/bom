package bom

import bom.schema._
import bom.types._
import bom.types._

/**
 * The <code>BOMVirtual</code> class defines a virtual node. It doesn't occupy
 * physical space in the binary space.
 */
case class BOMVirtual(
  override val schema: BOMSchemaVirtual,
  override val parent: BOMContainer,
  override val index: Int)
  extends BOMLeaf(schema, parent, index) {

  override def value: Any = schema.valueFun(this)

  override lazy val size: Long = 0

}
