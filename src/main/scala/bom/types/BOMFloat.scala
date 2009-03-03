package bom.types

import bom.bin._
import java.lang.{Float => JFloat}

class BOMFloat extends BOMType {

  def typeSize(params: Any*): Int = 32

  def read(bspace: BOMBinarySpace, params: Any*): Float = {
    JFloat.intBitsToFloat((new BOMInteger).read(bspace).asInstanceOf[Int])
  }

}
