package bom

import java.util._

object BOMNil extends BOMNode(null, null, 0) {

  def iterator: Iterator[BOMNode] = null
  def length: Long = 0
  def / (index: Int): BOMNode = this
  def / (name: String): BOMNode = this

}
