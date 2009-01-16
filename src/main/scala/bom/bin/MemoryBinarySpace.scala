package bom.bin

import java.io._
import java.nio._
import bom._

class MemoryBinarySpace(val buffer: ByteBuffer) extends BOMBinarySpace {

  var offset: Long = 0
  var current: Byte = 0
  
  def this(size: Int) {
    this(ByteBuffer.allocateDirect(size))
  }
  
  def this(bytes: Array[Byte]) {
    this(ByteBuffer.allocateDirect(bytes.size))
    buffer.put(bytes)
  }
  
  def this(is: InputStream) {
    this(is.available)
    val bytes = new Array[Byte](is.available)
    var len = 0
    while (is.available > 0) {
      len += is.read(bytes, len, bytes.length - len)
    }
    buffer.put(bytes, 0, len)
  }
  
  def size: Long = buffer.capacity * 8

  def capacity: Long = buffer.capacity * 8

  def position: Long = buffer.position * 8

  def position(position: Long) = buffer.position((position / 8).intValue)
  
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

  def getByte: byte = {
    if (offset != 0) {
      throw new BOMException("Bad alignment...")
    }
    buffer.get
  }

  def getBytes(bytes: Array[byte]) = {
    if (offset != 0) {
      throw new BOMException("Bad alignment...")
    }
    buffer.get(bytes)
  }

  def getBit: Int = {
    if (offset == 0) {
      current = getByte
    }
    var bit = (current >> (7 - offset)) & 1
    offset = (offset + 1) % 8
    bit
  }
  
  def getBits(count: Int): Int = {
    var result = 0;
    for (i <- 0 until count) {
      result = (result << 1) | getBit
    }
    result
  }

}
