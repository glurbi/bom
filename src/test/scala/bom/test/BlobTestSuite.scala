package bom.test

import org.scalatest._

import bom.BOM._
import bom.schema._
import bom.types._
import bom.bin._

class BlobTestSuite extends FunSuite {

  object TestSchema extends Schema with SchemaBuilder {
    def schema = document("blobs") {
      blob("blob1", byteSize(3))
      blob("blob2", bitSize(32))
    }
  }

  def bspace: BinarySpace = {
    implicit def int2Byte(i: Int): Byte = i.asInstanceOf[Byte]
    val bytes: Array[Byte] = List[Byte](
      0x01, 0x02, 0x03,
      0x01, 0x09, 0x07, 0x07
    ).toArray
    new MemoryBinarySpace(bytes)
  }

  test("can read from a simple sequence blobs") {
    val doc = new BOMDocument(TestSchema.schema, bspace)
    assert(size(doc/"blob1") == 3 * 8)
    assert(size(doc/"blob2") == 4 * 8)
    assert(value(doc/"blob2").asInstanceOf[Array[Byte]](0) == 1)
    assert(value(doc/"blob2").asInstanceOf[Array[Byte]](1) == 9)
    assert(value(doc/"blob2").asInstanceOf[Array[Byte]](2) == 7)
    assert(value(doc/"blob2").asInstanceOf[Array[Byte]](3) == 7)
  }

}
