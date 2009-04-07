package bom

import bom.schema._
/**
 * The <code>BOMComposite</code> interface represents a BOM node that can have zero or more children.
 * The different child nodes can be accessed by iterating over the containers elements.
 */
abstract case class BOMContainer(override val schema: BOMSchemaElement,
                                 override val parent: BOMContainer,
                                 override val index: Int)
  extends BOMNode(schema, parent, index) {

}
