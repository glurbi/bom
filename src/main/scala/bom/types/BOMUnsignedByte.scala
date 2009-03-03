package bom.types

import bom.bin._

class BOMUnsignedByte extends BOMType {

  def typeSize(params: Any*): Int = 8

  def read(bspace: BOMBinarySpace, params: Any*): Short = {
    (bspace.getByte & 255).asInstanceOf[Short]
  }

}

