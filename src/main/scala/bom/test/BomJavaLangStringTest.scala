package bom.test

import java.io._
import bom.bin._
import bom.schema._
import bom.examples._
import bom.stream._

object BomJavaLangStringTest {

  def main(args: Array[String]) = {

    val is = getClass.getResourceAsStream("/java/lang/String.class")
    val binarySpace = new MemoryBinarySpace(is)
    val schema = JavaClassSchema.schema
    val doc = new BOMDocument(schema, binarySpace)
    val root = doc.rootNode

    assert(443 == doc.queryNumber(root, "/class/constant_pool_count"))
    assert(442 == doc.queryNumber(root, "count(/class/constant_pool/constant)"))
    assert(72 == doc.queryNumber(root,
      "/class/methods/method[4]/attributes/method_attribute/bytecode/code_length"))

    val reader = new BOMEventReader(binarySpace, schema)
    var count = 0
    while (reader.hasNext) {
      count += 1
      reader.nextEvent
    }
    assert(count == 13020)
    
    println(this.getClass.toString + " SUCCESS")
  }
  
}
