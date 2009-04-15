package bom.test

import org.scalatest._

import bom.BOM._
import bom.schema._
import bom.types._
import bom.bin._

class IntegerTestSuite extends FunSuite {

  object TestSchema extends Schema with SchemaBuilder with BOMTypes {
    def schema = document("integers") {
      number("i1", bom_byte)
      number("i2", bom_int)
      number("i3", bom_long)
      number("i4", bom_ubyte)
      number("i5", bom_uint)
      number("i6", bom_int)
      number("i7", bom_int3)
    }
  }

  def bspace: BinarySpace = {
    implicit def int2Byte(i: Int): Byte = i.asInstanceOf[Byte]
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

  test("can read from a simple sequence of various integers") {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    assert(value(doc/0) == 1.asInstanceOf[Byte])
    assert(value(doc/1) == 15)
    assert(value(doc/2) == 15L)
    assert(value(doc/3) == (0xFF).asInstanceOf[Short])
    assert(value(doc/4) == 0xFFFFL)
    assert(value(doc/5) == -1)
    assert(value(doc/6) == -1)
  }

}
