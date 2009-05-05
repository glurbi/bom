package bom.test

import org.scalatest._

import bom.BOM._
import bom.schema._
import bom.types._
import bom.bin._

class SwitchTestSuite extends FunSuite {

  object TestSchema extends Schema with SchemaBuilder {
    def schema = document("switches") {
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
  }

  def bspace: BinarySpace = {
    implicit def int2Byte(i: Int): Byte = i.asInstanceOf[Byte]
    val bytes: Array[Byte] = List[Byte](
      0x02, 0x03, 0x46, 0x6F, 0x6F,
      0x01, 0xFF, 0xFF
    ).toArray
    new MemoryBinarySpace(bytes)
  }

  test("numbers with a mapping definition") {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    assert(value(doc/"array"/0/1/"string") == "Foo")
    assert(value(doc/"array"/1/1) == 65535)
  }

}
