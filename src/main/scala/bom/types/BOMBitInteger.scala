package bom.types;

import bom.bin._

class BOMBitInteger(bitCount: Int) extends BOMType {
  
  def size: Int = bitCount
  
  def read(bspace: BinarySpace): Long = {
    bspace.getBits(bitCount)
  }

}
