package bom

import bom.schema._

/**
 * The <code>BOMContainer</code> class represents a BOM node that can have zero
 * or more children. The different child nodes can be accessed by iterating over
 * the containers elements, or lookep up by index.
 */
abstract case class BOMContainer(
  override val schema: SchemaElement,
  override val parent: BOMContainer,
  override val index: Int)
extends BOMNode(schema, parent, index) {}
