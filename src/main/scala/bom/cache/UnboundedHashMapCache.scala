package bom.cache

import scala.collection.mutable._

import bom.BOM._

trait UnboundedHashMapCache extends NodeCache {

  private val cache = HashMap.empty[BOMIdentifier, BOMNode]

  override def add(node: BOMNode) {
    cache += node.identifier -> node
  }

  override def get(id: BOMIdentifier): Option[BOMNode] = cache.get(id)

}
