package bom.test

import org.scalatest._

import bom.BOM._
import bom.schema._
import bom.types._
import bom.bin._

class MaskTestSuite extends FunSuite {

  object TestSchema extends Schema with SchemaBuilder {
    def schema = document("masks") {
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
  }

  def bspace: BinarySpace = {
    implicit def int2Byte(i: Int): Byte = i.asInstanceOf[Byte]
    val bytes: Array[Byte] = List[Byte](
      0x00, 0x03,
      0x01, 0x10
    ).toArray
    new MemoryBinarySpace(bytes)
  }

  test("numbers with a mask definition") {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    val nb1 = (doc/"nb1").asInstanceOf[BOMNumber]
    assert(nb1.schema.getMasks(nb1.value.longValue).exists(_ == "BIT_ONE"))
    assert(nb1.schema.getMasks(nb1.value.longValue).exists(_ == "BIT_TWO"))
    assert(!nb1.schema.getMasks(nb1.value.longValue).exists(_ == "BIT_NINE"))
    assert(!nb1.schema.getMasks(nb1.value.longValue).exists(_ == "BIT_SEVENTEEN"))
    val nb2 = (doc/"nb2").asInstanceOf[BOMNumber]
    assert(!nb2.schema.getMasks(nb2.value.longValue).exists(_ == "BIT_ONE"))
    assert(!nb2.schema.getMasks(nb2.value.longValue).exists(_ == "BIT_TWO"))
    assert(nb2.schema.getMasks(nb2.value.longValue).exists(_ == "BIT_NINE"))
    assert(nb2.schema.getMasks(nb2.value.longValue).exists(_ == "BIT_SEVENTEEN"))
  }

}
