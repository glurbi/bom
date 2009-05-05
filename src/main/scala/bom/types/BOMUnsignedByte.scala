package bom.types

import bom.bin._

class BOMUnsignedByte extends BOMType {

  def size: Int = 8

  def read(bspace: BinarySpace): Short = {
    (bspace.getByte & 255).asInstanceOf[Short]
  }

}

