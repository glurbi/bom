package bom.schema

import scala.collection.mutable.HashMap

import bom._
import bom.bin._

case class SchemaSwitch(
  override val parent: SchemaElement,
  override val depth: Int)
extends SchemaElement {

  override val name: String = null

  val cases = new HashMap[Any, SchemaCase]
  var switchFun: BOMNode => Any = _
  var defaultCase: SchemaCase = _

  override def add(child: SchemaElement) {
    //TODO: should use pattern matching
    if (!(child.isInstanceOf[SchemaCase])) {
      error("Invalid child type: " + child.getClass)
    }
    val scase = child.asInstanceOf[SchemaCase]
    if (scase.caseValue != null) {
      cases.put(scase.caseValue, scase)
    }
  }

  override def createNode(parent: BOMContainer, index: Int): BOMNode = {
    val node = new BOMBaseNode(this, parent, index) {
      override def depth: Int = parent.depth + 1;
      override def size: Long = error("Not implemented!")
      def /(index: Int): BOMNode = index match {
          case -1 => parent
          case _ => error("Illegal argument!")
      }
      def /(name: String): BOMNode = error("Not implemented!")
      def length: Long = error("Not implemented!")
      def elements: Iterator[BOMNode] = error("Not implemented!")
    }
    findMatchingCase(switchFun(node)).instance(parent, index)
  }

  override def children: List[SchemaElement] = cases.values.toList

  def findMatchingCase(o: Any): SchemaCase = {
    var scase = cases.getOrElse(o, null)
    if (scase == null) {
      scase = defaultCase
    }
    scase
  }

}
