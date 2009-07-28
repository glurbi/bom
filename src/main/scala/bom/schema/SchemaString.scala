package bom.schema

import java.nio.charset._

import bom._
import bom.bin._

case class SchemaString(
  override val name: String,
  override val parent: SchemaElement,
  override val depth: Int)
extends SchemaElement {

  var encoding: String = _
  lazy val charset = Charset.availableCharsets.get(encoding)
    
  def add(child: SchemaElement) = error("A string cannot have a child element.")

  override def children: List[SchemaElement] = null

  override def createNode(parent: BOMContainer, index: Int): BOMNode =
    new BOMString(this, parent, index)

}
