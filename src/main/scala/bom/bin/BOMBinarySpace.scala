package bom.bin

/**
 * A binary space defines a region (memory, disk, network, ...) where data can be
 * read or written with a set of primitives.
 */
trait BOMBinarySpace {

    /**
     * @return the size of this binary space in bytes
     */
    def spaceSize: int

    /**
     * @return the capacity of this binary space in bytes
     */
    def capacity: int;

    /**
     * @return the current cursor position in bytes
     */
    def position: int

    /**
     * Set the position of the cursor
     */
    def position(position: int)

    /**
     * Reads and returns a byte at the current cursor position. The cursor
     * position is incremented by 1.
     * 
     * @return a byte
     */
    def getByte: byte

    /**
     * Reads a number of bytes at the current cursor position and stores them in
     * the array passed in parameter. The cursor position is incremented by the
     * size in bytes of the array unless an exception has been raised.
     */
    def getBytes(bytes: Array[byte])

}
