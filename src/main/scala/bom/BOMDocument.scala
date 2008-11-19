package bom

import java.io._
import java.util._
import org.w3c.dom._
import javax.xml.namespace._
import javax.xml.xpath._
import bom.dom._
import bom.schema._
import bom.bin._

/**
 * The <code>BOMDocument</code> interface defines the entry point for accessing
 * a concrete binary structure.
 */
case class BOMDocument(override val schema: BOMSchemaDefinition,
                       val bspace: BOMBinarySpace)
  extends BOMContainer(schema, null, 0) {

    /**
     * @return the root node of the binary structure
     */
  def rootNode: BOMNode = schema.children.get(0).createNode(this, 0)

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
  override def size: long = rootNode.size
  override def position: long = 0
  
  override def binarySpace: BOMBinarySpace = bspace
  
  def asDomNode: Node = {
    // the only reason for caching the document node is that saxon test equality based on
    // identity and not using the equals method (net.sf.saxon.dom.DocumentWrapper#wrap).
    if (domDocument == null) {
      domDocument = new BOMDocumentAdapter(this)
    }
    domDocument
  }
  
  def iterator: Iterator[BOMNode] = {
    new Iterator[BOMNode] {
      var used: boolean = false
      def hasNext: boolean = !used && (schema.children.size != 0)
      def next: BOMNode = {
        if (!used) {
          used = true;
          return schema.children.get(0).createNode(BOMDocument.this, 0)
        }
        return null
      }
      def remove = throw new UnsupportedOperationException
    }
  }

  override def equals(that: Any): boolean = this.eq(that.asInstanceOf[AnyRef])

}
