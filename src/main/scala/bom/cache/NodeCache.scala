package bom.cache

import bom.BOM._

trait NodeCache {
  def add(node: BOMNode)
  def get(id: BOMIdentifier): Option[BOMNode]
}
