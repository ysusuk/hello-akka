import akka.actor.{Actor, Props, ActorRef}
import akka.event.{SubchannelClassification, EventBus}
import akka.util.Subclassification

/**
 * Created by iurii.susuk on 01.09.2015.
 */
case class Money(amount: BigDecimal) {

  def this(amount: String) = this(new java.math.BigDecimal(amount))

  amount.setScale(4, BigDecimal.RoundingMode.HALF_UP)
}

case class Market(name: String)

case class PriceQuoted(market: Market, ticker: Symbol, price: Money)

class QuotesEventBus extends EventBus with SubchannelClassification {
  type Event = PriceQuoted
  type Classifier = Market
  type Subscriber = ActorRef

  override protected implicit def subclassification: Subclassification[Classifier] = new Subclassification[Classifier] {
    override def isEqual(x: Classifier, y: Classifier): Boolean = x.equals(y)

    override def isSubclass(x: Classifier, y: Classifier): Boolean = x.name.startsWith(y.name)
  }

  override protected def publish(event: Event, subscriber: Subscriber): Unit = subscriber ! event

  override protected def classify(event: Event): Market = event.market
}

class AllMarketsSubscriber extends Actor {
  def receive = {
    case quote: PriceQuoted =>
      println(s"AllMarketsSubscriber received: $quote")
      SubClassificationDriver.completedStep
  }
}

class NASDAQSubscriber extends Actor {
  def receive = {
    case quote: PriceQuoted =>
      println(s"NASDAQSubscriber received: $quote")
      SubClassificationDriver.completedStep
  }
}

class NYSESubscriber extends Actor {
  def receive = {
    case quote: PriceQuoted =>
      println(s"NYSESubscriber received: $quote")
      SubClassificationDriver.completedStep
  }
}

object SubClassificationDriver extends CompletableApp(6) {

  val allSubscriber = system.actorOf(Props[AllMarketsSubscriber], "AllMarketSubscriber")
  val nasdaqSubscriber = system.actorOf(Props[NASDAQSubscriber], "NASDAQSubscriber")
  val nyseSubscriber = system.actorOf(Props[NYSESubscriber], "NYSESubscriber")

  val quotesBus = new QuotesEventBus

  quotesBus.subscribe(allSubscriber, Market("quotes"))
  quotesBus.subscribe(nasdaqSubscriber, Market("quotes/NASDAQ"))
  quotesBus.subscribe(nyseSubscriber, Market("quotes/NYSE"))

  quotesBus.publish(PriceQuoted(Market("quotes/NYSE"), Symbol("ORCL"), new Money("37.84")))
  quotesBus.publish(PriceQuoted(Market("quotes/NASDAQ"), Symbol("MSFT"), new Money("37.16")))
  quotesBus.publish(PriceQuoted(Market("quotes/DAX"), Symbol("SAP:GR"), new Money("61.95")))
  quotesBus.publish(PriceQuoted(Market("quotes/NKY"), Symbol("6701:JP"), new Money("237")))

  awaitCompletion
}