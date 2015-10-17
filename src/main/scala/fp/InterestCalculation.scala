package fp

/**
 * @author Yura.Susuk yurasusuk@gmail.com.
 */
package object interest {
  type Amount = BigDecimal

  case class Balance(amount: Amount = 0)
}

import fp.interest._


object InterestCalculation extends App {

}

sealed trait TaxType
case object Tax extends TaxType
case object Fee extends TaxType
case object Commission extends TaxType

sealed trait TransactionType
case object InterestComputation extends TransactionType
case object Dividend extends TransactionType

trait TaxCalculationTable {
  type T <: TransactionType
  val transactionType: T
  ￼￼￼￼￼￼￼￼￼￼￼￼￼
  def getTaxRates: Map[TaxType, Amount] = {
    val taxTable = Map.empty[TaxType, Amount]
    taxTable
  }
}

trait TaxCalculation {
  type S <: TaxCalculationTable
  val taxCalculationTable: S

  def calculate(taxOn: Amount): Amount =
    taxCalculationTable.getTaxRates.map { case (t, r) =>
      doCompute(taxOn, r)
    }.sum

  protected def doCompute(taxOn: Amount, rate: Amount): Amount = {
    taxOn * rate
  }
}

trait InterestCalculation {
  type C <: TaxCalculation
  val taxCalculation: C

  def interest(b: Balance) = Some(b.amount * 0.05)

  def calculate(balance: Balance) =
    interest(balance).map { i =>
      i - taxCalculation.calculate(i)
    }
}

// explicit

object InterestTaxCalculationTable extends TaxCalculationTable {
  type T = TransactionType
  val transactionType = InterestComputation
}

object TaxCalculation extends TaxCalculation {
  type S = TaxCalculationTable
  val taxCalculationTable = InterestTaxCalculationTable
}

object InterestCalculation extends InterestCalculation {
  type C = TaxCalculation
  val taxCalculation = TaxCalculation
}