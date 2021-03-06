package bom.types;

import bom.bin._

/**
 * see http://en.wikipedia.org/wiki/Binary-coded_decimal
 */
class BOMBinaryCodedDecimal(val digits: Int) extends BOMType {
  
  def size: Int = 4 * digits
  
  def read(bspace: BinarySpace): Long = {
    var result = 0
    for (i <- 0 until digits) {
      val digit: Int = bspace.getBits(4)
      result = (result * 10) + digit
    }
    result
  }

}
