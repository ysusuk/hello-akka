import akka.actor.{ActorRef, Props, Actor}

/**
 * Created by iurii.susuk on 28.08.2015.
 */

class OrderAcceptanceEndpoint(nextFilter: ActorRef) extends Actor {

  def receive = {
    case rawOrder: Array[Byte] =>
      val text = new String(rawOrder)
      println(s"OrderAcceptanceEndpoint: processing $text")
      nextFilter ! ProcessIncomingOrder(rawOrder)
      // completable app
      PipesAndFiltersDriver.completedStep()
  }
}

object PipesAndFiltersDriver extends CompletableApp(9) {
  val orderText = "(encryption)(certificate)<order id='123'>...<order>"
  val rawOrderBytes = orderText.toCharArray.map(_.toByte)

  val filter5 = system.actorOf(
    Props[OrderManagementSystem],
    "orderManagementSystem")
  val filter4 = system.actorOf(
    Props(classOf(Deduplicator), filter5),
    "deduplicator")
  val filter3 = system.actorOf(
    Props(classOf(Authenticator), filter4),
    "authenticator")
  val filter2 = system.actorOf(
    Props(classOf(Decrypter), filter3),
    "decrypter")
  val filter1 = system.actorOf(
    Props(classOf(OrderAcceptanceEndpoint), filter2),
    "orderAcceptanceEndpoint")

  filter1 ! rawOrderBytes
  filter1 ! rawOrderBytes

  awaitCompletion()

  println("PipesAndFiltersDriver: is completed.")
}
