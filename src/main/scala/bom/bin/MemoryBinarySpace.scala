package bom.bin

import java.io._
import java.nio._
import bom._

class MemoryBinarySpace(val buffer: ByteBuffer) extends BOMBinarySpace {

  var offset: Long = 0
  
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
  
  def size: Long = buffer.limit * 8

  def capacity: Long = buffer.capacity * 8

  def position: Long = buffer.position * 8 + offset

  def position(position: Long) = {
    buffer.position((position / 8).intValue)
    offset = position % 8
  }
  
  def getByte: Byte = {
    buffer.get
  }

  def getBytes(bytes: Array[byte]) = {
    buffer.get(bytes)
  }

  def getBit: Int = {
    val old = buffer.position * 8
    val bit = (getByte >> (7 - offset)) & 1
    offset = (offset + 1) % 8
    if (offset < 8) position(old + offset)
    if (offset == 0) position(old + 8)
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
