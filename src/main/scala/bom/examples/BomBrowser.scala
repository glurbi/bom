package bom.examples

import java.io._
import javax.swing._

import bom.bin._
import bom.schema._
import bom.types._
import bom.stream._
import bom.ui._

object BomBrowser {

  def main(args: Array[String]) = {

    val schema = Class.forName(args(0)).getMethod("schema").
      invoke(null, null).asInstanceOf[BOMSchemaElement]
    val bspace = new MemoryBinarySpace(new FileInputStream(args(1)))
    val doc = new BOMDocument(schema, bspace)
    val frame = new JFrame
    frame.add(new BOMDataTreePanel(doc))
    frame.setTitle("BOM Browser")
    frame.setSize(800, 600)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.show
  }

}
