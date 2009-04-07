package bom

import java.util._

import bom.schema._

abstract case class BOMLeaf(override val schema: BOMSchemaElement,
                            override val parent: BOMContainer,
                            override val index: Int)
  extends BOMNode(schema, parent, index) {

  def length: Long = 0

  def /(index: Int): BOMNode = index match {
    case -1 => parent
    case _ => error("A leaf node doesn't have children.")
  }

  def /(name: String): BOMNode = null
  
  def iterator: Iterator[BOMNode] = new Iterator[BOMNode]() {
    def hasNext: Boolean = false
    def next: BOMNode = throw new NoSuchElementException
    def remove = throw new UnsupportedOperationException
  }

}
