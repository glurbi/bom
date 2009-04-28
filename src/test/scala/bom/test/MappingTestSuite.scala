package bom.test

import org.scalatest._

import bom.BOM._
import bom.schema._
import bom.types._
import bom.bin._

class MappingTestSuite extends FunSuite {

  object TestSchema extends Schema with SchemaBuilder {
    def schema = document("mappings") {
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
  }

  def bspace: BinarySpace = {
    implicit def int2Byte(i: Int): Byte = i.asInstanceOf[Byte]
    val bytes: Array[Byte] = List[Byte](
      0x00, 0x03
    ).toArray
    new MemoryBinarySpace(bytes)
  }

  test("numbers with a mapping definition") {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    val mapped_nb1 = (doc/"mapped_nb1").asInstanceOf[BOMNumber]
    assert(mapped_nb1.schema.mappedValue(mapped_nb1.value) == "UNKNOWN")
    val mapped_nb2 = (doc/"mapped_nb2").asInstanceOf[BOMNumber]
    assert(mapped_nb2.schema.mappedValue(mapped_nb2.value) == "THREE")
  }

}
