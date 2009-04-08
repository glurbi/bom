package bom

import java.io._
import java.util._
import org.w3c.dom._
import javax.xml.namespace._
import javax.xml.xpath._
import bom.dom._
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

  /**
   * Query the document from the context node, with the xpath expression and
   * returns the result as a <code>java.lang.Number</code>.
   */
  def queryNumber(context : BOMNode, xpath : String) : Number =
    xpathEvaluator.evaluateAsNumber(xpath, context)


  /**
   * Query the document from the context node, with the xpath expression and
   * returns the result as a <code>java.lang.String</code>.
   */
  def queryString(context : BOMNode, xpath : String) : String =
    xpathEvaluator.evaluateAsString(xpath, context)

  /**
   * Query the document from the context node, with the xpath expression and
   * returns the result as a <code>BOMNode</code>.
   */
  def queryNode(context : BOMNode, xpath : String) : BOMNode =
    xpathEvaluator.evaluateAsElement(xpath, context)

  /**
   * Query the document from the context node, with the xpath expression and
   * returns the result as a <code>List<BOMNode></code>.
   */
  def queryNodeList(context : BOMNode, xpath : String) : List[BOMNode] =
    xpathEvaluator.evaluateAsElementList(xpath, context)
  
  var domDocument: Node = null
  val xpathEvaluator = new XPathEvaluator

  override def document: BOMDocument = this
  override lazy val size: Long = rootNode.size
  override lazy val position: Long = 0
  
  override def binarySpace: BOMBinarySpace = bspace

  override lazy val identifier: BOMIdentifier = Nil

  def asDomNode: Node = {
    // the only reason for caching the document node is that saxon test equality based on
    // identity and not using the equals method (net.sf.saxon.dom.DocumentWrapper#wrap).
    if (domDocument == null) {
      domDocument = new BOMDocumentAdapter(this)
    }
    domDocument
  }
  
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
