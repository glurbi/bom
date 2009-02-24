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
        sequence("array1") {
          number("length_array1", bom_byte)
          array("array1", "../length_array1", regular) {
            number("item", bom_byte)
          }
        }
        sequence("array2") {
          array("array2", "4", regular) {
            array("nested", "3", regular) {
              number("item", bom_byte)
            }
          }
        }
        sequence("blobs") {
          blob("blob1", byteSize(3))
          blob("blob2", bitSize(32))
        }
      }
    }
  }

  def bspace: BOMBinarySpace = {
    implicit def int2Byte(i: Int): Byte = i.asInstanceOf[Byte]
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
      0x12, 0x34, 0x56, 0x78,

      // array1
      0x06,
      0x01, 0x02, 0x03, 0x04, 0x05, 0x06,

      // array2
      0x01, 0x02, 0x03,
      0x04, 0x05, 0x06,
      0x07, 0x08, 0x09,
      0x0A, 0x0B, 0x0C,

      // blobs
      0x01, 0x02, 0x03,
      0x01, 0x09, 0x07, 0x07

    ).toArray
    new MemoryBinarySpace(bytes)
  }

  def main(args: Array[String]) = {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    val root = doc.rootNode
    
    assert(root.name.equals("test"))

    // integers
    assert(root("integers")(0).value == 1.asInstanceOf[Byte])
    assert(root(0)(1).value == 15)
    assert(root(0)(2).value == 15L)
    assert(root(0)(3).value == (0xFF).asInstanceOf[Short])
    assert(root(0)(4).value == 0xFFFFL)
    assert(root(0)(5).value == -1)
    assert(root(0)(6).value == -1)

    // bcds
    assert(root("bcds")(0).value == 9L)
    assert(root(1)(1).value == 87L)
    assert(root("bcds")("bcd3").value == 654L)
    assert(root(1)(3).value == 1234L)
    assert(root(1)("bcd6").value == 123456L)
    assert(root("bcds")(5).value == 12345678L)

    // array_1
    assert(root("array1")("array1").childCount == 6)
    assert(root(2)(1)(5).value == 6.asInstanceOf[Byte])

    //array_2
    assert(root("array2")("array2").size == 12 * 8)
    assert(root(3)(0)(3)(1).value == 11)

    // blobs
    assert(root("blobs")("blob1").size == 3 * 8)
    assert(root("blobs")("blob2").size == 4 * 8)
    assert(root("blobs")("blob2").value.asInstanceOf[Array[Byte]](0) == 1)
    assert(root("blobs")("blob2").value.asInstanceOf[Array[Byte]](1) == 9)
    assert(root("blobs")("blob2").value.asInstanceOf[Array[Byte]](2) == 7)
    assert(root("blobs")("blob2").value.asInstanceOf[Array[Byte]](3) == 7)

    println(this.getClass.toString + " SUCCESS")
  }

}
