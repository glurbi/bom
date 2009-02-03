package bom.test

import bom.schema._
import bom.types._
import bom.bin._

object BomTest {

  object TestSchema extends BOMSchema with BOMSchemaBuilder with BOMTypes {
    def schema = document {
      sequence("test") {
        number("i1", bom_byte)
        number("i2", bom_int)
        number("i3", bom_long)
        number("i4", bom_ubyte)
        number("i5", bom_uint)
        number("i6", bom_int)
        number("i7", bom_int3)
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
      0x00, 0x00, 0xFF, 0xFF,
      0xFF, 0xFF, 0xFF, 0xFF,
      0xFF, 0xFF, 0xFF
    ).toArray
    new MemoryBinarySpace(bytes)
  }

  def main(args: Array[String]) = {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    val root = doc.rootNode
    
    assert(root.name.equals("test"))
    assert(root.child(0).value.equals(1.asInstanceOf[Byte]))
    assert(root.child(1).value.equals(15))
    assert(root.child(2).value.equals(15L))
    assert(root.child(3).value.equals((0xFF).asInstanceOf[Short]))
    assert(root.child(4).value.equals(0xFFFFL))
    assert(root.child(5).value.equals(-1))
    assert(root.child(6).value.equals(-1))

    println(this.getClass.toString + " SUCCESS")
  }

}
