package bom.test

import org.scalatest._

import bom.BOM._
import bom.schema._
import bom.types._
import bom.bin._

class StringTestSuite extends FunSuite {

  object TestSchema extends Schema with SchemaBuilder {
    def schema = document("strings") {
      string("greetings", "UTF-8", byteSize(11))
    }
  }

  def bspace: BinarySpace = {
    implicit def int2Byte(i: Int): Byte = i.asInstanceOf[Byte]
    val bytes: Array[Byte] = List[Byte](
      0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x2C, 0x20, 0x42, 0x4F, 0x4D, 0x21
    ).toArray
    new MemoryBinarySpace(bytes)
  }

  test("UTF-8 string") {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    assert(value(doc/"greetings") == "Hello, BOM!")
  }

}
