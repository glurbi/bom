package bom.examples.schemas

import bom.schema._
import bom.types._
import bom.BOM._

/**
 * Format description:
 * http://en.wikipedia.org/wiki/Portable_Network_Graphics
 * http://www.w3.org/TR/PNG/
 */
object PngSchema extends Schema with SchemaBuilder {

  def schema = document("png") {
    signature
    array("chunks", unbounded, irregular) {
      chunk
    }
  }
  
  def signature =
    sequence("signature") {
	  blob("blob", byteSize(8))
    }
  
  def chunk =
    sequence("chunk") {
	  number("length", bom_uint)
      string("type", "utf-8", byteSize(4))
      switch(n => stringValue(n / -1 / "type")) {
        when("IHDR") {
          ihdrData
        }
        when("IEND") {
          blob("END OF CHUNKS",n => 0)
        }
        when("*") {
          blob("data",n => 8 * longValue(n / -1 / "length"))
        }
      }
	  number("crc", bom_uint)
	}

  def ihdrData =
    sequence("IHDR data") {
      number("width", bom_uint)
      number("height", bom_uint)
      number("bit depth", bom_ubyte)
      number("color type", bom_ubyte) {
        map {
          value(0, "Greyscale")
          value(2, "True Color")
          value(3, "Indexed Color")
          value(4, "Greyscale with alpha")
          value(6, "True Color with alpha")
          default("UNKNOWN")
        }            
      }
      number("compression method", bom_ubyte)
      number("filter method", bom_ubyte)
      number("interlace method", bom_ubyte)
    }

}
