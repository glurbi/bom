package bom.schema

import java.util.{HashMap => JHashMap}
import java.util.{Iterator => JIterator}

import bom._
import bom.bin._

case class SchemaSwitch(

  override val parent: SchemaElement,

  override val depth: Int)

  extends SchemaElement {

  override val name: String = null

  val cases = new JHashMap[Any, SchemaCase]
  var switchFun: BOMNode => Any = _
  var defaultCase: SchemaCase = _

  override def add(child: SchemaElement) {
    if (!(child.isInstanceOf[SchemaCase])) {
      error("Invalid child type: " + child.getClass)
    }
    val scase = child.asInstanceOf[SchemaCase]
    cases.put(scase.caseValue, scase)
  }

  def instance(parent: BOMContainer, index: Int): BOMNode = {
    val node = new BOMNode(this, parent, index) {
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

  override def children: List[SchemaElement] = {
    var res: List[SchemaElement] = Nil
    val it: JIterator[SchemaCase] = cases.values.iterator
    while (it.hasNext) {
      res = it.next :: res
    }
    res
  }

  def findMatchingCase(o: Any): SchemaCase = {
    var scase = cases.get(o)
    if (scase == null) {
      scase = defaultCase
    }
    scase
  }

}
