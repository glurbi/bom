package bom.types

trait BOMTypes {

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
  
}
