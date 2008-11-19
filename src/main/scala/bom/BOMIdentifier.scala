package bom

import java.util.Arrays

class BOMIdentifier(val rawID: Array[int]) {

  override def hashCode: int = Arrays.hashCode(rawID)

  override def equals(that: Any): boolean = that match {
    case other: BOMIdentifier
      => this.eq(other) || Arrays.equals(this.rawID, other.rawID)
    case _
      => false
  }

  override def toString: String = Arrays.toString(rawID)

}
