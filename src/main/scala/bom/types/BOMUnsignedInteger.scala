package bom.types

import bom.bin._

class BOMUnsignedInteger extends BOMType {

  def typeName: String = "unsigned-integer"

  def typeSize(params: Any*): Int = 32

  def read(bspace: BOMBinarySpace, params: Any*): Any = {
    ((bspace.getByte.asInstanceOf[Long] & 255) << 24) +
    ((bspace.getByte.asInstanceOf[Long] & 255) << 16) +
    ((bspace.getByte.asInstanceOf[Long] & 255) << 8) +
    ((bspace.getByte.asInstanceOf[Long] & 255) << 0)
  }
  
}
