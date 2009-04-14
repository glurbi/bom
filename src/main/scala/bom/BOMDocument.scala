package bom

import java.io._
import java.util._

import bom.schema._
import bom.bin._
import bom.BOM._

/**
 * The <code>BOMDocument</code> class defines the entry point for accessing
 * a concrete binary structure.
 */
case class BOMDocument(
  override val schema: BOMSchemaElement,

  /**
   * @return the binary space associated with this document.
   */
  val bspace: BOMBinarySpace)

  extends BOMContainer(schema, null, 0) {

    /**
     * @return the root node of the binary structure
     */
  def rootNode: BOMNode = schema.children(0).instance(this, 0)

  override def document: BOMDocument = this
  override lazy val size: Long = rootNode.size
  override lazy val position: Long = 0
  
  override def binarySpace: BOMBinarySpace = bspace

  override lazy val identifier: BOMIdentifier = Nil

  def iterator: Iterator[BOMNode] = new Iterator[BOMNode] {
      var used: Boolean = false
      def hasNext: Boolean = !used && (schema.children.size != 0)
      def next: BOMNode = {
        if (!used) {
          used = true;
          return schema.children(0).instance(BOMDocument.this, 0)
        }
        return null
      }
      def remove = throw new UnsupportedOperationException
    }

  def / (index: Int): BOMNode = index match {
        case 0 => schema.children(0).instance(BOMDocument.this, 0)
        case -1 => error("A document node doesn't have a parent.")
        case _ => error("Illegal index value.")
  }

  def / (name: String): BOMNode = null

  def length: Long = 1
  
}
