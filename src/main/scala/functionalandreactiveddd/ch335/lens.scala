package functionalandreactiveddd.ch335

/**
 * @author Yura.Susuk yurasusuk@gmail.com.
 */
case class Lens[O, V](get: O => V, set: (O, V) => O)

case class Address(no: String, street: String, city: String, state: String, zip: String)

case class Customer(id: Int, name: String, address: Address)

object Address {

  val noLens = Lens[Address, String](
    get = (address: Address) => address.no,
    set = (address: Address, newNo: String) => address.copy(no = newNo)
  )
}

object Customer {

  val addressLens = Lens[Customer, Address](
    get = (customer: Customer) => customer.address,
    set = (customer: Customer, newAddress: Address) => customer.copy(address = newAddress)
  )
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
}