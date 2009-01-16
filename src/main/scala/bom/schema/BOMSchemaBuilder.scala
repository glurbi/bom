package bom.schema

import bom.types._
import bom.bin._
import scala.collection.mutable._

import java.lang.{Long => JLong}

trait BOMSchemaBuilder {

  protected val stack = new Stack[BOMSchemaElement]

  def definition(byteOrder: ByteOrder)(body: => Unit): BOMSchemaDefinition = {
    val d = new BOMSchemaDefinition
    stack.push(d)
    d.byteOrder = byteOrder
    body
    stack.pop.asInstanceOf[BOMSchemaDefinition]
  }
  
  def typedef(body: => Unit): BOMSchemaDefinition = {
    stack.push(new BOMSchemaDefinition)
    body
    stack.pop.asInstanceOf[BOMSchemaDefinition]
  }

  def sequence(name: String)(body: => Unit) = {
    val seq = new BOMSchemaSequence
    stack.top.appendChild(seq)
    seq.parent = stack.top
    seq.name = name
    stack.push(seq)
    body
    stack.pop
  }

  def array(name: String, lengthXPath: String)(body: => Unit): Unit =
    array(name, lengthXPath, false) { body }
  
  def array(name: String, lengthXPath: String, regular: Boolean)(body: => Unit) = {
    val a = new BOMSchemaArray
    stack.top.appendChild(a)
    a.parent = stack.top
    a.name = name
    a.arrayLengthExpression = lengthXPath
    a.regular = regular
    stack.push(a)
    body
    stack.pop
  }

  def switch(xpath: String)(body: => Unit) = {
    val bomSwitch = new BOMSchemaSwitch
    stack.top.appendChild(bomSwitch)
    bomSwitch.parent = stack.top
    bomSwitch.switchExpression = xpath
    stack.push(bomSwitch)
    body
    stack.pop
  }

  // 'case' is a scala keyword, we use 'when' instead
  def when(value: String)(body: => Unit) = {
    val bomCase = new BOMSchemaCase
    if ("*".equals(value)) {
      stack.top.asInstanceOf[BOMSchemaSwitch].defaultCase = bomCase
    } else {
      bomCase.caseValue = value
    }
    stack.top.appendChild(bomCase)
    bomCase.parent = stack.top
    stack.push(bomCase)
    body
    stack.pop
  }

  def reference(definition: BOMSchemaDefinition) = {
    val schemaElement = definition.children.get(0)
    stack.top.appendChild(schemaElement)
    schemaElement.parent = stack.top
  }
  
  def number(name: String, numberType: BOMType): Unit =
    number(name, numberType, {})

  def number(name: String, numberType: BOMType, body: => Unit): Unit =  {
    val n = new BOMSchemaNumber
    stack.top.appendChild(n)
    n.parent = stack.top
    n.name = name
    n.numberType = numberType
    stack.push(n)
    body
    stack.pop
  }
  
  def masks(body: => Unit) = { body }

  def mask(name: String, value: String) = {
    val longValue = JLong.decode(value).asInstanceOf[Long]
    stack.top.asInstanceOf[BOMSchemaNumber].addMask(name, longValue)
  }
  
  def map(body: => Unit) = { body }

  def value(from: String, to: String) {
    val n = stack.top.asInstanceOf[BOMSchemaNumber]
    if ("*".equals(from)) {
      n.defaultMapping = to
    }
    n.addMapping(from, to)
  }

  def string(name: String, encoding: String, sizeXPath: String) = {
    val s = new BOMSchemaString
    stack.top.appendChild(s)
    s.parent = stack.top
    s.name = name
    s.encoding = encoding
    s.sizeExpression(sizeXPath)
  }
  
}
