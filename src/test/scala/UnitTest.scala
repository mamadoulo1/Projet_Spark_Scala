import junit.framework.TestCase
import org.junit._
import org.junit.Assert._

class UnitTest extends TestCase{
  @Test
  def testDivision() : Unit = {
    val valeur_actuelle : Double = HelloWorldBigData.division(10,2)
    val valeur_prevue : Int = 5
    assertEquals("Le resultat attendu est 5",valeur_prevue,valeur_actuelle.toInt)


  }
  @Test
  def testConversion(): Unit = {
    var valeur_actuelle : Int= HelloWorldBigData.convert_entier("15")
    var valeur_prevue : Int = 15
    assertEquals("La fonction doit renvoyer un nombre.", valeur_prevue, valeur_actuelle)
  }

  @Test
  def testComptageCaracteres(): Unit = {
    var valeur_actuelle : Int = HelloWorldBigData.Comptage_caracteres2("Exemples de caracteres")
    var valeur_prevue : Int = 22
    assertEquals("La fonction doit renvoyer un nombre.", valeur_prevue, valeur_actuelle)
  }

}
