package bom.types

import bom.bin._

class BOMInteger extends BOMType {

  def size: Int = 32

  def read(bspace: BOMBinarySpace): Int = {
    def b: Byte = bspace.getByte
    ((((b & 0xFF) << 24) |
      ((b & 0xFF) << 16) |
      ((b & 0xFF) <<  8) |
      ((b & 0xFF) <<  0)))
  }

}

