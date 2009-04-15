package bom.types

import bom.bin._

class BOMUnsignedInteger extends BOMType {

  def size: Int = 32

  def read(bspace: BinarySpace): Long = {
    ((bspace.getByte.asInstanceOf[Long] & 255) << 24) +
    ((bspace.getByte.asInstanceOf[Long] & 255) << 16) +
    ((bspace.getByte.asInstanceOf[Long] & 255) << 8) +
    ((bspace.getByte.asInstanceOf[Long] & 255) << 0)
  }
  
}
