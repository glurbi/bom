package bom.stream

import bom._

object BOMEvent {
  
  abstract class BOMEventType
  case object StartContainer extends BOMEventType
  case object EndContainer extends BOMEventType
  case object Leaf extends BOMEventType

}

case class BOMEvent(val node: BOMNode, val eventType: BOMEvent.BOMEventType) {

  override def toString: String = node.toString

}
