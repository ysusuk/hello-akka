package fp

/**
 * @author Yura.Susuk yurasusuk@gmail.com.
 */
sealed trait DayOfWeek {
  val value: Int

  override def toString = value match {
    case 1 => "Monday"
    case 2 => "Tuesday"
    case 3 => "Wednesday"
    case 4 => "Thursday"
    case 5 => "Friday"
    case 6 => "Saturday"
    case 7 => "Sunday"
  }
}

object DayOfWeek {
  private def unsafeDayOfWeek(day: Int) = new DayOfWeek {
    override val value: Int = day
  }

  private def isValid: Int => Boolean = { i => i >= 1 && i <= 7 }

  def dayOfWeek(day: Int) = if (isValid(day)) Some(unsafeDayOfWeek(day)) else None
}

object Test extends App {
  println(DayOfWeek.dayOfWeek(0))
  println(DayOfWeek.dayOfWeek(1))
}