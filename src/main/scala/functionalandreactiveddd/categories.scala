package functionalandreactiveddd

/**
 * @author Yura.Susuk yurasusuk@gmail.com.
 */

import java.util.Date

import scalaz.Monoid
import Monoid._
import scalaz.State
import State._


package object tr {
  type Money = BigDecimal
  type AccountNo = String
  type BS = Map[AccountNo, Balance]
  val balances: BS = Map.empty

  case class Transaction(accountNo: AccountNo, amount: Money, date: Date)

  val txns: List[Transaction] = List(Transaction("123", 1, new Date))

  def updateBalance(txns: List[Transaction]): State[BS, Unit] =
    modify { (b: BS) =>
      txns.foldLeft(b) { (a, txn) =>
        implicitly[Monoid[BS]].append(a, Map(txn.accountNo -> Balance(txn.amount)))
      }
    }
}

