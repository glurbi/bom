package bom.types

import bom.bin._

class BOMInteger extends BOMType {

  def typeName: String = "integer"

  def typeSize(params: Any*): int = 4

  def read(bspace: BOMBinarySpace, params: Any*): Any = {
    (bspace.getByte.asInstanceOf[Long] << 24) +
    (bspace.getByte.asInstanceOf[Long] << 16) +
    (bspace.getByte.asInstanceOf[Long] << 8) +
    (bspace.getByte.asInstanceOf[Long] << 0)
  }

}

