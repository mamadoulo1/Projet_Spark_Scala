import java.io.FileNotFoundException

import org.apache.avro.ipc.specific.Person
import org.apache.log4j._

import scala.io._
import scala.collection.mutable._

object HelloWorldBigData {
  private var trace_appli : Logger = LogManager.getLogger("Logger_Console")
  BasicConfigurator.configure()
  def main(args: Array[String]): Unit = {
    //lecture_fichier("C:/programme/mesdonnees.txt")
    try{
      val diviseur = division(12, 0)
      trace_appli.info(s"Votre nombre est égal à  : ${diviseur}")
    }
    catch {
      case ex: ArithmeticException =>0
      case ex2: IllegalArgumentException=>0
    }


val nombre : Int = convert_entier("10")
    trace_appli.info(s"Votre nombre est égal à  : ${nombre}")
    val ma_var_imm : String = "Juvenal" //variable immutable
    val une_var_imm: String = "Formation Big Data"


    /* mon premier programme Scala*/
    println("Hello world: mon premier programme scala")
    var test_mu: Int= 10
    test_mu = test_mu + 15
    print(test_mu)


    var test_in: Int = 15
    test_in = 10 + test_in
    print("\nVotre texte contient "+Comptage_caracteres("Mamadou"))
    getResultat(10)
    testWhile(10)
    testFor()
    collectionScala()
    collectionTuple()
  }
  // ma premiere fonction
  def Comptage_caracteres(texte: String): Unit={
    trace_appli.info("démarrage du traçage de la classe")
    trace_appli.info(s"le paramètre tracé par Log4J pour cette fonction est : $texte")
    trace_appli.warn(s"Message d'avertissement Log4J interpolation de chaînes : ${10 + 15}")

    if (texte.isEmpty()){
      0
    }
    else {
      texte.trim.length()
    }
  }
  //synthaxe 2
  def Comptage_caracteres2(texte:String) : Int = {
    return texte.trim.length()
  }
  //synthaxe 3
  def Comtage_caracteres3 (texte: String): Int = texte.trim.length()
  //ma_premiere_methode_procedure
  def getResultat(parametre: Any) : Unit = {
    if (parametre == 10 ){
      println("Votre parametre est un entier")
    }
    else {
      println("Votre valeur n'est pas un entier.")
    }
  }
  //Structures conditionnelles
  def testWhile(valeur_cond: Int): Unit= {
    var i: Int = 0
    while (i < valeur_cond){
      println("Iteration while N° "+ i)
      i = i+ 1
    }
  }
  def testFor(): Unit = {
    var i : Int = 0
    for (i <- 5 to 15){
      println("Iteration For N° "+ i)

    }
  }
  //les collections en scala
  def collectionScala(): Unit={
    val ma_liste: List[Int]=List(1,2,3,45,75)
    val liste_s: List[String]= List("joel", "ed","chris", "maurice", "julien", "jean")
    val plage_g : List[Int] = List.range(1, 15, 2)
    println(ma_liste(0))
    for (i <-  liste_s) {
      println(i)
    }
    //collectionScala()
    val  resultats: List[String] = liste_s.filter(e => e.endsWith("n"))
    for (r <- resultats) {
      println(r)
    }
    val res: Int = liste_s.count(i => i.endsWith("n"))
    println("nbr d'éléments respectant la condition: "+ res)
    val ma_liste2: List[Int] = ma_liste.map(e => e * 2)
    for (i <-  ma_liste2) {
      println(i)
    }

    val ma_liste3: List[Int] = ma_liste.map((e:Int) => e * 2)
    val ma_liste4: List[Int] = ma_liste.map(_ * 2)
    val  ma_nouvelle_liste: List[Int] = plage_g.filter(p => p> 5)
    val  new_list : List[String]= liste_s.map(s=>s.capitalize)
    new_list.foreach(e=> println("nouvelle liste: "+ e))
    plage_g.foreach(println(_))
  }

  def collectionTuple(): Unit = {
    val tuple_test = (45, "JVC", "FALSE")
    println(tuple_test._3)
  }
  class Person(var nom: String, var prenom:String, var age:Int)
  val nouvelle_personne : Person = new Person("Choukogoue", "Jevnal", 40)
  val tuple_2 = ("test", nouvelle_personne, 67)
  println(tuple_2._2)

  //table de hashage
  val states = Map(
    "AK" -> "Alaska",
    "IL" -> "Illinois",
    "KY" -> "Kentucky"
  )
  val tableau : Array[String] = Array("a", "b", "c")
  tableau.foreach(e => println(e))

  //utilisation d'un gestionnaire d'erreur
  def convert_entier(nombre_entier: String) : Int = {
    try {
      val nombre : Int = nombre_entier.toInt
      return nombre
    }
    catch {
      case ex :  Exception =>0
    }

  }

  def lecture_fichier(chemin_fichier: String): Unit = {
    try {
      val fichier = Source.fromFile(chemin_fichier)
      fichier.getLines()
      fichier.close()

    }
    catch {
      case ex: FileNotFoundException => trace_appli.error("Votre fichier est introuvable." + ex.printStackTrace())
    }

  }

  def division(numerateur: Int, denominateur: Int): Double = {
    val resultat  = numerateur/denominateur
    return resultat
  }



}
