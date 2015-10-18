package functionalandreactiveddd.ch335

/**
 * @author Yura.Susuk yurasusuk@gmail.com.
 */
case class Lens[O, V](get: O => V, set: (O, V) => O)

case class Address(no: String, street: String, city: String, state: String, zip: String)

case class Customer(id: Int, name: String, address: Address)

private[ch335] object U {
  def compose[Outer, Inner, Value](outer: Lens[Outer, Inner], inner: Lens[Inner, Value]) = Lens[Outer, Value](
    get = outer.get andThen inner.get,
    set = (obj, value) => outer.set(obj, inner.set(outer.get(obj), value))
  )
}

import U._

private[ch335] object Address {

  val noLens = Lens[Address, String](
    get = (address: Address) => address.no,
    set = (address: Address, newNo: String) => address.copy(no = newNo)
  )
}

object Customer {

  private val addressLens = Lens[Customer, Address](
    get = (customer: Customer) => customer.address,
    set = (customer: Customer, newAddress: Address) => customer.copy(address = newAddress)
  )

  val addressNoLens = compose(addressLens, Address.noLens)

  //              ^
  // abstracts to |
  //  val addressNoLens = Lens[Customer, String](
  //    get = (customer: Customer) => Address.noLens.get(addressLens.get(customer)),
  //    set = (customer: Customer, newNo: String) => addressLens.set(customer, Address.noLens.set(addressLens.get(customer), newNo))
  //  )
}

object Test extends App {
  val addressB12 = Address("B-12", "Monroe Street", "Denver", "CO", "80231")
  println(Address.noLens.get(addressB12))

  val addressB13 = Address.noLens.set(addressB12, "B-13")
  println(Address.noLens.get(addressB13))

  val customerB12 = Customer(1, "blah", addressB12)
  println(Customer.addressLens.get(customerB12))

  val customerB13 = Customer.addressLens.set(customerB12, addressB13)
  println(Customer.addressLens.get(customerB13))

  val customerB14 = Customer.addressNoLens.set(customerB13, "B-14")
  println(Customer.addressLens.get(customerB14))
  println(Customer.addressNoLens.get(customerB14))
  println(customerB14)
}