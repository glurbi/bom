package bom.stream

import java.io._
import java.util._

import bom.schema._
import bom.bin._
import bom._
import bom.bin._
import bom.schema._
import bom.stream.Event._

class EventReader(
  val bspace: BinarySpace,
  val schema: SchemaElement) {

  val containers = new ArrayDeque[BOMContainer]
  val iterators = new ArrayDeque[Iterator[BOMNode]]
  var event: Event = null

  def hasNext: Boolean = event match {
    case Event(x: BOMDocument, EndContainer) => false
    case null => true
    case _ => true
  }

  def nextEvent: Event = {
    event match {
      case Event(x: BOMContainer, StartContainer) => {
        containers.push(x)
        iterators.push(x.iterator)
        updateEvent
      }
      case Event(_, EndContainer) => {
          iterators.pop
          updateEvent
      }
      case Event(_, Leaf) => updateEvent
      case null => {
          event = Event(new BOMDocument(schema.asInstanceOf[SchemaDocument], bspace), StartContainer)
      }
    }
    event
  }

  def updateEvent = {
    val it = iterators.peek
    val next = if (it.hasNext) it.next else null
    next match {
      case BOMContainer(_, _, _) => event = Event(next, StartContainer)
      case BOMLeaf(_, _, _) => event = Event(next, Leaf)
      case null => event = Event(containers.pop, EndContainer)
    }
  }
  
}
