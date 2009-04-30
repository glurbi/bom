package bom

import java.io._
import java.util._

import bom.schema._
import bom.bin._
import bom.BOM._
import bom.cache._

/**
 * The <code>BOMDocument</code> class defines the entry point for accessing
 * a concrete binary structure.
 */
case class BOMDocument(

  override val schema: SchemaDocument,
  override val binarySpace: BinarySpace)

extends BOMSequence(schema, null, 0) with NodeCache {

  override def document: BOMDocument = this
  override lazy val position: Long = 0
  override lazy val identifier: BOMIdentifier = Nil

}
