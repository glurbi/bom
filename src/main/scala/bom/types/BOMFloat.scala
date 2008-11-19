package bom.types

import bom.bin._
import java.lang.{Float => JFloat}

class BOMFloat extends BOMType {

  def typeName = "float"

  def typeSize(params: Any*): int = 4

  def read(bspace: BOMBinarySpace, params: Any*): Any = {
    JFloat.intBitsToFloat((new BOMInteger).read(bspace).asInstanceOf[Int])
  }

}
