package bom.stream

import java.io._
import scala.collection.mutable.Stack

import bom.schema._
import bom.bin._
import bom._
import bom.bin._
import bom.schema._
import bom.stream.Event._
import bom.cache._

class EventReader(
  val bspace: BinarySpace,
  val schema: SchemaElement) {

  val containers = new Stack[BOMContainer]
  val iterators = new Stack[java.util.Iterator[BOMNode]]
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
          event = Event(new BOMDocument(schema.asInstanceOf[SchemaDocument], bspace)
                          with UnboundedHashMapCache, StartContainer)
      }
    }
    event
  }

  def updateEvent = {
    val it = iterators.top
    val next = if (it.hasNext) it.next else null
    next match {
      case BOMContainer(_, _, _) => event = Event(next, StartContainer)
      case BOMLeaf(_, _, _) => event = Event(next, Leaf)
      case null => event = Event(containers.pop, EndContainer)
    }
  }
  
}
