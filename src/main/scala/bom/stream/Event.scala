package bom.stream

import bom._

object Event {
  
  abstract class EventType
  case object StartContainer extends EventType
  case object EndContainer extends EventType
  case object Leaf extends EventType

}

case class Event(val node: BOMNode, val eventType: Event.EventType) {

  override def toString: String = node.toString

}
