package bom.test

import org.scalatest._

import bom.BOM._
import bom.schema._
import bom.types._
import bom.bin._

class VirtualTestSuite extends FunSuite {

  object TestSchema extends Schema with SchemaBuilder {
    def schema = document("virtuals") {
      number("a", bom_int)
      number("b", bom_int)
      virtual("v1", n => (longValue(n / -1 / "a") + longValue(n / -1 / "b")) * 2)
      virtual("v2", n => Math.pow(2, n / -1 / "a"))
    }
  }

  def bspace: BinarySpace = {
    implicit def int2Byte(i: Int): Byte = i.asInstanceOf[Byte]
    val bytes: Array[Byte] = List[Byte](
      0x00, 0x00, 0x00, 0x10,
      0x00, 0x00, 0x00, 0x02
    ).toArray
    new MemoryBinarySpace(bytes)
  }

  test("virtual numbers") {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    assert(value(doc/"v1") == 36)
    assert(value(doc/"v2") == 65536)
  }

}
