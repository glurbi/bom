package bom.types

import bom.bin._

class BOMInteger extends BOMType {

  def typeName: String = "integer"

  def typeSize(params: Any*): Int = 32

  def read(bspace: BOMBinarySpace, params: Any*): Int = {
    (bspace.getByte.asInstanceOf[Int] << 24) +
    (bspace.getByte.asInstanceOf[Int] << 16) +
    (bspace.getByte.asInstanceOf[Int] << 8) +
    (bspace.getByte.asInstanceOf[Int] << 0)
  }

}

