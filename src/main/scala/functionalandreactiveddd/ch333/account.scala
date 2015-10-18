package functionalandreactiveddd.ch333

import functionalandreactiveddd._

import java.util.Date

import scala.util.{Success, Failure, Try}


/**
 * @author Yura.Susuk yurasusuk@gmail.com.
 */
sealed trait Account {
  def no: String

  def name: String

  def dateOfOpen: Option[Date]

  def dateOfClose: Option[Date]

  def balance: Balance
}

final case class CheckingAccount private[ch333](no: String, name: String, dateOfOpen: Option[Date], dateOfClose: Option[Date] = None, balance: Balance = Balance()) extends Account

final case class SavingAccount private[ch333](no: String, name: String, rateOfInterest: Amount, dateOfOpen: Option[Date], dateOfClose: Option[Date] = None, balance: Balance = Balance()) extends Account

object Account {
  def checkingAccount(no: String, name: String, dateOfOpen: Option[Date], dateOfClose: Option[Date], balance: Balance): Try[Account] = {

    closeDateCheck(dateOfOpen, dateOfClose).map { d =>
      CheckingAccount(no, name, Some(d._1), d._2, balance)
    }
  }

  def savingAccount(no: String, name: String, rateOfInterest: Amount, dateOfOpen: Option[Date], dateOfClose: Option[Date], balance: Balance): Try[Account] = {

    closeDateCheck(dateOfOpen, dateOfClose).map { d =>
      SavingAccount(no, name, rateOfInterest, Some(d._1), d._2, balance)
    }
  }

  private def closeDateCheck(openDate: Option[Date], closeDate: Option[Date]): Try[(Date, Option[Date])] = {
    val od = openDate getOrElse today
    closeDate.map { cd =>
      if (cd before od) Failure(new Exception(s"Close date [$cd] cannot be earlier than open date[$od] "))
      else Success((od, Some(cd)))
    } getOrElse {
      Success((od, closeDate))
    }
  }
}