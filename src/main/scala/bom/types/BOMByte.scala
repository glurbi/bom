package bom.types

import bom.bin._

class BOMByte extends BOMType {

  def size: Int = 8

  def read(bspace: BOMBinarySpace): Byte = bspace.getByte

}
