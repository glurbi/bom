package bom.bin

import java.io._
import java.nio._
import bom._

class MemoryBinarySpace(val buffer: ByteBuffer) extends AbstractBinarySpace {

  def this(size: Int) {
    this(ByteBuffer.allocateDirect(size))
  }
  
  def this(bytes: Array[Byte]) {
    this(ByteBuffer.allocateDirect(bytes.size))
    buffer.put(bytes)
    buffer.flip
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

  def position(pos: Long) = {
    buffer.position((pos / 8).intValue)
    offset = pos % 8
  }
  
  def getByte: Byte = buffer.get

  def getBytes(bytes: Array[byte]) = buffer.get(bytes)

  protected[this] def bytePosition: Long = buffer.position
  
}
