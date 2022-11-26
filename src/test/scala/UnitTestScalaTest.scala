import org.scalatest
import org.scalatest.funsuite.AnyFunSuite
class UnitTestScalaTest extends AnyFunSuite {
  test("La division doit renvoyer 10") {
    assert(HelloWorldBigData.division(20,2)===10)
  }

  test("La division doit renvoyer une erreur de type Arithmetic"){
    assertThrows[ArithmeticException](
      HelloWorldBigData.division(20,0)
    )
  }

}
