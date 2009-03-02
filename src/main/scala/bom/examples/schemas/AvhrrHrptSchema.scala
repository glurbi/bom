package bom.examples.schemas

import bom.schema._
import bom.types._

/**
 * Binary product can be downloaded freely from:
 * https://www.class.ncdc.noaa.gov/
 *
 * General description of the satellite instrument and data format:
 * http://www2.ncdc.noaa.gov/docs/klm/index.htm
 */
object AvhrrHrptSchema extends BOMSchema with BOMSchemaBuilder with BOMTypes {

  def schema = document {
    hrpt
  }

  def hrpt =
    sequence("hrpt") {
      ars
      header
      array("records", "3") {
        record
      }
    }

  /**
   * http://www2.ncdc.noaa.gov/docs/klm/html/c8/s831-2.htm
   */
  def ars =
    sequence("ars") {
    }

  /**
   * http://www2.ncdc.noaa.gov/docs/klm/html/c8/s83132-2.htm
   */
  def header =
    sequence("header") {
    }

  /**
   * http://www2.ncdc.noaa.gov/docs/klm/html/c8/s83133-2.htm
   */
  def record =
    sequence("record") {
    }

}
