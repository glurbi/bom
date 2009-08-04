package bom

import bom.types._

/**
 * A set of utility methods.
 */
object BOM {

  def value(node: BOMNode) = node.value
  def position(node: BOMNode): Long = node.position
  def size(node: BOMNode): Long = node.size
  def length(node: BOMNode): Long = node.length

  def longValue(node: BOMNode): Long = node.value match {
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

  def doubleValue(node: BOMNode): Double = node.value match {
    case l: Long => l.toDouble
    case i: Int => i.toDouble
    case s: Short => s.toDouble
    case b: Byte => b.toDouble
    case f: Float => f.toDouble
    case d: Double => d
    case _ => error ("unexpected type")
  }

  def stringValue(node: BOMNode): String = node.value match {
    case s: String => s
    case l: Long => l.toString
    case i: Int => i.toString
    case s: Short => s.toString
    case b: Byte => b.toString
    case f: Float => f.toString
    case d: Double => d.toString
    case null => ""
    case _ => error ("unexpected type")
  }

  type BOMIdentifier = List[Int]

  //
  // aliases for some common types
  //

  def bom_byte = new BOMByte
  def bom_int = new BOMInteger
  def bom_int3 = new BOMInteger3
  def bom_long = new BOMLong

  def bom_uint = new BOMUnsignedInteger
  def bom_ushort = new BOMUnsignedShort
  def bom_ubyte = new BOMUnsignedByte

  def bom_float = new BOMFloat
  def bom_double = new BOMDouble

  def bom_bcd1 = new BOMBinaryCodedDecimal(1)
  def bom_bcd2 = new BOMBinaryCodedDecimal(2)
  def bom_bcd3 = new BOMBinaryCodedDecimal(3)
  def bom_bcd4 = new BOMBinaryCodedDecimal(4)
  def bom_bcd6 = new BOMBinaryCodedDecimal(6)
  def bom_bcd8 = new BOMBinaryCodedDecimal(8)


  def bom_boolean = new BOMBitInteger(1)

  def bom_bitint1 = new BOMBitInteger(1)
  def bom_bitint2 = new BOMBitInteger(2)
  def bom_bitint3 = new BOMBitInteger(3)
  def bom_bitint4 = new BOMBitInteger(4)
  def bom_bitint10 = new BOMBitInteger(10)

}
