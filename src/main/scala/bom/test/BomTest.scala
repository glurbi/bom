package bom.test

import bom.schema._
import bom.types._
import bom.bin._

object BomTest {

  object TestSchema extends BOMSchema with BOMSchemaBuilder with BOMTypes {
    def schema = document {
      sequence("test") {
        number("bom_byte", bom_byte)
        number("bom_int", bom_int)
        number("bom_long", bom_long)
        number("bom_ubyte", bom_ubyte)
        number("bom_uint", bom_uint)
      }
    }
  }

  implicit def int2Byte(i: Int): Byte = i.asInstanceOf[Byte]

  def bspace: BOMBinarySpace = {
    val bytes: Array[Byte] = List[Byte](
      0x01,
      0x00, 0x00, 0x00, 0x0F,
      0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0F,
      0xFF,
      0x00, 0x00, 0xFF, 0xFF
    ).toArray
    new MemoryBinarySpace(bytes)
  }

  def main(args: Array[String]) = {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    val root = doc.rootNode
    assert(root.name.equals("test"))
    val test = root.asInstanceOf[BOMSequence]
    val bom_byte = test.child("bom_byte").asInstanceOf[BOMNumber].value
    assert(bom_byte.byteValue.equals(1.asInstanceOf[Byte]))
    val bom_int = test.child("bom_int").asInstanceOf[BOMNumber].value
    assert(bom_int.intValue.equals(15.asInstanceOf[Int]))
    val bom_long = test.child("bom_long").asInstanceOf[BOMNumber].value
    assert(bom_long.longValue.equals(15.asInstanceOf[Long]))
    val bom_ubyte = test.child(3).asInstanceOf[BOMNumber].value
    assert(bom_ubyte.shortValue.equals((0xFF).asInstanceOf[Short]))
    val bom_uint = test.child(4).asInstanceOf[BOMNumber].value
    assert(bom_uint.longValue.equals((0xFFFF).asInstanceOf[Long]))

    println(this.getClass.toString + " SUCCESS")
  }

}
