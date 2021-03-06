package bom.types

import bom.bin._

trait BOMType {

    /**
     * @return the type size (in bits)
     */
    def size: Int

    /**
     * Reads one element of this type in the binary space.
     * The element is read at the current cursor position. The position of the
     * cursor in the binary space is incremented by the size of the type.
     *
     * @param bspace a binary space
     * @return the element read
     */
    def read(bspace: BinarySpace): Any

}
