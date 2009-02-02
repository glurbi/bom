package bom.types

import bom.bin._

class BOMByte extends BOMType {

  def typeName: String = "byte"

  def typeSize(params: Any*): Int = 8

  def read(bspace: BOMBinarySpace, params: Any*): Byte = bspace.getByte

}
