package fp

/**
 * Created by iurii.susuk on 06.10.2015.
 */
object Func extends App {

  def if2[A](cond: Boolean, onTrue: => A, onFalse: => A): A =
    if (cond) onTrue else onFalse

  // non-strictness will prevent error to be thrown
  val nonEmptyInput = "abc"
  println(if2[String](nonEmptyInput.isEmpty, sys.error("empty input - on non empty"), nonEmptyInput))

  val emptyInput = ""
  println(if2[String](emptyInput.isEmpty, sys.error("empty input - on empty"), emptyInput))

}
