package bom.test

import org.scalatest._

import bom.BOM._
import bom.schema._
import bom.types._
import bom.bin._

class ArrayTestSuite extends FunSuite {

  object TestSchema extends Schema with SchemaBuilder {
    def schema = document("arrays") {
      sequence("array1") {
        number("length_array1", bom_byte)
        array("array1", length(_ / -1 / "length_array1"), regular) {
          number("item", bom_byte)
        }
      }
      sequence("array2") {
        array("array2", length(4), regular) {
          array("nested", length(3), regular) {
            number("item", bom_byte)
          }
        }
      }
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
    }
  }

  def bspace: BinarySpace = {
    implicit def int2Byte(i: Int): Byte = i.asInstanceOf[Byte]
    val bytes: Array[Byte] = List[Byte](
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
      0x06, 0x06, 0x06, 0x06, 0x06, 0x66
    ).toArray
    new MemoryBinarySpace(bytes)
  }

  test("regular array, dynamic length, simple array element type") {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    assert(length(doc/"array1"/"array1") == 6)
    assert(value(doc/"array1"/1/5) == 6.asInstanceOf[Byte])
  }

  test("regular array, static length, simple array element type") {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    assert(size(doc/"array2"/"array2") == 12 * 8)
    assert(value(doc/"array2"/0/3/1) == 11)
  }

  test("irregular array, static length, complex array element type") {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    assert(length(doc/"array3"/"array3"/0) == 4)
    assert(length(doc/"array3"/"array3"/1) == 5)
    assert(length(doc/"array3"/"array3"/2) == 6)
    assert(value(doc/"array3"/"array3"/2/5) == 0x66)
  }

}
