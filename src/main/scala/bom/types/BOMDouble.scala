package bom.types

import bom.bin._

class BOMDouble extends BOMType {

  def size: Int = 64

  def read(bspace: BinarySpace): Double = {
    java.lang.Double.longBitsToDouble((new BOMLong).read(bspace).asInstanceOf[Long])
  }

}