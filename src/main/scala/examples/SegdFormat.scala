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
        array("scan_type_headers", "../general_header_1/scan_type_per_record") {
          array("channel_set_headers", "../../general_header_1/channel_sets_per_scan_type") {
            reference(scanTypeHeader)
          }
        }
        reference(extendedHeader)
        reference(externalHeader)
        array("data", "../general_header_1/scan_type_per_record") {
          array("scan type", "../../general_header_1/channel_sets_per_scan_type") {
            array("channel set", "../../../scan_type_headers/channel_set_headers[1]/channel_set_header[1]/number_of_channels", true) {
              reference(trace)
            }
          }
        }
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
        blob("general constants", 6)
        number("year", bom_bcd2)
        number("general header blocks", bom_bcd1)
        number("day", bom_bcd3)
        number("hour", bom_bcd2)
        number("minute", bom_bcd2)
        number("second", bom_bcd2)
        number("manufacture's code", bom_bcd2)
        number("manufacture's serial number", bom_bcd4)
        blob("not used", 3)
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
        blob("undefined", 9)
        number("general header block number", bom_bcd2)
        blob("undefined", 13)
      }
    }

  def scanTypeHeader =
    typedef {
      sequence("channel_set_header") {
        number("scan type number", bom_bcd2)
        number("channel set number", bom_bcd2)
        number("start time", bom_ushort)
        number("end time", bom_ushort)
        number("optional", bom_ubyte)
        number("input voltage", bom_ubyte)
        number("number_of_channels", bom_bcd4)
        number("channel type", bom_ubyte)
        number("sc", bom_ubyte)
        blob("undefined", 20)
      }
    }

  def extendedHeader =
    typedef {
      blob("extended header", "32 * ../general_header_2/extended_header_blocks")
    }

  def externalHeader =
    typedef {
      blob("external header", "32 * ../general_header_2/external_header_blocks")
    }

  def trace =
    typedef {
      sequence("trace") {
        reference(traceHeader)
        array("trace header extensions", "../trace_header/trace_header_extensions", true) {
          reference(traceHeaderExtension)
        }
        reference(traceData)
      }
    }

  def traceHeader =
    typedef {
      sequence("trace_header") {
        number("file number", bom_bcd4)
        number("scan type number", bom_bcd2)
        number("channel set number", bom_bcd2)
        number("trace number", bom_bcd4)
        blob("first timing word", 3)
        number("trace_header_extensions", bom_byte)
        blob("pad", 10)
      }
    }

  def traceHeaderExtension =
    typedef {
      blob("trace header extension", 32)
    }

  def traceData =
    typedef {
      blob("trace data", 15000)
    }

}
