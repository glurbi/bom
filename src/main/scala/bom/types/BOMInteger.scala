package bom.types

import bom.bin._

class BOMInteger extends BOMType {

  def typeSize(params: Any*): Int = 32

  def read(bspace: BOMBinarySpace, params: Any*): Int = {
    def b: Byte = bspace.getByte
    ((((b & 0xFF) << 24) |
      ((b & 0xFF) << 16) |
      ((b & 0xFF) <<  8) |
      ((b & 0xFF) <<  0)))
  }

}

