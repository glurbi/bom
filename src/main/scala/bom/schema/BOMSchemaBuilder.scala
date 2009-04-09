package bom.schema

import bom.types._
import bom.bin._
import scala.collection.mutable._

import java.lang.{Long => JLong}

trait BOMSchemaBuilder {

  protected val stack = new Stack[BOMSchemaElement]

  def document(body: => Unit): BOMSchemaDocument = {
    val d = new BOMSchemaDocument
    stack.push(d)
    body
    stack.pop.asInstanceOf[BOMSchemaDocument]
  }

  def sequence(name: String)(body: => Unit): BOMSchemaSequence = {
    val seq = new BOMSchemaSequence(name, stack.top, stack.top.depth + 1)
    stack.top.add(seq)
    stack.push(seq)
    body
    stack.pop
    seq
  }

  val regular = true
  val irregular = false

  def array(name: String, lengthFun: BOMNode => Long)(body: => Unit): BOMSchemaArray =
    array(name, lengthFun, false) { body }
  
  def array(name: String, lengthFun: BOMNode => Long, regular: Boolean)(body: => Unit): BOMSchemaArray = {
    val a = new BOMSchemaArray(name, stack.top, stack.top.depth + 1)
    stack.top.add(a)
    a.lengthFun = lengthFun
    a.regular = regular
    stack.push(a)
    body
    stack.pop
    a
  }

  def switch(xpath: String)(body: => Unit): BOMSchemaSwitch = {
    val bomSwitch = new BOMSchemaSwitch(stack.top, stack.top.depth)
    stack.top.add(bomSwitch)
    bomSwitch.switchExpression = xpath
    stack.push(bomSwitch)
    body
    stack.pop
    bomSwitch
  }

  // 'case' is a scala keyword, we use 'when' instead
  def when(value: String)(body: => Unit): BOMSchemaCase = {
    val bomCase = new BOMSchemaCase(stack.top, stack.top.depth)
    if ("*".equals(value)) {
      stack.top.asInstanceOf[BOMSchemaSwitch].defaultCase = bomCase
    } else {
      bomCase.caseValue = value
    }
    stack.top.add(bomCase)
    stack.push(bomCase)
    body
    stack.pop
    bomCase
  }

  implicit val body = () => {}
  def number(name: String, numberType: BOMType)(implicit body: () => Unit): BOMSchemaNumber =  {
    val n = new BOMSchemaNumber(name, stack.top, stack.top.depth + 1)
    stack.top.add(n)
    n.numberType = numberType
    stack.push(n)
    body()
    stack.pop
    n
  }

  def masks(body: => Unit) = () => { body }

  def mask(name: String, value: String) = {
    val longValue = JLong.decode(value).asInstanceOf[Long]
    stack.top.asInstanceOf[BOMSchemaNumber].addMask(name, longValue)
  }
  
  def map(body: => Unit) = () => { body }

  def value(from: String, to: String) {
    val n = stack.top.asInstanceOf[BOMSchemaNumber]
    if ("*".equals(from)) {
      n.defaultMapping = to
    }
    n.addMapping(from, to)
  }

  def string(name: String, encoding: String, sizeFun: BOMNode => Long): BOMSchemaString = {
    val s = new BOMSchemaString(name, stack.top, stack.top.depth + 1)
    s.sizeFun = sizeFun
    stack.top.add(s)
    s.encoding = encoding
    s
  }

  def blob(name: String, sizeFun: BOMNode => Long): BOMSchemaBlob = {
    val b = new BOMSchemaBlob(name, stack.top, stack.top.depth + 1)
    b.sizeFun = sizeFun
    stack.top.add(b)
    b
  }

  def virtual(name: String, valueFun: BOMNode => Any): BOMSchemaVirtual = {
    val v = new BOMSchemaVirtual(name, stack.top, stack.top.depth + 1)
    stack.top.add(v)
    v.valueFun = valueFun
    v
  }

  def byteSize(xpath: String): BOMNode => Long =
    (n: BOMNode) => n.document.queryNumber(n, xpath).intValue * 8

  def byteSize(size: Long): BOMNode => Long = (n: BOMNode) => size * 8

  def bitSize(xpath: String): BOMNode => Long =
    (n: BOMNode) => n.document.queryNumber(n, xpath).intValue

  def bitSize(size: Long): BOMNode => Long = (n: BOMNode) => size

  def position(fun: BOMNode => Long) = {
    stack.top.positionFun = fun
  }

  def size(fun: BOMNode => Long) = {
    stack.top.sizeFun = fun
  }

  def length(lengthFun: BOMNode => Long): BOMNode => Long = lengthFun

  def length(len: Long): BOMNode => Long = (n: BOMNode) => len

  def root(n: BOMNode): BOMNode = n.document.rootNode
}
