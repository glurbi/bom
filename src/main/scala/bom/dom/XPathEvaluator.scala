package bom.dom

import java.util._
import javax.xml.namespace._
import javax.xml.xpath._
import bom.dom._
import org.w3c.dom._

class XPathEvaluator {

  System.setProperty(
    "javax.xml.xpath.XPathFactory:" + XPathConstants.DOM_OBJECT_MODEL,
    "net.sf.saxon.xpath.XPathFactoryImpl")

  val xPathFactory = XPathFactory.newInstance
  val xPath = xPathFactory.newXPath
  val expressionCache = new HashMap[String, XPathExpression]
  val stringResultCache = new HashMap[CacheResult, String]
  val numberResultCache = new HashMap[CacheResult, Number]
  val functionResolver = new FunctionResolver
  val contexts = new ArrayDeque[BOMNode]
  val xpathFunctions = new HashMap[String, XPathFunction]

  val bomContextFunction = new XPathFunction {
    def evaluate(args: List[_]): Object = contexts.getFirst.asDomNode
  }

  val bomPreviousSiblingFunction = new XPathFunction {
    def evaluate(args: List[_]): Object =
      contexts.getFirst.asDomNode.getPreviousSibling
  }

  val bomPowerFunction = new XPathFunction {
    def evaluate(args: List[_]): Object = {
      val base = args.get(0).asInstanceOf[Number].doubleValue
      val exponent = args.get(1).asInstanceOf[List[Node]].get(0).
        asInstanceOf[BOMLeafAdapter].node.asInstanceOf[BOMNumber].value.doubleValue
      double2Double(Math.pow(base, exponent))
    }
  }

  xPath.setNamespaceContext(new BOMNamespaceContext)
  xPath.setXPathFunctionResolver(functionResolver)
  xpathFunctions.put("context", bomContextFunction)
  xpathFunctions.put("previous-sibling", bomPreviousSiblingFunction)
  xpathFunctions.put("power", bomPowerFunction)

  def evaluateAsElementList(xpath: String, context: BOMNode): List[BOMNode] = {
    contexts.push(context)
    try {
      val nodes = xpathExpression(xpath).
        evaluate(context.asDomNode, XPathConstants.NODESET).asInstanceOf[NodeList]
      val result = new ArrayList[BOMNode](nodes.getLength)
      for (i <- 0 until nodes.getLength) {
        result.add((nodes.item(i).asInstanceOf[NodeAdapter]).node)
      }
      result
    } catch {
      case xpee: XPathExpressionException => error(xpee.getMessage)
    } finally {
      contexts.pop();
    }
  }

  def evaluateAsString(xpath: String, context: BOMNode): String = {
    contexts.push(context)
    val id = new CacheResult(xpath, context.identifier)
    var result = stringResultCache.get(id)
    if (result == null) {
      try {
        result = xpathExpression(xpath).
          evaluate(context.asDomNode, XPathConstants.STRING).asInstanceOf[String]
      } catch {
        case xpee: XPathExpressionException => error(xpee.getMessage)
      }
      stringResultCache.put(id, result)
    }
    contexts.pop()
    result
  }

  def evaluateAsNumber(xpath: String, context: BOMNode): Number = {
    contexts.push(context)
    val id = new CacheResult(xpath, context.identifier)
    var result = numberResultCache.get(id)
    if (result == null) {
      try {
        result = xpathExpression(xpath).
          evaluate(context.asDomNode, XPathConstants.NUMBER).asInstanceOf[Number]
      } catch {
        case xpee: XPathExpressionException => error(xpee.getMessage)
      }
      numberResultCache.put(id, result)
    }
    contexts.pop
    result
  }

  def evaluateAsElement(xpath: String, context: BOMNode): BOMNode = {
    contexts.push(context)
    try {
      val n = xpathExpression(xpath).
        evaluate(context.asDomNode, XPathConstants.NODE).asInstanceOf[NodeAdapter]
      val result = if (n != null) n.node else null
      result
    } catch {
      case xpee: XPathExpressionException => error(xpee.getMessage)
    } finally {
      contexts.pop
    }
  }

  def xpathExpression(xpath: String): XPathExpression = {
    var xexp = expressionCache.get(xpath)
    if (xexp == null) {
      xexp = xPath.compile(xpath)
      expressionCache.put(xpath, xexp)
    }
    xexp
  }

  class FunctionResolver extends XPathFunctionResolver {
    def resolveFunction(functionName: QName, arity: Int): XPathFunction = {
      xpathFunctions.get(functionName.getLocalPart)
    }
  }

  case class CacheResult(val xpath: String, val nodeIdentifier: BOMIdentifier)

}
