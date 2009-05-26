package bom.bin

import java.io._

class FileBinarySpace(val file: File) extends AbstractBinarySpace {

  private val raf: RandomAccessFile = new RandomAccessFile(file, "r")
  
  def size: Long = raf.length * 8

  def capacity: Long = size

  def position: Long = raf.getFilePointer * 8 + offset

  def position(position: Long) {
    raf.seek((position / 8).intValue)
    offset = position % 8
  }

  def getByte: Byte = raf.readByte

  def getBytes(bytes: Array[Byte]) = {
    for (i <- 0 until bytes.length) {
      bytes(i) = getByte
    }
  }

  protected[this] def bytePosition: Long = raf.getFilePointer

}
