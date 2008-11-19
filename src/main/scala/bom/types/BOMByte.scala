package bom.types

import bom.bin._

class BOMByte extends BOMType {

  def typeName: String = "byte"

  def typeSize(params: Any*): int = 1

  def read(bspace: BOMBinarySpace, params: Any*): Any = bspace.getByte

}
