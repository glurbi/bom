package bom

/**
 * A set of utility methods.
 */
object BOM {

  def value(node: BOMNode) = node.value
  def position(node: BOMNode): Long = node.position
  def size(node: BOMNode): Long = node.size
  def length(node: BOMNode): Long = node.length

  type BOMIdentifier = List[Int]
}
