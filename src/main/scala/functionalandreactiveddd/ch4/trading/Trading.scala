package functionalandreactiveddd.ch4.trading

/**
 * @author Yura.Susuk yurasusuk@gmail.com.
 */
trait Trading[Account, Market, Order, ClientOrder, Execution, Trade] {
  def clientOrders: ClientOrder => List[Order]
  def execute(market: Market, account: Account): Order => List[Execution]
  def allocate(accounts: List[Account]): Execution => List[Trade]
}
