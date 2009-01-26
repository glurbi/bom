package bom.stream

import java.io._
import java.util._

import bom.bin._
import bom.schema._
import bom.stream._
import bom.stream.BOMEvent._
import bom.types._

object BOMDumper {

  def dump(schema: BOMSchemaElement, bspace: BOMBinarySpace) = {
    val reader = new BOMEventReader(bspace, schema)
    while (reader.hasNext) {
      reader.nextEvent match {
        case BOMEvent(d: BOMDocument, _) =>
        case BOMEvent(c: BOMContainer, StartContainer) => dumpStartContainer(c)
        case BOMEvent(n: BOMNumber, _) => dumpNumber(n)
        case BOMEvent(b: BOMBlob, _) => dumpBlob(b)
        case BOMEvent(l: BOMLeaf, Leaf) => dumpLeaf(l)
        case BOMEvent(_, BOMEvent.EndContainer) =>
      }
    }
    println
  }

  private def dumpCommon(node: BOMNode) = {
    println
    format("%08x", node.position / 8)
    format("%s", "              ".substring(0, node.depth))
    format("%s", node.name)
  }
  
  private def dumpStartContainer(container: BOMContainer) = {
    dumpCommon(container)
    container.parent match {
      case (a: BOMArray) => format("[%d]", container.index)
      case _ =>
    }
  }

  private def dumpLeaf(leaf: BOMLeaf) = {
    dumpCommon(leaf)
    format(" %s", leaf.value)
  }

  private def dumpNumber(number: BOMNumber) = {
    dumpLeaf(number)
    if (number.schema.hasMapping) {
      format(" (%s)", number.schema.mappedValue(number.value))
    }
    if (number.schema.hasMasks) {
      format(" [ ")
      val masks = number.schema.getMasks(number.value.longValue)
      val it = masks.iterator
      while (it.hasNext) {
        format("%s ", it.next)
      }
      format("]")
    }
  }

  private def dumpBlob(blob: BOMBlob) = {
    dumpCommon(blob)
    format(" (%d bytes)", blob.byteCount)
    for (i <- 0 until Math.min(blob.byteCount, 20).intValue) {
      format(" %02x", blob.value(i))
    }
    if (blob.byteCount > 20) {
      format(" ...")
    }
  }

}
