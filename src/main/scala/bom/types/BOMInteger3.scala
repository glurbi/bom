package bom.types

import bom.bin._

class BOMInteger3 extends BOMType {

  def typeName: String = "integer-3"

  def typeSize(params: Any*): Int = 8 * 3

  def read(bspace: BOMBinarySpace, params: Any*): Any = {
    val b3: Byte = bspace.getByte
    val b2: Byte = bspace.getByte
    val b1: Byte = bspace.getByte
    if ((b3 & 128) == 0) {
      ((b3 & 127) << 16) + (b2 << 8) + (b1 << 0)
    } else {
      -(~((b3 << 16) + (b2 << 8) + (b1 << 0) + 1))
    }
  }

}
