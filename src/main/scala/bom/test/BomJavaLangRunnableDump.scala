package bom.test

import java.io._
import bom.bin._
import bom.types._
import bom.schema._
import javax.xml.xpath._
import bom.stream._

object BomJavaLangRunnableDump {

  def main(args: Array[String]) = {

    val is = getClass.getResourceAsStream("/java/lang/Runnable.class")
    val binarySpace = new MemoryBinarySpace(is)
    val definition = BomJavaClassFormat.classDefinition
    
    BOMDumper.dump(definition, binarySpace)
  }
  
}
