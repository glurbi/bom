package bom.test

import java.util.{Set => JSet}

import bom.BOM._
import bom.schema._
import bom.types._
import bom.bin._

// TODO: split the test in many...
object BomTest {

  object TestSchema extends Schema with SchemaBuilder {

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
        string("greetings", "utf-8", byteSize(11))
      }

    def array1 =
      sequence("array1") {
        number("length_array1", bom_byte)
        array("array1", length(_ / -1 / "length_array1"), regular) {
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
          array("nested", length(n => n / -1 / -1 / "lengths" / n.index), regular) {
            number("item", bom_byte)
          }
        }
      }

    def virtuals =
      sequence("virtuals") {
        number("a", bom_int)
        number("b", bom_int)
        virtual("v1", n => (longValue(n / -1 / "a") + longValue(n / -1 / "b")) * 2)
        virtual("v2", n => Math.pow(2, n / -1 / "a"))
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
            switch(n => stringValue(n / -1 / "nb")) {
              when("1") {
                number("choice1", bom_ushort)
              }
              when("2") {
                sequence("choice2") {
                  number("length", bom_ubyte)
                  string("string", "utf-8", byteSize(_ / -1 / "length"))
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
        virtual("v1", n => longValue(n / -1 / (n.index -1)) + 3)
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

    def schema = document("test") {
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

  def bspace: BinarySpace = {
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
    
    assert(doc.name.equals("test"))

    // integers
    assert(value(doc/"integers"/0) == 1.asInstanceOf[Byte])
    assert(value(doc/0/1) == 15)
    assert(value(doc/0/2) == 15L)
    assert(value(doc / 0 / -1 / 0 / 2) == 15L)
    assert(value(doc/0/3) == (0xFF).asInstanceOf[Short])
    assert(value(doc/0/4) == 0xFFFFL)
    assert(value(doc/0/5) == -1)
    assert(value(doc/0/6) == -1)

    // bcds
    assert(value(doc/"bcds"/0) == 9L)
    assert(value(doc/1/1) == 87L)
    assert(value(doc/"bcds"/"bcd3") == 654L)
    assert(value(doc/1/3) == 1234L)
    assert(value(doc/1/"bcd6") == 123456L)
    assert(value(doc/"bcds"/5) == 12345678L)
    assert(value(doc / "bcds" / -1 / "bcds" / 5) == 12345678L)

    // blobs
    assert(size(doc/"blobs"/"blob1") == 3 * 8)
    assert(size(doc/"blobs"/"blob2") == 4 * 8)
    assert(value(doc/"blobs"/"blob2").asInstanceOf[Array[Byte]](0) == 1)
    assert(value(doc/"blobs"/"blob2").asInstanceOf[Array[Byte]](1) == 9)
    assert(value(doc/"blobs"/"blob2").asInstanceOf[Array[Byte]](2) == 7)
    assert(value(doc/"blobs"/"blob2").asInstanceOf[Array[Byte]](3) == 7)

    // strings
    assert(value(doc/"strings"/"greetings") == "Hello, BOM!")
    
    // array1
    assert(length(doc/"array1"/"array1") == 6)
    assert(value(doc/4/1/5) == 6.asInstanceOf[Byte])

    //array2
    assert(size(doc/"array2"/"array2") == 12 * 8)
    assert(value(doc/5/0/3/1) == 11)

    //array3
    assert(length(doc/"array3"/"array3"/0) == 4)
    assert(length(doc/"array3"/"array3"/1) == 5)
    assert(length(doc/"array3"/"array3"/2) == 6)
    assert(value(doc/"array3"/"array3"/2/5) == 0x66)

    // virtuals
    assert(value(doc/"virtuals"/"v1") == 36)
    assert(value(doc/"virtuals"/"v2") == 65536)

    // masks
    val nb1 = (doc/"masks"/"nb1").asInstanceOf[BOMNumber]
    assert(nb1.schema.getMasks(nb1.value.longValue).contains("BIT_ONE"))
    assert(nb1.schema.getMasks(nb1.value.longValue).contains("BIT_TWO"))
    assert(!nb1.schema.getMasks(nb1.value.longValue).contains("BIT_NINE"))
    assert(!nb1.schema.getMasks(nb1.value.longValue).contains("BIT_SEVENTEEN"))
    val nb2 = (doc/"masks"/"nb2").asInstanceOf[BOMNumber]
    assert(!nb2.schema.getMasks(nb2.value.longValue).contains("BIT_ONE"))
    assert(!nb2.schema.getMasks(nb2.value.longValue).contains("BIT_TWO"))
    assert(nb2.schema.getMasks(nb2.value.longValue).contains("BIT_NINE"))
    assert(nb2.schema.getMasks(nb2.value.longValue).contains("BIT_SEVENTEEN"))

    // mapping
    val mapped_nb1 = (doc/"mapping"/"mapped_nb1").asInstanceOf[BOMNumber]
    assert(mapped_nb1.schema.mappedValue(mapped_nb1.value) == "UNKNOWN")
    val mapped_nb2 = (doc/"mapping"/"mapped_nb2").asInstanceOf[BOMNumber]
    assert(mapped_nb2.schema.mappedValue(mapped_nb2.value) == "THREE")

    // switch
    assert(value(doc/"switch1"/"array"/0/1/"string") == "Foo")
    assert(value(doc/"switch1"/"array"/1/1) == 65535)

    // previousSibling
    assert(value(doc/"previousSibling"/"v1") == 5)

    // bitfield
    assert(value(doc/"bitFields"/"bitField"/"bit15") == 1)
    assert(value(doc/"bitFields"/"bitField"/"bit14") == 0)
    assert(value(doc/"bitFields"/"bitField"/"bit13") == 1)
    assert(value(doc/"bitFields"/"bitField"/"bit13") == 1)
    assert(value(doc/"bitFields"/"bitField"/"bit15") == 1)
    assert(value(doc/"bitFields"/"bitField"/"bit14") == 0)
    assert(value(doc/"bitFields"/"bitField"/"bit2") == 1)
    assert(value(doc/"bitFields"/"bitField"/"bit1and0") == 3)

    // overrides
    assert(position(doc/"overrides"/"n1") == position(doc/"overrides") + 8)
    assert(value(doc/"overrides"/"n1") == 0x34)
    assert(size(doc/"overrides") == 6 * 8)
    assert(value(doc/"overrides"/"a"/0) == 0x01)
    assert(value(doc/"overrides"/"a"/1) == 0x02)
    assert(value(doc/"overrides"/"a"/2) == 0x03)
    assert(size(doc/"overrides"/"a") == 4 * 8)

    println(this.getClass.toString + " SUCCESS")
  }

}
