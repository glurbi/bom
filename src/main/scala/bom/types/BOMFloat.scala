package bom.types

import bom.bin._

class BOMFloat extends BOMType {

  def size: Int = 32

  def read(bspace: BinarySpace): Float = {
    java.lang.Float.intBitsToFloat((new BOMInteger).read(bspace).asInstanceOf[Int])
  }

}
