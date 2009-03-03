package bom.types

import bom.bin._
import java.lang.{Double => JDouble}

class BOMDouble extends BOMType {

  def size: Int = 64

  def read(bspace: BOMBinarySpace): Double = {
    JDouble.longBitsToDouble((new BOMLong).read(bspace).asInstanceOf[Long])
  }

}