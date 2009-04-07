package bom.test

import java.util.{Set => JSet}

import bom.BOM._
import bom.schema._
import bom.types._
import bom.bin._

// TODO: split the test in many...
object BomTest {

  object TestSchema extends BOMSchema with BOMSchemaBuilder with BOMTypes {

    def integers =
      sequence("integers") {
        number("i1", bom_byte)
        number("i2", bom_int)
        number("i3", bom_long)
        number("i4", bom_ubyte)
        number("i5", bom_uint)
        number("i6", bom_int)
        number("i7", bom_int3)
      }

    def bcds =
      sequence("bcds") {
        number("bcd1", bom_bcd1)
        number("bcd2", bom_bcd2)
        number("bcd3", bom_bcd3)
        number("bcd4", bom_bcd4)
        number("bcd6", bom_bcd6)
        number("bcd8", bom_bcd8)
      }

    def blobs =
      sequence("blobs") {
        blob("blob1", byteSize(3))
        blob("blob2", bitSize(32))
      }

    def strings =
      sequence("strings") {
        string("greetings", "utf-8", byteSize("11"))
      }

    def array1 =
      sequence("array1") {
        number("length_array1", bom_byte)
        array("array1", length("../length_array1"), regular) {
          number("item", bom_byte)
        }
      }

    def array2 =
      sequence("array2") {
        array("array2", length(4), regular) {
          array("nested", length(3), regular) {
            number("item", bom_byte)
          }
        }
      }

    def array3 =
      sequence("array3") {
        array("lengths", length(3), regular) {
          number("length", bom_byte)
        }
        array("array3", length(3), irregular) {
          array("nested", length("../../lengths/length[number(bom:context()/@index)]"), regular) {
            number("item", bom_byte)
          }
        }
      }

    def virtuals =
      sequence("virtuals") {
        number("a", bom_int)
        number("b", bom_int)
        virtual("v1", "(../a + ../b) * 2")
        virtual("v2", "bom:power(2, ../a)")
      }

    def masking =
      sequence("masks") {
        number("nb1", bom_ushort) {
          externalMaskDef
        }
        number("nb2", bom_ushort) {
          masks {
            mask("BIT_ONE", "0x0001")
            mask("BIT_TWO", "0x0002")
            mask("BIT_NINE", "0x0010")
            mask("BIT_SEVENTEEN", "0x0100")
          }
        }
      }
    def externalMaskDef =
      masks {
        mask("BIT_ONE", "0x0001")
        mask("BIT_TWO", "0x0002")
        mask("BIT_NINE", "0x0010")
        mask("BIT_SEVENTEEN", "0x0100")
      }

    def mapping =
      sequence("mapping") {
        number("mapped_nb1", bom_ubyte) {
          map {
            value("1", "ONE")
            value("2", "TWO")
            value("3", "THREE")
            value("*", "UNKNOWN")
          }
        }
        number("mapped_nb2", bom_ubyte) {
          map {
            value("1", "ONE")
            value("2", "TWO")
            value("3", "THREE")
            value("*", "UNKNOWN")
          }
        }
      }

    def switch1 =
      sequence("switch1") {
        array("array", length(2)) {
          sequence("item") {
            number("nb", bom_ubyte)
            switch("../nb") {
              when("1") {
                number("choice1", bom_ushort)
              }
              when("2") {
                sequence("choice2") {
                  number("length", bom_ubyte)
                  string("string", "utf-8", byteSize("../length"))
                }
              }
            }

          }
        }
      }

    def previousSibling =
      sequence("previousSibling") {
        number("n1", bom_ubyte)
        number("n2", bom_ubyte)
        virtual("v1", "bom:previous-sibling() + 3")
      }

    def bitFields =
      sequence("bitFields") {
        sequence("bitField") {
          number("bit15", bom_bitint1)
          number("bit14", bom_bitint1)
          number("bit13", bom_bitint1)
          number("bit12", bom_bitint1)
          blob("bitblob", bitSize(9))
          number("bit2", bom_bitint1)
          number("bit1and0", bom_bitint2)
        }
      }

    def overrides =
      sequence("overrides") {
        size(byteSize(6))
        number("n1", bom_ubyte) (() => {
          position(n => n.parent.position + 8)
        })
        array("a", length(3)) {
          size(byteSize(4))
          number("n2", bom_ubyte)
        }
      }

    def schema = document {
      sequence("test") {
        integers
        bcds
        blobs
        strings
        array1
        array2
        array3
        virtuals
        masking
        mapping
        switch1
        previousSibling
        bitFields
        overrides
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

      // blobs
      0x01, 0x02, 0x03,
      0x01, 0x09, 0x07, 0x07,

      // strings
      0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x2C, 0x20, 0x42, 0x4F, 0x4D, 0x21,

      // array1
      0x06,
      0x01, 0x02, 0x03, 0x04, 0x05, 0x06,

      // array2
      0x01, 0x02, 0x03,
      0x04, 0x05, 0x06,
      0x07, 0x08, 0x09,
      0x0A, 0x0B, 0x0C,

      // array3
      0x04, 0x05, 0x06,
      0x04, 0x04, 0x04, 0x44,
      0x05, 0x05, 0x05, 0x05, 0x55,
      0x06, 0x06, 0x06, 0x06, 0x06, 0x66,

      // virtuals
      0x00, 0x00, 0x00, 0x10,
      0x00, 0x00, 0x00, 0x02,

      // masking
      0x00, 0x03,
      0x01, 0x10,

      // mapping
      0x00, 0x03,

      // switch1
      0x02, 0x03, 0x46, 0x6F, 0x6F,
      0x01, 0xFF, 0xFF,

      // previousSibling
      0x01, 0x02,

      // bitfields
      0xB0, 0x07, // bitfield: 1011 000000000 111

      // overrides
      0x12, 0x34, 0x01, 0x02, 0x03, 0xFF

    ).toArray
    new MemoryBinarySpace(bytes)
  }

