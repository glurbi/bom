package bom.types

import bom.bin._

class BOMUnsignedByte extends BOMType {

  def typeName: String = "unsigned-byte"

  def typeSize(params: Any*): Int = 8

  def read(bspace: BOMBinarySpace, params: Any*): Any = {
    (bspace.getByte & 255).asInstanceOf[Byte]
  }

}

