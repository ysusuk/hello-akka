import akka.persistence.{AtLeastOnceDelivery, PersistentActor}

case class QuoteBestLoanRate()

case class LoanRateQuoteStarted()

class LoanBroker extends PersistentActor with AtLeastOnceDelivery {
  override def receiveRecover: Receive = ???

  override def receiveCommand: Receive = {
    case command: QuoteBestLoanRate =>
    case started: LoanRateQuoteStarted =>

      persist(started) { ack =>
        //confirmDelivery(ack.id)
      }
  }

  override def persistenceId: String = ???
}

