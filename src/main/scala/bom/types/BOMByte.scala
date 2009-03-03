package bom.types

import bom.bin._

class BOMByte extends BOMType {

  def typeSize(params: Any*): Int = 8

  def read(bspace: BOMBinarySpace, params: Any*): Byte = bspace.getByte

}
