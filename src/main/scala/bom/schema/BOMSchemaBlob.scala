package bom.schema

import java.util._
import bom._
import bom.bin._

case class BOMSchemaBlob extends BOMSchemaElement {

  def appendChild(schema: BOMSchemaElement) = throw new BOMException

  def childrenCount: int = 0

  override def children: List[BOMSchemaElement] = null

  override def createNode(parent: BOMContainer, index: int): BOMNode = {
    if (sizeExpression != null) {
      new BOMBlob(this, parent, index, (n: BOMNode) =>
        n.document.queryNumber(n, sizeExpression).intValue * 8)
    } else {
      new BOMBlob(this, parent, index, BOMNode => size * 8)
    }
  }

}
