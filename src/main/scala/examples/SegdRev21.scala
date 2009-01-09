package examples

import bom.schema._
import bom.types._
import bom.types.ByteOrder._

object SegdRev21  extends BOMSchemaBuilder with BOMTypes {

  def segdFormat =
    definition(BIG_ENDIAN) {
      sequence("segd") {
        reference(storageUnitLabel)
        reference(generalHeader)
      }
    }

  def storageUnitLabel =
    typedef {
      string("sequenceNumber", "utf-8", "4")
      string("segdRevision", "utf-8", "5")
    }

  def generalHeader =
    typedef {
    }

}
