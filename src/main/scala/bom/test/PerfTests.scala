package bom.test

import java.io._
import bom.bin._
import bom.schema._
import bom.stream._

object PerfTests {

  def main(args: Array[String]) = {
    val is = getClass.getResourceAsStream("/java/lang/String.class")
    val binarySpace = new MemoryBinarySpace(is)
    val definition = BomJavaClassFormat.classDefinition
    
    while (true) {
      val reader = new BOMEventReader(binarySpace, definition)
      var count = 0
      while (reader.hasNext) {
        count += 1
        reader.nextEvent
      }
    }
    
  }
  
}
