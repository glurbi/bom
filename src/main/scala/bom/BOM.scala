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
    case i: Int => i.toLong
    case s: Short => s.toLong
    case b: Byte => b.toLong
    case f: Float => f.toLong
    case d: Double => d.toLong
    case _ => error ("unexpected type")
  }

  def intValue(node: BOMNode): Long = node.value match {
    case i: Int => i
    case s: Short => s.toInt
    case b: Byte => b.toInt
    case f: Float => f.toInt
    case d: Double => d.toInt
    case _ => error ("unexpected type")
  }

  implicit def doubleValue(node: BOMNode): Double = node.value match {
    case l: Long => l.toDouble
    case i: Int => i.toDouble
    case s: Short => s.toDouble
    case b: Byte => b.toDouble
    case f: Float => f.toDouble
    case d: Double => d
    case _ => error ("unexpected type")
  }

  implicit def stringValue(node: BOMNode): String = node.value match {
    case s: String => s
    case l: Long => l.toString
    case i: Int => i.toString
    case s: Short => s.toString
    case b: Byte => b.toString
    case f: Float => f.toString
    case d: Double => d.toString
    case _ => error ("unexpected type")
  }

  type BOMIdentifier = List[Int]
}
