import akka.actor.{ActorRef, Props, Actor}

/**
 * Created by iurii.susuk on 28.08.2015.
 */

case class ProcessIncomingOrder(orderInfo: Array[Byte])

class OrderManagementSystem extends Actor {

  def receive = {
    case msg: ProcessIncomingOrder =>
      val text = new String(msg.orderInfo)
      println(s"OrderManagementSystem: processing unique order $text")
      PipesAndFiltersDriver.completedStep()
  }
}

class Deduplicator(nextFilter: ActorRef) extends Actor {

  def receive = {
    case msg: ProcessIncomingOrder =>
      val text = new String(msg.orderInfo)
      println(s"Deduplicator: processing $text")
      // deduplicate
      nextFilter ! msg
      PipesAndFiltersDriver.completedStep()
  }
}

class Authenticator(nextFilter: ActorRef) extends Actor {

  def receive = {
    case ProcessIncomingOrder(orderInfo) =>
      val text = new String(orderInfo)
      println(s"Authenticator: processing $text")
      val orderText = text.replace("(certificate)", "")
      nextFilter ! ProcessIncomingOrder(orderText.toCharArray().map(_.toByte))
      // completable app: testing
      PipesAndFiltersDriver.completedStep()
  }
}

class Decrypter(nextFilter: ActorRef) extends Actor {

  def receive = {
    case ProcessIncomingOrder(orderInfo) =>
      val text = new String(orderInfo)
      println(s"Decrypter: processing $text")
      val orderText = text.replace("(encryption)", "")
      nextFilter ! ProcessIncomingOrder(orderText.toCharArray().map(_.toByte))
      // completable app: testing
      PipesAndFiltersDriver.completedStep()
  }
}

class OrderAcceptanceEndpoint(nextFilter: ActorRef) extends Actor {

  def receive = {
    case rawOrder: Array[Byte] =>
      val text = new String(rawOrder)
      println(s"OrderAcceptanceEndpoint: processing $text")
      nextFilter ! ProcessIncomingOrder(rawOrder)
      // completable app: testing
      PipesAndFiltersDriver.completedStep()
  }
}

object PipesAndFiltersDriver extends CompletableApp(10) {
  val orderText = "(encryption)(certificate)<order id='123'>...<order>"
  val rawOrderBytes = orderText.toCharArray.map(_.toByte)

  val empty = system.actorOf(Props[EmptyActor])
  val filter5 = system.actorOf(
    Props[OrderManagementSystem],
    "orderManagementSystem")
  val filter4 = system.actorOf(
    Props(classOf[Deduplicator], filter5),
    "deduplicator")
  val filter3 = system.actorOf(
    Props(classOf[Authenticator], filter4),
    "authenticator")
  val filter2 = system.actorOf(
    Props(classOf[Decrypter], filter3),
    "decrypter")
  val filter1 = system.actorOf(
    Props(classOf[OrderAcceptanceEndpoint], filter2),
    "orderAcceptanceEndpoint")

  filter1 ! rawOrderBytes
  filter1 ! rawOrderBytes

  awaitCompletion()

  println("PipesAndFiltersDriver: is completed.")
}

class EmptyActor extends Actor {
  def receive = Actor.emptyBehavior
}
