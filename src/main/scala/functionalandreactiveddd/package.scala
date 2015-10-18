import java.util.Calendar

/**
 * @author Yura.Susuk yurasusuk@gmail.com.
 */
package object functionalandreactiveddd {
  type Amount = BigDecimal
  def today = Calendar.getInstance().getTime

  sealed trait Currency

  case class Balance(amount: Amount = 0)
}
