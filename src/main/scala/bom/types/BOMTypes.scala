package bom.types

trait BOMTypes {

  def bom_byte = new BOMByte
  def bom_int = new BOMInteger
  def bom_long = new BOMLong

  def bom_uint = new BOMUnsignedInteger
  def bom_ushort = new BOMUnsignedShort
  def bom_ubyte = new BOMUnsignedByte

  def bom_float = new BOMFloat
  def bom_double = new BOMDouble

}
