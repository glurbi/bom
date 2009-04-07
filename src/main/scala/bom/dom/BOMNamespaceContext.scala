package bom.dom

import java.util.Iterator

import javax.xml.namespace.NamespaceContext

class BOMNamespaceContext extends NamespaceContext {

  val BOM_NAMESPACE = "http://bom/";

  def getNamespaceURI(prefix: String): String =
    if ("bom".equals(prefix)) BOM_NAMESPACE else null

  def getPrefix(namespaceURI: String): String = error("Not implemented!")

  override def getPrefixes(namespaceURI: String): Iterator[String] = error("Not implemented!")

}
