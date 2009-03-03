package bom.types

import bom.bin._

class BOMLong extends BOMType {

  def size: Int = 64

  def read(bspace: BOMBinarySpace): Long = {
    (bspace.getByte.asInstanceOf[Long] << 56) +
    (bspace.getByte.asInstanceOf[Long] << 48) +
    (bspace.getByte.asInstanceOf[Long] << 40) +
    (bspace.getByte.asInstanceOf[Long] << 32) +
    (bspace.getByte.asInstanceOf[Long] << 24) +
    (bspace.getByte.asInstanceOf[Long] << 16) +
    (bspace.getByte.asInstanceOf[Long] << 8) +
    (bspace.getByte.asInstanceOf[Long] << 0)
  }

}
