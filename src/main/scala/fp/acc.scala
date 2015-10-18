package fp

import java.util.Date

import scala.util.Try

/**
 * @author Yura.Susuk yurasusuk@gmail.com.
 */
package object base {

  trait Account

  trait Balance

  trait Amount

  trait AccountService {

    def open(no: String, name: String, date: Option[Date]): Try[Account]

    def close(account: Account, closeDate: Option[Date]): Try[Account]

    def debit(account: Account, amount: Amount): Try[Account]

    def credit(account: Account, amount: Amount): Try[Account]

    def balance(account: Account): Try[Balance]

    def transfer(from: Account, to: Account, amount: Amount):
    Try[(Account, Account, Amount)] = for {
      a <- debit(from, amount)
      b <- credit(to, amount)
    } yield (a, b, amount)
  }

}


