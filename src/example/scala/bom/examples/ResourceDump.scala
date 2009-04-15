package bom.examples

import java.io._
import bom.bin._
import bom.schema._
import bom.types._
import bom.stream._

object ResourceDump {

  def main(args: Array[String]) = {
    BOMDumper.dump(
      Class.forName(args(0)).getMethod("schema").invoke(null, null).asInstanceOf[SchemaElement],
      new MemoryBinarySpace(getClass.getResourceAsStream(args(1))))
  }

}
