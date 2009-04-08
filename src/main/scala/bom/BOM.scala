package bom

/**
 * A set of utility methods.
 */
object BOM {

  def value(node: BOMNode) = node.value
  def position(node: BOMNode): Long = node.position
  def size(node: BOMNode): Long = node.size
  def length(node: BOMNode): Long = node.length

  //implicit def intValue(node: BOMNode): Int = node.value.asInstanceOf[Int]

  implicit def longValue(node: BOMNode): Long = node.value match {
      case l: Long => l
      case i: Int => i
      case _ => error ("unexpected type")
  }

  type BOMIdentifier = List[Int]
}
