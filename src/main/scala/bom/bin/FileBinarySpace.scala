package bom.bin

import java.io._
import java.nio._

class FileBinarySpace(private val file: File, bufSize: Int) extends AbstractBinarySpace {

  def this(file: File) = this(file, 4096)
  
  private val raf = new RandomAccessFile(file, "r")
  private val fc = raf.getChannel
  private val bb = ByteBuffer.allocateDirect(bufSize)
  
  private var bbPos = 0L
  
  def size: Long = raf.length * 8

  def capacity: Long = size

  def position: Long = bbPos * 8 + bb.position * 8 + offset

  def position(pos: Long) {
    if (pos != position) {
      bb.clear
      fc.position((pos / 8).intValue)
      bbPos = fc.position
      offset = pos % 8
      bb.clear
      fc.read(bb)
      bb.flip
    }
  }

  def getByte: Byte = {
    if (bb.position == bb.limit) {
      bbPos = fc.position
      bb.clear
      fc.read(bb)
      bb.flip
    }
    bb.get
  }

  def getBytes(bytes: Array[Byte]) = {
    for (i <- 0 until bytes.length) {
      bytes(i) = getByte
    }
  }

  protected[this] def bytePosition: Long = position / 8

}
