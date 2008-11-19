package bom.bin

import java.io._
import java.nio._
import bom._

class MemoryBinarySpace(val buffer: ByteBuffer) extends BOMBinarySpace {

  def this(size: int) {
    this(ByteBuffer.allocateDirect(size))
  }
  
  def this(bytes: Array[byte]) {
    this(ByteBuffer.allocateDirect(bytes.size))
    buffer.put(bytes)
  }
  
  def this(is: InputStream) {
    this(is.available)
    val bytes = new Array[byte](is.available)
    var len = 0
    while (is.available > 0) {
      len += is.read(bytes, len, bytes.length - len)
    }
    buffer.put(bytes, 0, len)
  }
  
  def spaceSize: int = buffer.capacity

  def capacity: int = buffer.capacity

  def position: int = buffer.position

  def position(position: int) = buffer.position(position)
  
  def byteOrder: ByteOrder = {
    if (buffer.order == java.nio.ByteOrder.BIG_ENDIAN) {
      ByteOrder.BIG_ENDIAN;
    } else {
      ByteOrder.LITTLE_ENDIAN;
    }
  }
  def byteOrder(byteOrder: ByteOrder) = {
    if (byteOrder == ByteOrder.BIG_ENDIAN) {
      buffer.order(java.nio.ByteOrder.BIG_ENDIAN);
    } else {
      buffer.order(java.nio.ByteOrder.LITTLE_ENDIAN);
    }
  }

  def getByte: byte = buffer.get
  def getBytes(bytes: Array[byte]) = buffer.get(bytes)

}
