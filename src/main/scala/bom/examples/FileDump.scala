package bom.examples

import java.io._
import bom.bin._
import bom.schema._
import bom.types._
import bom.stream._

object FileDump {

  def main(args: Array[String]) = {
    BOMDumper.dump(
      Class.forName(args(0)).getMethod("schema").invoke(null, null).asInstanceOf[BOMSchemaElement],
      new MemoryBinarySpace(new FileInputStream(args(1))))
  }

}
