package bom.types

import bom.bin.BOMBinarySpace

trait BOMType {

    /**
     * @return the type name
     */
    def typeName: String

    /**
     * @return the type size (in bits)
     */
    def typeSize(params: Any*): Int

    /**
     * Reads one element of this type in the binary space.
     * The element is read at the current cursor position. The position of the
     * cursor in the binary space is incremented by the size of the type.
     *
     * @param bspace a binary space
     * @return the element read
     */
    def read(bspace: BOMBinarySpace, params: Any*): Any

}
