package bom.test

import org.scalatest._

import bom.BOM._
import bom.schema._
import bom.types._
import bom.bin._

class BitFieldTestSuite extends FunSuite {

  object TestSchema extends Schema with SchemaBuilder {
    def schema = document("bitFields") {
        sequence("bitField") {
        number("bit15", bom_bitint1)
        number("bit14", bom_bitint1)
        number("bit13", bom_bitint1)
        number("bit12", bom_bitint1)
        blob("bitblob", bitSize(9))
        number("bit2", bom_bitint1)
        number("bit1and0", bom_bitint2)
      }
    }
  }

  def bspace: BinarySpace = {
    implicit def int2Byte(i: Int): Byte = i.asInstanceOf[Byte]
    val bytes: Array[Byte] = List[Byte](
      0xB0, 0x07 // bitfield: 1011 000000000 111
    ).toArray
    new MemoryBinarySpace(bytes)
  }

  test("a bit field with an internal mini blob") {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    assert(value(doc/"bitField"/"bit15") == 1)
    assert(value(doc/"bitField"/"bit14") == 0)
    assert(value(doc/"bitField"/"bit13") == 1)
    assert(value(doc/"bitField"/"bit13") == 1)
    assert(value(doc/"bitField"/"bit15") == 1)
    assert(value(doc/"bitField"/"bit14") == 0)
    assert(value(doc/"bitField"/"bit2") == 1)
    assert(value(doc/"bitField"/"bit1and0") == 3)
  }

}
