package bom.dom

import bom.BOMException;
import bom.BOMNode;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

class AttributeAdapter(node: BOMNode, name: String, value: String)
  extends NodeAdapter(node) with Attr {

  def getNodeType: short = Node.ATTRIBUTE_NODE

  def getName: String = name

  override def getNodeName: String = name

  override def getLocalName: String = null

  def getValue: String = value

  override def hasChildNodes: boolean = false

  override def getNodeValue: String = value

  def getOwnerElement: Element = throw new BOMException

  def getSchemaTypeInfo: TypeInfo = throw new BOMException

  def getSpecified: boolean = throw new BOMException

  def isId: boolean = throw new BOMException

  def setValue(value: String) = throw new BOMException

}
