package bom.schema

import java.util._
import javax.xml.xpath._
import bom._
import bom.dom._
import bom.bin._
import org.w3c.dom._

case class BOMSchemaSwitch extends BOMSchemaElement {

  val cases = new HashMap[Any, BOMSchemaCase]
  var switchExpression: String = null
  var defaultCase: BOMSchemaCase = null

  override def appendChild(schema: BOMSchemaElement) {
    if (!(schema.isInstanceOf[BOMSchemaCase])) {
      throw new BOMException();
    }
    val scase = schema.asInstanceOf[BOMSchemaCase]
    cases.put(scase.caseValue, scase)
  }

  def createNode(parent: BOMContainer, index: int): BOMNode = {
    val elementAdapter = new ElementAdapter(null) {
      override def hasChildNodes: boolean = false
      def ownerDocument: Document = parent.document.asDomNode.asInstanceOf[Document]
      def parentNode: Node = parent.asDomNode
      override def getLocalName: String = ""
      override def getNodeName: String = ""
    }
    val node = new BOMNode(this, parent, index) {
      def asDomNode: Node = elementAdapter
      override def depth: int = parent.depth + 1;
    }
    elementAdapter.node = node
    val matchingCase = findMatchingCase(
      parent.document.queryString(node, switchExpression))
    matchingCase.createNode(parent, index)
  }

  override def children: List[BOMSchemaElement] =
    new ArrayList[BOMSchemaElement](cases.values)

  override def childrenCount: int = cases.size

  def findMatchingCase(o: Object): BOMSchemaCase = {
    var scase = cases.get(o)
    if (scase == null) {
      scase = defaultCase
    }
    scase
  }

  override def depth: int = parent.depth

}
