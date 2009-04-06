package bom

/**
 * The Binary Object Model exception base class.
 */

//TODO: replace by calls to error()...

@SerialVersionUID(1)
case class BOMException(message: String, cause: Throwable)
  extends RuntimeException {

  def this() = this(null, null)
  def this(cause: Throwable) = this(null, cause)
  def this(message: String) = this(message, null)
    
}
