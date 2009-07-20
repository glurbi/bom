package bom.schema

import bom.types._
import bom.bin._
import scala.collection.mutable._

trait SchemaBuilder {

  protected val stack = new Stack[SchemaElement]

  def document(name: String)(body: => Unit): SchemaDocument = {
    val d = new SchemaDocument(name)
    stack.push(d)
    body
    stack.pop.asInstanceOf[SchemaDocument]
  }

  def sequence(name: String)(body: => Unit): SchemaSequence = {
    val seq = new SchemaSequence(name, stack.top, stack.top.depth + 1)
    stack.top.add(seq)
    stack.push(seq)
    body
    stack.pop
    seq
  }

  val regular = true
  val irregular = false

  def array(name: String, lengthFun: BOMNode => Long)(body: => Unit): SchemaArray =
    array(name, lengthFun, false) { body }
  
  def array(name: String, lengthFun: BOMNode => Long, regular: Boolean)(body: => Unit): SchemaArray = {
    val a = new SchemaArray(name, stack.top, stack.top.depth + 1)
    stack.top.add(a)
    a.lengthFun = lengthFun
    a.regular = regular
    stack.push(a)
    body
    stack.pop
    a
  }

  def switch(switchFun: BOMNode => Any)(body: => Unit): SchemaSwitch = {
    val bomSwitch = new SchemaSwitch(stack.top, stack.top.depth)
    stack.top.add(bomSwitch)
    bomSwitch.switchFun = switchFun
    stack.push(bomSwitch)
    body
    stack.pop
    bomSwitch
  }

  // 'case' is a scala keyword, we use 'when' instead
  def when(value: String)(body: => Unit): SchemaCase = {
    val bomCase = new SchemaCase(stack.top, stack.top.depth)
    if ("*".equals(value)) {
      stack.top.asInstanceOf[SchemaSwitch].defaultCase = bomCase
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
  def number(name: String, numberType: BOMType)(implicit body: () => Unit): SchemaNumber =  {
    val n = new SchemaNumber(name, stack.top, stack.top.depth + 1)
    stack.top.add(n)
    n.numberType = numberType
    stack.push(n)
    body()
    stack.pop
    n
  }

  def masks(body: => Unit) = () => { body }

  def mask(name: String, value: String) = {
    val longValue = java.lang.Long.decode(value).asInstanceOf[Long]
    stack.top.asInstanceOf[SchemaNumber].addMask(name, longValue)
  }
  
  //
  // for mapping leaf raw values to another value
  //
  
  def map(body: => Unit) = () => { body }
  def value(from: Any, to: AnyRef) { stack.top.asInstanceOf[SchemaLeaf].addMapping(from, to) }
  def default(value: AnyRef) { stack.top.asInstanceOf[SchemaLeaf].defaultMapping = value }

  def string(name: String, encoding: String, sizeFun: BOMNode => Long): SchemaString = {
    val s = new SchemaString(name, stack.top, stack.top.depth + 1)
    s.sizeFun = sizeFun
    stack.top.add(s)
    s.encoding = encoding
    s
  }

  def blob(name: String, sizeFun: BOMNode => Long): SchemaBlob = {
    val b = new SchemaBlob(name, stack.top, stack.top.depth + 1)
    b.sizeFun = sizeFun
    stack.top.add(b)
    b
  }

  def virtual(name: String, valueFun: BOMNode => Any): SchemaVirtual = {
    val v = new SchemaVirtual(name, stack.top, stack.top.depth + 1)
    stack.top.add(v)
    v.valueFun = valueFun
    v
  }

  def byteSize(size: Long): BOMNode => Long = (n: BOMNode) => size * 8
  def byteSize(sizeFun: BOMNode => Long): BOMNode => Long = n => sizeFun(n) * 8

  def bitSize(size: Long): BOMNode => Long = (n: BOMNode) => size
  def bitSize(sizeFun: BOMNode => Long): BOMNode => Long = sizeFun

  def position(fun: BOMNode => Long) {
    stack.top.positionFun = fun
  }

  def size(fun: BOMNode => Long) {
    stack.top.sizeFun = fun
  }

  def length(lengthFun: BOMNode => Long): BOMNode => Long = lengthFun

  def length(len: Long): BOMNode => Long = (n: BOMNode) => len

  def unbounded: BOMNode => Long = (n: BOMNode) => {
    var index = 0
    var done = false
    while (!done) {
      try {
        (n / index).size
        index += 1
      } catch {
        case e: java.nio.BufferUnderflowException => done = true
      }
    }
    index
  }
  
}
