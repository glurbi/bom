package bom.types;

import bom.bin._

/**
 * see http://en.wikipedia.org/wiki/Binary-coded_decimal
 */
class BOMBinaryCodedDecimal(val digits: Int) extends BOMType {
  
  def typeName: String = "bcd"
  
  def typeSize(params: Any*): Int = 4 * digits
  
  def read(bspace: BOMBinarySpace, params: Any*): Any = {
    var result = 0
    for (i <- 0 until digits) {
      val digit: Int = bspace.getBits(4)
      result = (result * 10) + digit
    }
    result
  }

}