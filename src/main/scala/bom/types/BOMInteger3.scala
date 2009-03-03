package bom.types

import bom.bin._

class BOMInteger3 extends BOMType {

  def size: Int = 8 * 3

  def read(bspace: BOMBinarySpace): Int = {
    def b: Byte = bspace.getByte
    val raw = ((((b & 0xFF) << 16) |
                ((b & 0xFF) <<  8) |
                ((b & 0xFF) <<  0)))
    if ((raw & 0x800000) > 0) {
      -((~raw & 0xFFFFFF) + 1)
    } else {
      raw
    }
  }

}
