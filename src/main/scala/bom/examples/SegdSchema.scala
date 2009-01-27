package bom.examples

import bom.schema._
import bom.types._

object SegdSchema extends BOMSchema with BOMSchemaBuilder with BOMTypes {

  def schema = document {
    segd
  }

  def segd =
    sequence("segd") {
      reference(generalHeader1)
      reference(generalHeader2)
      array("scan_type_headers", "../general_header_1/scan_type_per_record") {
        array("channel_set_headers", "../../general_header_1/channel_sets_per_scan_type") {
          reference(channelSetHeader)
        }
      }
      reference(extendedHeader)
      reference(externalHeader)
      array("data", "/segd/general_header_1/scan_type_per_record") {
        array("scan type", "/segd/general_header_1/channel_sets_per_scan_type") {
          array("channel set", "/segd/scan_type_headers/channel_set_headers[bom:context()/../@index + 1]/channel_set_header[bom:context()/@index + 1]/number_of_channels", true) {
            reference(trace)
          }
        }
      }
    }

  def storageUnitLabel =
    sequence("storage unit label") {
      string("segd revision", "utf-8", byteSize("10"))
      string("sequence number", "utf-8", byteSize("4"))
    }

  def generalHeader1 =
    sequence("general_header_1") {
      number("file number", bom_bcd4)
      number("format code", bom_bcd4)
      blob("general constants", byteSize(6))
      number("year", bom_bcd2)
      number("general header blocks", bom_bcd1)
      number("day", bom_bcd3)
      number("hour", bom_bcd2)
      number("minute", bom_bcd2)
      number("second", bom_bcd2)
      number("manufacture's code", bom_bcd2)
      number("manufacture's serial number", bom_bcd4)
      blob("not used", byteSize(3))
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

  def generalHeader2 =
    sequence("general_header_2") {
      number("extended file number", bom_bcd6)
      number("extended channel set", bom_ushort)
      number("extended_header_blocks", bom_ushort)
      number("external_header_blocks", bom_ushort)
      blob("undefined", byteSize(9))
      number("general header block number", bom_bcd2)
      blob("undefined", byteSize(13))
    }

  def channelSetHeader =
    sequence("channel_set_header") {
      number("scan type number", bom_bcd2)
      number("channel set number", bom_bcd2)
      number("start_time", bom_ushort)
      number("end_time", bom_ushort)
      number("optional", bom_ubyte)
      number("input voltage", bom_ubyte)
      number("number_of_channels", bom_bcd4)
      number("channel type", bom_ubyte)
      number("sc", bom_bcd1)
      number("gain", bom_bcd1)
      blob("undefined", byteSize(20))
    }

  def extendedHeader =
    blob("extended header", byteSize("32 * ../general_header_2/extended_header_blocks"))

  def externalHeader =
    blob("external header", byteSize("32 * ../general_header_2/external_header_blocks"))

  def trace =
    sequence("trace") {
      reference(traceHeader)
      array("trace header extensions", "../trace_header/trace_header_extensions", true) {
        reference(traceHeaderExtension)
      }
      virtual("start_time", "/segd/scan_type_headers/channel_set_headers[bom:context()/../../../@index + 1]/channel_set_header[bom:context()/../../@index + 1]/start_time")
      virtual("end_time", "/segd/scan_type_headers/channel_set_headers[bom:context()/../../../@index + 1]/channel_set_header[bom:context()/../../@index + 1]/end_time")
      virtual("subscan", "bom:power(2, /segd/scan_type_headers/channel_set_headers[bom:context()/../../../@index + 1]/channel_set_header[bom:context()/../../@index + 1]/sc)")
      virtual("trace_size", "(../end_time - ../start_time) * ../subscan * 3")
      virtual("sample_count", "(../end_time - ../start_time) * ../subscan")
      reference(traceData)
    }

  def traceHeader =
    sequence("trace_header") {
      number("file number", bom_bcd4)
      number("scan type number", bom_bcd2)
      number("channel set number", bom_bcd2)
      number("trace number", bom_bcd4)
      blob("first timing word", byteSize(3))
      number("trace_header_extensions", bom_byte)
      blob("pad", byteSize(10))
    }

  def traceHeaderExtension =
    blob("trace header extension", byteSize(32))

  def traceData =
    array("trace data", "../sample_count", true) {
      number("sample", bom_int3)
    }

}
