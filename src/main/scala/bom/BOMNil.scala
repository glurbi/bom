package bom

object BOMNil extends BOMBaseNode(null, null, 0) {
  
  def elements: Iterator[BOMNode] = null
  def length: Long = 0
  def / (index: Int): BOMNode = this
  def / (name: String): BOMNode = this

}
