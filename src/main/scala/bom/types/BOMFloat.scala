package bom.types

import bom.bin._
import java.lang.{Float => JFloat}

class BOMFloat extends BOMType {

  def size: Int = 32

  def read(bspace: BOMBinarySpace): Float = {
    JFloat.intBitsToFloat((new BOMInteger).read(bspace).asInstanceOf[Int])
  }

}
