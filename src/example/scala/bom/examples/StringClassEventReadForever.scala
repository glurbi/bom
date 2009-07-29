package bom.examples

import bom.bin._
import bom.stream._

object StringClassEventReadForever {
  
  def main(args : Array[String]) : Unit = {
    while (true) {
      val bspace = new MemoryBinarySpace(getClass.getResourceAsStream("/java/lang/String.class"))
      val schema = bom.examples.schemas.JavaClassSchema.schema
      val reader = new EventReader(bspace, schema)
      while (reader.hasNext) {
        reader.nextEvent
      }
    }
  }
  
}
