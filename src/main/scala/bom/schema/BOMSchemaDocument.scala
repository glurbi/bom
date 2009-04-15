package bom.schema

import bom.bin._
import bom._

case class BOMSchemaDocument(
  
  override val name:String)

  extends BOMSchemaSequence(name, null, 0) {

  override val depth: Int = 0

}
