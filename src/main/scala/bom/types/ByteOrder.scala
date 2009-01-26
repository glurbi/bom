package bom.types

abstract class ByteOrder

// TODO: remove this object, it can be handled per type, thus not enforcing endianness on an entire schema
object ByteOrder {

    case object BIG_ENDIAN extends ByteOrder {
      override def toString: String = "big-endian"
    }

    case object LITTLE_ENDIAN extends ByteOrder {
      override def toString: String = "little-endian"
    }

}
