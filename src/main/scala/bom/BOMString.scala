package bom

import java.nio._
import java.nio.charset._
import javax.xml.xpath._
import bom.dom._
import bom.schema._
import org.w3c.dom._

/**
 * The <code>BOMString</code> interface defines a concrete node in the Binary Object Model, that
 * can be interpreted as a character string. It occupies physical space in the binary space.
 */
case class BOMString(override val schema: BOMSchemaString,
                     override val parent: BOMContainer,
                     override val index: Int)
  extends BOMLeaf(schema, parent, index) {

  /**
   * @return the value of this character string in the binary space
   */
  def value: String = {
    binarySpace.position(position.intValue)
    val ba = new Array[byte](size.intValue / 8)
    binarySpace.getBytes(ba)
    val encoding = schema.asInstanceOf[BOMSchemaString].encoding
    val charset = Charset.availableCharsets.get(encoding)
    charset.decode(ByteBuffer.wrap(ba)).toString
  }
 
  override def size: Long = schema.sizeFun(this)

  def asDomNode: Node = new BOMLeafAdapter(this)

  override def toString: String = name + " " + value

}
