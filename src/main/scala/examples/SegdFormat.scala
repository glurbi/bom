package examples

import bom.schema._
import bom.types._
import bom.types.ByteOrder._

object SegdFormat extends BOMSchemaBuilder with BOMTypes {

  def segd =
    definition(BIG_ENDIAN) {
      sequence("segd") {
        reference(generalHeader1)
        reference(generalHeader2)
        array("scan type headers", "../general_header_1/channel_sets_per_scan_type * ../general_header_1/scan_type_per_record") {
          reference(scanTypeHeader)
        }
        reference(extendedHeader)
        reference(externalHeader)
        reference(trace)
      }
    }

  def storageUnitLabel =
    typedef {
      sequence("storage unit label") {
        string("segd revision", "utf-8", "10")
        string("sequence number", "utf-8", "4")
      }
    }

  def generalHeader1 =
    typedef {
      sequence("general_header_1") {
        number("file number", bom_bcd4)
        number("format code", bom_bcd4)
        array("general constants", "6") {
          number("general constant", bom_bcd2)
        }
        number("year", bom_bcd2)
        number("general header blocks", bom_bcd1)
        number("day", bom_bcd3)
        number("hour", bom_bcd2)
        number("minute", bom_bcd2)
        number("second", bom_bcd2)
        number("manufacture's code", bom_bcd2)
        number("manufacture's serial number", bom_bcd4)
        number("not used", bom_byte)
        number("not used", bom_byte)
        number("not used", bom_byte)
        number("base scan interval", bom_byte)
        number("polarity", bom_byte)
        number("not used", bom_byte)
        number("record type", bom_byte)
        number("record length", bom_byte)
        number("scan_type_per_record", bom_bcd2)
        number("channel_sets_per_scan_type", bom_bcd2)
        number("skew", bom_bcd2)
        number("extended header length", bom_bcd2)
        number("external header length", bom_bcd2)
      }
    }

  def generalHeader2 =
    typedef {
      sequence("general_header_2") {
        number("extended file number", bom_bcd6)
        number("extended channel set", bom_ushort)
        number("extended_header_blocks", bom_ushort)
        number("external_header_blocks", bom_ushort)
        array("general header 2", "9") {
            number("value", bom_byte)
        }
        number("general header block number", bom_bcd2)
        array("general header 2", "13") {
            number("value", bom_byte)
        }
      }
    }

  def scanTypeHeader =
    typedef {
      sequence("scan type header") {
        number("scan type number", bom_bcd2)
        number("channel set number", bom_bcd2)
        number("start time", bom_ushort)
        number("end time", bom_ushort)
        number("optional", bom_ubyte)
        number("input voltage", bom_ubyte)
        number("number of channels", bom_bcd4)
        number("channel type", bom_ubyte)
        number("sc", bom_ubyte)
        array("scan type header", "20") {
            number("value", bom_byte)
        }
      }
    }

  def extendedHeader =
    typedef {
      array("extended header", "32 * ../general_header_2/extended_header_blocks", true) {
        number("value", bom_byte)
      }
    }

  def externalHeader =
    typedef {
      array("external header", "32 * ../general_header_2/external_header_blocks", true) {
        number("value", bom_byte)
      }
    }

  def trace =
    typedef {
      sequence("trace") {
        reference(traceHeader)
      }
    }

  def traceHeader =
    typedef {
      sequence("traceHeader") {
        number("file number", bom_bcd4)
        number("scan type number", bom_bcd2)
        number("channel set number", bom_bcd2)
        number("trace number", bom_bcd4)
      }
    }

}
