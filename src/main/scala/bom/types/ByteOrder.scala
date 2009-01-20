package bom.types

abstract class ByteOrder

object ByteOrder {

    case object BIG_ENDIAN extends ByteOrder {
      override def toString: String = "big-endian"
    }

    case object LITTLE_ENDIAN extends ByteOrder {
      override def toString: String = "little-endian"
    }

}
