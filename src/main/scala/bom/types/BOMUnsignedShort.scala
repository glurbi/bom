package bom.types

import bom.bin._

class BOMUnsignedShort extends BOMType {

  def typeName: String = "unsigned-short"

  def typeSize(params: Any*): Int = 16

  def read(bspace: BOMBinarySpace, params: Any*): Int = {
    ((bspace.getByte.asInstanceOf[Int] & 255) << 8) +
    ((bspace.getByte.asInstanceOf[Int] & 255) << 0)
  }

}

