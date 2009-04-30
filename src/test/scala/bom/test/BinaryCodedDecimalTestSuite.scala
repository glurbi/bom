package bom.test

import org.scalatest._

import bom.BOM._
import bom.schema._
import bom.types._
import bom.bin._

class BinaryCodedDecimalTestSuite extends FunSuite {

  object TestSchema extends Schema with SchemaBuilder {
    def schema = document("bcds") {
      number("bcd1", bom_bcd1)
      number("bcd2", bom_bcd2)
      number("bcd3", bom_bcd3)
      number("bcd4", bom_bcd4)
      number("bcd6", bom_bcd6)
      number("bcd8", bom_bcd8)
    }
  }

  def bspace: BinarySpace = {
    implicit def int2Byte(i: Int): Byte = i.asInstanceOf[Byte]
    val bytes: Array[Byte] = List[Byte](
      0x98, 0x76, 0x54,
      0x12, 0x34,
      0x12, 0x34, 0x56,
      0x12, 0x34, 0x56, 0x78
    ).toArray
    new MemoryBinarySpace(bytes)
  }

  test("binary coded decimal numbers") {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    assert(value(doc/0) == 9L)
    assert(value(doc/1) == 87L)
    assert(value(doc/"bcd3") == 654L)
    assert(value(doc/3) == 1234L)
    assert(value(doc/"bcd6") == 123456L)
    assert(value(doc/5) == 12345678L)
  }

}
