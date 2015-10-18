package functionalandreactiveddd.ch334

import java.util.Date

import functionalandreactiveddd._
import functionalandreactiveddd.ch333.Account

/**
 * @author Yura.Susuk yurasusuk@gmail.com.
 */
case class Money(amount: Amount)

case class Position(account: Account, ccy: Currency, balance: Money)

case class Portfolio(pos: Seq[Position], asOf: Date) {
  def balance(a: Account, ccy: Currency): Amount =
    pos.find(p => p.account == a && p.ccy == ccy)
      .map(_.balance.amount).getOrElse(0)

  def balance(ccy: Currency): Amount =
    pos.find(p => p.ccy == ccy)
      .map(_.balance.amount).sum
}
