package bom.types

import bom.bin._
import java.lang.{Double => JDouble}

class BOMDouble extends BOMType {

  def typeSize(params: Any*): Int = 64

  def read(bspace: BOMBinarySpace , params: Any*): Double = {
    JDouble.longBitsToDouble((new BOMLong).read(bspace).asInstanceOf[Long])
  }

}