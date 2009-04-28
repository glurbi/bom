package bom.test

import org.scalatest._

import bom.BOM._
import bom.schema._
import bom.types._
import bom.bin._

class OverrideTestSuite extends FunSuite {

  object TestSchema extends Schema with SchemaBuilder {
    def schema = document("overrides") {
      size(byteSize(6))
      number("n1", bom_ubyte) (() => {
        position(n => n.parent.position + 8)
      })
      array("a", length(3)) {
        size(byteSize(4))
        number("n2", bom_ubyte)
      }
    }
  }

  def bspace: BinarySpace = {
    implicit def int2Byte(i: Int): Byte = i.asInstanceOf[Byte]
    val bytes: Array[Byte] = List[Byte](
      0x12, 0x34, 0x01, 0x02, 0x03, 0xFF
    ).toArray
    new MemoryBinarySpace(bytes)
  }

  test("override size and position") {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    assert(position(doc/"n1") == position(doc) + 8)
    assert(value(doc/"n1") == 0x34)
    assert(size(doc) == 6 * 8)
    assert(value(doc/"a"/0) == 0x01)
    assert(value(doc/"a"/1) == 0x02)
    assert(value(doc/"a"/2) == 0x03)
    assert(size(doc/"a") == 4 * 8)
  }

}
