package bom.types

import bom.bin._

class BOMUnsignedShort extends BOMType {

  def size: Int = 16

  def read(bspace: BinarySpace): Int = {
    ((bspace.getByte.asInstanceOf[Int] & 255) << 8) +
    ((bspace.getByte.asInstanceOf[Int] & 255) << 0)
  }

}

