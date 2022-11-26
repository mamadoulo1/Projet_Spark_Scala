import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest._
class FlatSpecTest  extends AnyFlatSpec with Matchers {
  "la division" should("renvoyer 10") in {
    assert(HelloWorldBigData.division(20,2)===10)
  }
  "an arithmetic error" should("be thrown") in {
    an [ArithmeticException] should be thrownBy(HelloWorldBigData.division(20,0))
  }
  it should("Send an error") in {
    var liste_fruits : List[String] = List("banane", "Pomme", "Pamplemousse")
    assertThrows[IndexOutOfBoundsException](liste_fruits(4))
  }

}
