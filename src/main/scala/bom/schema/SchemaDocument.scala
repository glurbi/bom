package bom.schema

import bom.bin._
import bom._

case class SchemaDocument(
  override val name:String)
extends SchemaSequence(name, null, 0) {

  override val depth: Int = 0

}
