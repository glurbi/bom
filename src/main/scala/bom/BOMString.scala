package bom

import java.nio._
import java.nio.charset._
import javax.xml.xpath._
import bom.dom._
import bom.schema._
import org.w3c.dom._

/**
 * The <code>BOMString</code> class defines a node that is interpreted as a
 * character string.
 */
case class BOMString(
  override val schema: BOMSchemaString,
  override val parent: BOMContainer,
  override val index: Int)
  extends BOMLeaf(schema, parent, index) {

  /**
   * @return the value of this character string in the binary space
   */
  override def value: String = {
    binarySpace.position(position.intValue)
    val ba = new Array[byte](size.intValue / 8)
    binarySpace.getBytes(ba)
    val encoding = schema.asInstanceOf[BOMSchemaString].encoding
    val charset = Charset.availableCharsets.get(encoding)
    charset.decode(ByteBuffer.wrap(ba)).toString
  }
 
  override lazy val size: Long = schema.sizeFun(this)

  def asDomNode: Node = new BOMLeafAdapter(this)

}
