package bom.test

import bom.schema._
import bom.types._
import bom.bin._

object BomTest {

  object TestSchema extends BOMSchema with BOMSchemaBuilder with BOMTypes {
    def schema = document {
      sequence("test") {
        sequence("integers") {
          number("i1", bom_byte)
          number("i2", bom_int)
          number("i3", bom_long)
          number("i4", bom_ubyte)
          number("i5", bom_uint)
          number("i6", bom_int)
          number("i7", bom_int3)
        }
        sequence("bcds") {
          number("bcd1", bom_bcd1)
          number("bcd2", bom_bcd2)
          number("bcd3", bom_bcd3)
          number("bcd4", bom_bcd4)
          number("bcd6", bom_bcd6)
          number("bcd8", bom_bcd8)
        }
      }
    }
  }

  implicit def int2Byte(i: Int): Byte = i.asInstanceOf[Byte]

  def bspace: BOMBinarySpace = {
    val bytes: Array[Byte] = List[Byte](

      // integers
      0x01,
      0x00, 0x00, 0x00, 0x0F,
      0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0F,
      0xFF,
      0x00, 0x00, 0xFF, 0xFF,
      0xFF, 0xFF, 0xFF, 0xFF,
      0xFF, 0xFF, 0xFF,

      // bcds
      0x98, 0x76, 0x54,
      0x12, 0x34,
      0x12, 0x34, 0x56,
      0x12, 0x34, 0x56, 0x78

    ).toArray
    new MemoryBinarySpace(bytes)
  }

  def main(args: Array[String]) = {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    val root = doc.rootNode
    
    assert(root.name.equals("test"))

    // integers
    assert(root(0)(0).value.equals(1.asInstanceOf[Byte]))
    assert(root(0)(1).value.equals(15))
    assert(root(0)(2).value.equals(15L))
    assert(root(0)(3).value.equals((0xFF).asInstanceOf[Short]))
    assert(root(0)(4).value.equals(0xFFFFL))
    assert(root(0)(5).value.equals(-1))
    assert(root(0)(6).value.equals(-1))

    // bcds
    assert(root(1)(0).value.equals(9L))
    assert(root(1)(1).value.equals(87L))
    assert(root("bcds")("bcd3").value.equals(654L))
    assert(root(1)(3).value.equals(1234L))
    assert(root(1)("bcd6").value.equals(123456L))
    assert(root("bcds")(5).value.equals(12345678L))

    println(this.getClass.toString + " SUCCESS")
  }

}
