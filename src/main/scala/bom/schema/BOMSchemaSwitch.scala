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
  var switchFun: BOMNode => Any = _
  var defaultCase: BOMSchemaCase = _

  override def add(child: BOMSchemaElement) {
    if (!(child.isInstanceOf[BOMSchemaCase])) {
      error("Invalid child type: " + child.getClass)
    }
    val scase = child.asInstanceOf[BOMSchemaCase]
    cases.put(scase.caseValue, scase)
  }

  def instance(parent: BOMContainer, index: Int): BOMNode = {
    val node = new BOMNode(this, parent, index) {
      def asDomNode: Node = error("Not implemented!")
      override def depth: Int = parent.depth + 1;
      override lazy val size: Long = error("Not implemented!")
      def /(index: Int): BOMNode = index match {
          case -1 => parent
          case _ => error("Illegal argument!")
      }
      def /(name: String): BOMNode = error("Not implemented!")
      def length: Long = error("Not implemented!")
      def iterator: JIterator[BOMNode] = error("Not implemented!")
    }
    findMatchingCase(switchFun(node)).instance(parent, index)
  }

  override def children: List[BOMSchemaElement] = {
    var res: List[BOMSchemaElement] = Nil
    val it: JIterator[BOMSchemaCase] = cases.values.iterator
    while (it.hasNext) {
      res = it.next :: res
    }
    res
  }

  def findMatchingCase(o: Any): BOMSchemaCase = {
    var scase = cases.get(o)
    if (scase == null) {
      scase = defaultCase
    }
    scase
  }

}
