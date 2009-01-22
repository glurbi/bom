package bom

import bom.schema._

abstract case class BOMLeaf(override val schema: BOMSchemaElement,
                            override val parent: BOMContainer,
                            override val index: Int)
  extends BOMNode(schema, parent, index) {

  /**
   * @return the value of this instance in the binary space
   */
  def value: Any

}
