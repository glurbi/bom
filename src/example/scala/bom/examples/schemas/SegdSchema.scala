package bom.examples.schemas

import bom.schema._
import bom.types._
import bom.BOM._

/**
 * http://www.seg.org/SEGportalWEBproject/prod/SEG-Publications/Pub-Technical-Standards/Documents/seg_d_rev2.1.pdf
 */
object SegdSchema extends Schema with SchemaBuilder {

  def schema = document("segd") {
      generalHeader1
      generalHeader2
      array("scan_type_headers", length(_ / -1 / "general_header_1" / "scan_type_per_record")) {
        array("channel_set_headers", length(_ / -1 / -1 / "general_header_1" / "channel_sets_per_scan_type")) {
          channelSetHeader
        }
      }
      extendedHeader
      externalHeader
      array("data", length(_.document / "general_header_1" / "scan_type_per_record")) {
        array("scan type", length(_.document / "general_header_1" / "channel_sets_per_scan_type")) {
          array("channel set", length(n => n.document / "scan_type_headers" / (n / -1).index / n.index / "number_of_channels"), regular) {
            trace
          }
        }
      }
    }

  def storageUnitLabel =
    sequence("storage unit label") {
      string("segd revision", "utf-8", byteSize(10))
      string("sequence number", "utf-8", byteSize(4))
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
    blob("extended header", byteSize(n => 32 * (n / -1 / "general_header_2" / "extended_header_blocks")))

  def externalHeader =
    blob("external header", byteSize(n => 32 * (n / -1 / "general_header_2" / "external_header_blocks")))

  def trace =
    sequence("trace") {
      traceHeader
      array("trace header extensions", length(_ / -1 / "trace_header" / "trace_header_extensions"), regular) {
        traceHeaderExtension
      }
      virtual("start_time", n => longValue(n.document / "scan_type_headers" / (n / -1 / -1 / -1).index / (n / -1 / -1).index / "start_time"))
      virtual("end_time", n => longValue(n.document / "scan_type_headers" / (n / -1 / -1 / -1).index / (n / -1 / -1).index / "end_time"))
      virtual("subscan", n => Math.pow(2, n.document / "scan_type_headers" / (n / -1 / -1 / -1).index / (n / -1 / -1).index / "sc"))
      virtual("trace_size", n => (doubleValue(n / -1 / "end_time") - doubleValue(n / -1 / "start_time")) * (n / -1 / "subscan") * 3)
      virtual("sample_count", n => (doubleValue(n / -1 / "end_time") - doubleValue(n / -1 / "start_time")) * (n / -1 / "subscan"))
      traceData
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
    array("trace data", length(_ / -1 / "sample_count"), regular) {
      number("sample", bom_int3)
    }

}
