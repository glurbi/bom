package examples

import java.io._
import bom.bin._
import bom.types._
import bom.stream._

object SegdDump {

  def main(args: Array[String]) = {

    val is = new FileInputStream(args(0))
    val binarySpace = new MemoryBinarySpace(is)
    val definition = SegdFormat.segd

    BOMDumper.dump(definition, binarySpace)
  }

}
