package bom.stream

import java.io._
import java.util._
import org.w3c.dom._
import javax.xml.namespace._
import javax.xml.xpath._
import bom.dom._
import bom.schema._
import bom.bin._
import bom._
import bom.bin._
import bom.schema._
import bom.stream.BOMEvent._

class BOMEventReader(val bspace: BOMBinarySpace,
                     val definition: BOMSchemaDefinition) {

  val containers = new ArrayDeque[BOMContainer]
  val iterators = new ArrayDeque[Iterator[BOMNode]]
  var event: BOMEvent = null

  def hasNext: boolean = event match {
    case BOMEvent(x: BOMDocument, EndContainer) => false
    case null => true
    case _ => true
  }

  def nextEvent: BOMEvent = {
    event match {
      case BOMEvent(x: BOMContainer, StartContainer) => {
        containers.push(x)
        iterators.push(x.iterator)
        updateEvent
      }
      case BOMEvent(_, EndContainer) => {
          iterators.pop
          updateEvent
      }
      case BOMEvent(_, Leaf) => updateEvent
      case null => {
          event = BOMEvent(definition.createDocument(bspace), StartContainer)
      }
    }
    event
  }

  def updateEvent = {
    val it = iterators.peek
    val next = if (it.hasNext) it.next else null
    next match {
      case BOMContainer(_, _, _) => event = BOMEvent(next, StartContainer)
      case BOMLeaf(_, _, _) => event = BOMEvent(next, Leaf)
      case null => event = BOMEvent(containers.pop, EndContainer)
    }
  }
  
}
