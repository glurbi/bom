package bom.test

import java.io._
import bom.bin._
import bom.schema._
import bom.examples._
import bom.stream._

object BomJavaLangRunnableTest {

  def main(args: Array[String]) = {

    val is = getClass.getResourceAsStream("/java/lang/Runnable.class")
    val binarySpace = new MemoryBinarySpace(is)
    val schema = JavaClassSchema.schema
    val doc = new BOMDocument(schema, binarySpace)
    val root = doc.rootNode
    
    assert(9 == doc.queryNumber(root, "/class/constant_pool_count"))
    assert(8 == doc.queryNumber(root, "count(/class/constant_pool/constant)"))
    assert(8 == doc.queryNumber(root, "/class/attributes/attribute[1]/sourcefile_index"))
    assert("Runnable.java" == doc.queryString(root,
      "/class/constant_pool/constant[8]/content/bytes"))

    val reader = new BOMEventReader(binarySpace, schema)
    var count = 0
    while (reader.hasNext) {
      count += 1
      reader.nextEvent
    }
    assert(count == 94)
    
    println(this.getClass.toString + " SUCCESS")
  }
  
}
