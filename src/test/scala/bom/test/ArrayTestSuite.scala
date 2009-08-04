package bom.test

import org.scalatest._

import bom.BOM._
import bom.schema._
import bom.bin._

class ArrayTestSuite extends FunSuite {

  object TestSchema extends Schema with SchemaBuilder {
    def schema = document("arrays") {
      sequence("array1") {
        number("length_array1", bom_byte)
        def lengthFun(n: BOMNode) = longValue(n / -1 / "length_array1")
        array("array1", length(lengthFun _), regular) {
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
          array("nested", length(n => longValue(n / -1 / -1 / "lengths" / n.index)), regular) {
            number("item", bom_byte)
          }
        }
      }
      sequence("array4") {
        array("array4", length(4), irregular) {
          switch(n => if (n.index > 0) stringValue(n / -1 / (n.index - 1) / "tag") else "*") {
            when("99") {
              sequence("dummy") {}
        	}
            when("*") {
              sequence("foo") {
                number("tag", bom_ubyte)
              }
        	}
          }
        }
      }
      array("unbounded array", unbounded, irregular) {
        sequence("chunk") {
          number("type", bom_ubyte)
          switch(n => stringValue(n / -1 / "type")) {
            when("1") {
              blob("blob1", n => 1 * 8)
            }
            when("2") {
              blob("blob2",n => 2 * 8)
            }
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
      0x06, 0x06, 0x06, 0x06, 0x06, 0x66,
      // array4
      0x0c, 0x63, 0x0d,
      // unbounded array
      0x02, 0xCC, 0xCC,
      0x02, 0xCC, 0xCC,
      0x01, 0xDD,
      0x02, 0xCC, 0xCC
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
  
  test("bizarre array") {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    assert(value(doc/"array4"/"array4"/0/"tag") == 0x0c)
    assert(value(doc/"array4"/"array4"/1/"tag") == 99)
    assert(doc/"array4"/"array4"/2/"tag" == BOMNil)
    assert(value(doc/"array4"/"array4"/2/"tag") == null)
    assert(value(doc/"array4"/"array4"/3/"tag") == 0x0d)
  }

  test("unbounded array") {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    assert((doc/"unbounded array").length == 4)
    assert((doc/"unbounded array").size == 11 * 8)
  }
  
}
