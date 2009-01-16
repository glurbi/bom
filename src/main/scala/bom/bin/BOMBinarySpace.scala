package bom.bin

/**
 * A binary space defines a region (memory, disk, network, ...) where data can
 * be read or written with a set of primitives.
 */
trait BOMBinarySpace {

  /**
   * @return the size of this binary space (in bits)
   */
  def size: Long

  /**
   * @return the capacity of this binary space (in bits)
   */
  def capacity: Long

  /**
   * @return the current cursor position (in bits)
   */
  def position: Long

  /**
   * Set the cursor position (in bits)
   */
  def position(position: Long)

  /**
   * Reads and returns a byte at the current cursor position. The cursor
   * position is incremented by 8.
   *
   * @return a byte
   */
  def getByte: Byte

  /**
   * Reads a number of bytes at the current cursor position and stores them in
   * the array passed in parameter. The cursor position is incremented by the
   * size in bits of the array.
   */
  def getBytes(bytes: Array[Byte])

  /**
   * Reads and returns the bit at the current cursor position. The cursor
   * position is incremented by 1.
   *
   * @return the bit read in the lowest bit of the Int returned
   */
  def getBit: Int

  /**
   * Reads a number of bits at the current cursor position.
   * The cursor position is incremented by the number of bits read.
   * It is not allowed to read mode than 8 bits.
   *
   * @return the bits read in the lowest bits of the Int returned
   */
  def getBits(count: Int): Int

}