  def main(args: Array[String]) = {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    val root = doc.rootNode
    
    assert(root.name.equals("test"))

    // integers
    assert(value(root/"integers"/0) == 1.asInstanceOf[Byte])
    assert(value(root/0/1) == 15)
    assert(value(root/0/2) == 15L)
    assert(value(root / 0 / -1 / 0 / 2) == 15L)
    assert(value(root/0/3) == (0xFF).asInstanceOf[Short])
    assert(value(root/0/4) == 0xFFFFL)
    assert(value(root/0/5) == -1)
    assert(value(root/0/6) == -1)

    // bcds
    assert(value(root/"bcds"/0) == 9L)
    assert(value(root/1/1) == 87L)
    assert(value(root/"bcds"/"bcd3") == 654L)
    assert(value(root/1/3) == 1234L)
    assert(value(root/1/"bcd6") == 123456L)
    assert(value(root/"bcds"/5) == 12345678L)
    assert(value(root / "bcds" / -1 / "bcds" / 5) == 12345678L)

    // blobs
    assert(size(root/"blobs"/"blob1") == 3 * 8)
    assert(size(root/"blobs"/"blob2") == 4 * 8)
    assert(value(root/"blobs"/"blob2").asInstanceOf[Array[Byte]](0) == 1)
    assert(value(root/"blobs"/"blob2").asInstanceOf[Array[Byte]](1) == 9)
    assert(value(root/"blobs"/"blob2").asInstanceOf[Array[Byte]](2) == 7)
    assert(value(root/"blobs"/"blob2").asInstanceOf[Array[Byte]](3) == 7)

    // strings
    assert(value(root/"strings"/"greetings") == "Hello, BOM!")
    
    // array1
    assert(length(root/"array1"/"array1") == 6)
    assert(value(root/4/1/5) == 6.asInstanceOf[Byte])

    //array2
    assert(size(root/"array2"/"array2") == 12 * 8)
    assert(value(root/5/0/3/1) == 11)

    //array3
    assert(length(root/"array3"/"array3"/0) == 4)
    assert(length(root/"array3"/"array3"/1) == 5)
    assert(length(root/"array3"/"array3"/2) == 6)
    assert(value(root/"array3"/"array3"/2/5) == 0x66)

    // virtuals
    assert(value(root/"virtuals"/"v1") == 36)
    assert(value(root/"virtuals"/"v2") == 65536)

    // masks
    val nb1 = (root/"masks"/"nb1").asInstanceOf[BOMNumber]
    assert(nb1.schema.getMasks(nb1.value.longValue).contains("BIT_ONE"))
    assert(nb1.schema.getMasks(nb1.value.longValue).contains("BIT_TWO"))
    assert(!nb1.schema.getMasks(nb1.value.longValue).contains("BIT_NINE"))
    assert(!nb1.schema.getMasks(nb1.value.longValue).contains("BIT_SEVENTEEN"))
    val nb2 = (root/"masks"/"nb2").asInstanceOf[BOMNumber]
    assert(!nb2.schema.getMasks(nb2.value.longValue).contains("BIT_ONE"))
    assert(!nb2.schema.getMasks(nb2.value.longValue).contains("BIT_TWO"))
    assert(nb2.schema.getMasks(nb2.value.longValue).contains("BIT_NINE"))
    assert(nb2.schema.getMasks(nb2.value.longValue).contains("BIT_SEVENTEEN"))

    // mapping
    val mapped_nb1 = (root/"mapping"/"mapped_nb1").asInstanceOf[BOMNumber]
    assert(mapped_nb1.schema.mappedValue(mapped_nb1.value) == "UNKNOWN")
    val mapped_nb2 = (root/"mapping"/"mapped_nb2").asInstanceOf[BOMNumber]
    assert(mapped_nb2.schema.mappedValue(mapped_nb2.value) == "THREE")

    // switch
    assert(value(root/"switch1"/"array"/0/1/"string") == "Foo")
    assert(value(root/"switch1"/"array"/1/1) == 65535)

    // previousSibling
    assert(value(root/"previousSibling"/"v1") == 5)

    // bitfield
    assert(value(root/"bitFields"/"bitField"/"bit15") == 1)
    assert(value(root/"bitFields"/"bitField"/"bit14") == 0)
    assert(value(root/"bitFields"/"bitField"/"bit13") == 1)
    assert(value(root/"bitFields"/"bitField"/"bit13") == 1)
    assert(value(root/"bitFields"/"bitField"/"bit15") == 1)
    assert(value(root/"bitFields"/"bitField"/"bit14") == 0)
    assert(value(root/"bitFields"/"bitField"/"bit2") == 1)
    assert(value(root/"bitFields"/"bitField"/"bit1and0") == 3)

    // overrides
    assert(position(root/"overrides"/"n1") == position(root/"overrides") + 8)
    assert(value(root/"overrides"/"n1") == 0x34)
    assert(size(root/"overrides") == 6 * 8)
    assert(value(root/"overrides"/"a"/0) == 0x01)
    assert(value(root/"overrides"/"a"/1) == 0x02)
    assert(value(root/"overrides"/"a"/2) == 0x03)
    assert(size(root/"overrides"/"a") == 4 * 8)

    println(this.getClass.toString + " SUCCESS")
  }

}
