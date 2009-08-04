package bom.examples.schemas

import bom.schema._
import bom.types._
import bom.BOM._

/**
 * Binary product can be downloaded freely from:
 * https://www.class.ncdc.noaa.gov/
 *
 * General description of the satellite instrument and data format:
 * http://www2.ncdc.noaa.gov/docs/klm/index.htm
 */
object AvhrrHrptSchema extends Schema with SchemaBuilder {

  def schema = document("hrpt") {
      ars
      header
      array("records", length((n: BOMNode) => longValue(n.document / "header" / "recordCount")), regular) {
        record
      }
    }

  /**
   * http://www2.ncdc.noaa.gov/docs/klm/html/c8/s831-2.htm
   */
  def ars =
    sequence("ars") {
      string("COST number", "us-ascii", byteSize(6))
      string("CLASS number", "us-ascii", byteSize(8))
      string("Order creation date", "us-ascii", byteSize(4))
      string("Order creation day", "us-ascii", byteSize(3))
      string("Processing site code", "us-ascii", byteSize(1))
      string("Processing Software ID", "us-ascii", byteSize(8))
      string("Data set name", "us-ascii", byteSize(42))
      blob("blank", byteSize(2))
      string("Select Flag", "us-ascii", byteSize(1))
      string("Beginning Latitude", "us-ascii", byteSize(3))
      string("Ending Latitude", "us-ascii", byteSize(3))
      string("Beginning Longitude", "us-ascii", byteSize(4))
      string("Ending Longitude", "us-ascii", byteSize(4))
      string("Start Hour", "us-ascii", byteSize(2))
      string("Start Minute", "us-ascii", byteSize(2))
      string("Number of minute", "us-ascii", byteSize(3))
      string("Appended data flag", "us-ascii", byteSize(1))
      array("Channel Select Flags", length(20)) {
        string("flag", "us-ascii", byteSize(1))
      }
      string("Sensor Data Word Size", "us-ascii", byteSize(2))
      blob("blank", byteSize(27))
      string("Ascend/Descend Flag", "us-ascii", byteSize(1))
      string("First Latitude", "us-ascii", byteSize(3))
      string("Last Latitude", "us-ascii", byteSize(3))
      string("First Longitude", "us-ascii", byteSize(4))
      string("Last Longitude", "us-ascii", byteSize(4))
      string("Data Format", "us-ascii", byteSize(20))
      string("Size of Records", "us-ascii", byteSize(6))
      string("Number of Records", "us-ascii", byteSize(6))
      blob("filler", byteSize(319))
    }

  /**
   * http://www2.ncdc.noaa.gov/docs/klm/html/c8/s83132-2.htm
   */
  def header =
    sequence("header") {
      blob("tmp", byteSize(128))
      number("recordCount", bom_ushort)
      number("earthLocatedScanLines", bom_ushort)
      number("missingScanLines", bom_ushort)
      blob("tmp", byteSize(15738))
    }

  /**
   * http://www2.ncdc.noaa.gov/docs/klm/html/c8/s83133-2.htm
   */
  def record =
    sequence("record") {
      sequence("Scan Line Information") {
        number("scanLineNumber", bom_ushort)
        blob("undefined", byteSize(22))
      }
      sequence("Quality indicators") {
        number("do not use scan", bom_boolean)
        number("time sequence error detected", bom_boolean)
        number("data gap precedes this scan", bom_boolean)
        number("insufficient data for calibration", bom_boolean)
        number("earth location data not available", bom_boolean)
        number("first good time following a clock update", bom_boolean)
        number("instrument status changed with this scan", bom_boolean)
        number("sync lock dropped during this frame", bom_boolean)
        number("frame sync word has errors", bom_boolean)
        number("frame sync returned to lock", bom_boolean)
        number("frame sync word not valid", bom_boolean)
        number("bit slippage occurred during this frame", bom_boolean)
        blob("blank", bitSize(11))
        number("TIP parity error detected", bom_boolean)
        number("reflected sunlight detected ch 3B", bom_bitint2)
        number("reflected sunlight detected ch 4", bom_bitint2)
        number("reflected sunlight detected ch 5", bom_bitint2)
        number("resync occurred on this frame ", bom_boolean)
        number("pseudonoise occurred on this frame ", bom_boolean)
        blob("undefined", byteSize(20))
      }
      sequence("Calibration coefficient") {
        blob("undefined", byteSize(264))
      }
      sequence("Navigation") {
        blob("undefined", byteSize(744))
      }
      sequence("Frame telemetry") {
        blob("undefined", byteSize(208))
      }
      sequence("Earth Observations") {
        array("data", length(2048), regular) {
          size(byteSize(3414 * 4))
          sequence("channels") {
            position(n => (n.index /  3, n.index % 3) match {
              case (dividend: Int, 0) => dividend * 160 + n.parent.position
              case (dividend: Int, 1) => dividend * 160 + 2 + 10 + 10 + 10 + 2 + 10 + 10 + n.parent.position
              case (dividend: Int, 2) => dividend * 160 + 2 + 10 + 10 + 10 + 2 + 10 + 10 + 10 + 2 + 10 + 10 + 10 + 2 + 10 + n.parent.position
            })
            number("channel1", bom_bitint10) ( () => {
              position(n => (n.parent.index %  3) match {
                case 0 => 2 + n.parent.position
                case 1 => n.parent.position
                case 2 => n.parent.position
              })
            })
            number("channel2", bom_bitint10) ( () => {
              position(n => (n.parent.index %  3) match {
                case 0 => 2 + 10 + n.parent.position
                case 1 => 10 + 2 + n.parent.position
                case 2 => 10 + n.parent.position
              })
            })
            number("channel3", bom_bitint10) ( () => {
              position(n => (n.parent.index %  3) match {
                case 0 => 2 + 10 + 10 + n.parent.position
                case 1 => 10 + 2 + 10 + n.parent.position
                case 2 => 10 + 10 + 2 + n.parent.position
              })
            })
            number("channel4", bom_bitint10) ( () => {
              position(n => (n.parent.index %  3) match {
                case 0 => 2 + 10 + 10 + 10 + 2 + n.parent.position
                case 1 => 10 + 2 + 10 + 10 + n.parent.position
                case 2 => 10 + 10 + 2 + 10 + n.parent.position
              })
            })
            number("channel5", bom_bitint10) ( () => {
              position(n => (n.parent.index % 3) match {
                case 0 => 2 + 10 + 10 + 10 + 2 + 10 + n.parent.position
                case 1 => 10 + 2 + 10 + 10 + 10 + 2 + n.parent.position
                case 2 => 10 + 10 + 2 + 10 + 10 + n.parent.position
              })
            })
          }
        }
        blob("undefined", byteSize(8))
      }
      sequence("Digital B Housekeeping Telemetry") {
        blob("undefined", byteSize(16))
      }
      sequence("Analog Housekeeping Telemetry") {
        blob("undefined", byteSize(32))
      }
      sequence("Clouds From Avhrr") {
        blob("undefined", byteSize(896))
      }
    }

}
