package bom.test

import java.io._
import org.scalatest._

import bom.bin._

class FileBinarySpaceTestSuite extends FunSuite {

  def createBinarySpace: FileBinarySpace = {
    implicit def int2Byte(i: Int): Byte = i.asInstanceOf[Byte]
    val bytes: Array[Byte] = List[Byte](
      0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
      0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
      0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17
    ).toArray
    val file = File.createTempFile("bom-", ".bin");
    file.deleteOnExit
    val fos = new FileOutputStream(file)
    fos.write(bytes)
    fos.close
    new FileBinarySpace(file)
  }

  test("BinarySpace methods used within conditions") {
    val bspace = createBinarySpace
    assert(bspace.size == 24 * 8)
    assert(bspace.capacity == 24 * 8)
    assert(bspace.position == 0)
    bspace.position(8 * 8)
    assert(bspace.position == 8 * 8)
    assert(bspace.getByte == 8)
    assert(bspace.getByte == 9)
    assert(bspace.position == 10 * 8)
    assert(bspace.getBit == 0)
    assert(bspace.position == 10 * 8 + 1)
    assert(bspace.getBit == 0)
    assert(bspace.getBit == 0)
    assert(bspace.getBit == 0)
    assert(bspace.getBit == 1)
    assert(bspace.getBit == 0)
    assert(bspace.getBit == 1)
    assert(bspace.getBit == 0)
    assert(bspace.position == 11 * 8)
    assert(bspace.getBits(4) == 0)
    assert(bspace.getBits(4) == 11)
    val bytes = new Array[Byte](6);
    bspace.getBytes(bytes)
    assert(bytes(5) == 0x11)
    assert(bspace.position == 18 * 8)
  }

}
