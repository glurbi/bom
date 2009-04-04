package bom.schema

import java.util.{HashMap => JHashMap}
import java.util.{Iterator => JIterator}
import javax.xml.xpath._
import bom._
import bom.dom._
import bom.bin._
import org.w3c.dom._

case class BOMSchemaSwitch(override val parent: BOMSchemaElement,
                           override val depth: Int)
  extends BOMSchemaElement {

  override val name: String = null

  val cases = new JHashMap[Any, BOMSchemaCase]
  var switchExpression: String = _
  var defaultCase: BOMSchemaCase = _

  override def add(child: BOMSchemaElement) {
    if (!(child.isInstanceOf[BOMSchemaCase])) {
      throw new BOMException();
    }
    val scase = child.asInstanceOf[BOMSchemaCase]
    cases.put(scase.caseValue, scase)
  }

  def instance(parent: BOMContainer, index: Int): BOMNode = {
    val elementAdapter = new ElementAdapter(null) {
      override def hasChildNodes: Boolean = false
      def ownerDocument: Document = parent.document.asDomNode.asInstanceOf[Document]
      def parentNode: Node = parent.asDomNode
      override def getLocalName: String = ""
      override def getNodeName: String = ""
    }
    val node = new BOMNode(this, parent, index) {
      def asDomNode: Node = elementAdapter
      override def depth: Int = parent.depth + 1;
      def apply(index: Int): BOMNode = throw new BOMException
    }
    elementAdapter.node = node
    val matchingCase = findMatchingCase(
      parent.document.queryString(node, switchExpression))
    matchingCase.instance(parent, index)
  }

  override def children: List[BOMSchemaElement] = {
    var res: List[BOMSchemaElement] = Nil
    val it: JIterator[BOMSchemaCase] = cases.values.iterator
    while (it.hasNext) {
      res = it.next :: res
    }
    res
  }

  def findMatchingCase(o: Object): BOMSchemaCase = {
    var scase = cases.get(o)
    if (scase == null) {
      scase = defaultCase
    }
    scase
  }

}
