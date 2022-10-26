import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.plans._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.expressions._
import org.apache.hadoop.fs._
import org.apache.hadoop.fs.{FileSystem, Path}



object sparkBigData {
  val schema_order = StructType(
    Array(
      StructField("orderid", IntegerType, false),
      StructField("customerid", IntegerType, false),
      StructField("campaignid", IntegerType, true),
      StructField("orderdate", TimestampType, true),
      StructField("city", StringType, true),
      StructField("state", StringType, true),
      StructField("zipcode", StringType, true),
      StructField("paymenttype", StringType, true),
      StructField("totalprice", DoubleType, true),
      StructField("numorderlines", IntegerType, true),
      StructField("numunits", IntegerType, true)

    )
  )


  var ss: SparkSession = null

  def main(args: Array[String]): Unit = {

    val session_s = SessionSpark(Env = true)
    val sc =SessionSpark(true).sparkContext
    sc.setLogLevel("OFF")
    val df_test = session_s.read
      .format("com.databricks.spark.csv")
      .option("delimiter", ",")
      .option("header", true)
      .csv("C:\\Users\\mamad\\Downloads\\Formation Spark Big Data\\sources de données\\2010-12-06.csv")
    //df_test.show(10)

    val df_goup = session_s.read
      .format("csv")
      .option("inferSchema", "true")
      .option("header", "true")
      .load("C:\\Users\\mamad\\Downloads\\Formation Spark Big Data\\sources de données\\csvs\\")

    //df_goup.show(10)

    val df_goup2 = session_s.read
      .format("csv")
      .option("inferSchema", "true")
      .option("header", "true")
      .load("C:\\Users\\mamad\\Downloads\\Formation Spark Big Data\\sources de données\\2010-12-06.csv",
        "C:\\Users\\mamad\\Downloads\\Formation Spark Big Data\\sources de données\\2011-01-20.csv")

    //df_goup2.show(10)
    //df_test.printSchema()
    val df_2 =df_test.select(col("InvoiceNo").cast(StringType),
      col("_c0").alias("ID du client"),
      col("StockCode").cast(IntegerType).alias("Code de la marchandise"),
      col("Invoice".concat("No")).alias("ID de la commande"))
     //df_2.show(10)

    val df_3= df_test.withColumn("InvoiceNo", col("InvoiceNo").cast(StringType))
      .withColumn("StockCode", col("StockCode").cast(StringType))
      .withColumn("valeur constante", lit(50))
        .withColumnRenamed("_c0", "ID client")
        .withColumn("ID_commande", concat_ws("|",col("InvoiceNo"), col("ID client")))
        .withColumn("Total_amount",round(col("Quantity")* col("UnitPrice"), 2))
        .withColumn("Created_dt", current_timestamp())
        .withColumn("Reduction_test", when(col("Total_amount")>15, lit(3)).otherwise(lit(0)))
      .withColumn("reduction",when(col("total_amount")<15,lit(0))
      .otherwise(when(col("total_amount").between(15, 20), lit(3))
        .otherwise(when(col("total_amount")>15,lit(0)))))
        .withColumn("net_income", col("Total_amount")-col("reduction"))

         //df_3.show(4)
         val df_not_reduced = df_3.filter(col("reduction")=== lit(0) || col("Country").isin("France", "USA"))
         //df_not_reduced.show(10)

    //jointure dataframe
    val df_ordres = session_s.read
      .format("com.databricks.spark.csv")
      .option("delimiter", "\t")
      .option("header", true)
      .schema(schema_order)
      .csv("C:\\Users\\mamad\\Downloads\\Formation Spark Big Data\\sources de données\\orders.txt")
    df_ordres.show(10)
    df_ordres.printSchema()

    val df_ordersGood = df_ordres.withColumnRenamed("numunits", "numunits_order")
      .withColumnRenamed("totalprice", "totalprice_order")

    //df_ordres.show(10)
    //df_ordres.printSchema()

    val df_products = session_s.read
      .format("com.databricks.spark.csv")
      .option("delimiter", "\t")
      .option("header", true)
      .load("C:\\Users\\mamad\\Downloads\\Formation Spark Big Data\\sources de données\\product.txt")


    val df_orderline = session_s.read
      .format("com.databricks.spark.csv")
      .option("delimiter", "\t")
      .option("header", true)
      .load("C:\\Users\\mamad\\Downloads\\Formation Spark Big Data\\sources de données\\orderline.txt")

    //df_ordres.show(10)
    //df_products.show(10)
    //df_orderline.show(10)

    val df_joinOrders = df_orderline.join(df_ordersGood, df_ordersGood.col("orderid") === df_orderline.col("orderid"), Inner.sql)
      .join(df_products, df_products.col("productid") === df_orderline.col("productid"), "inner")

    val df_fichier1 = session_s.read
      .format("com.databricks.spark.csv")
      .option("delimiter", ",")
      .option("header", true)
      .csv("C:\\Users\\mamad\\Downloads\\Formation Spark Big Data\\sources de données\\2010-12-06.csv")
    val df_fichier2 = session_s.read
      .format("com.databricks.spark.csv")
      .option("delimiter", ",")
      .option("header", true)
      .csv("C:\\Users\\mamad\\Downloads\\Formation Spark Big Data\\sources de données\\2011-01-20.csv")

    val df_fichier3 = session_s.read
      .format("com.databricks.spark.csv")
      .option("delimiter", ",")
      .option("header", true)
      .csv("C:\\Users\\mamad\\Downloads\\Formation Spark Big Data\\sources de données\\2011-12-08.csv")

    val df_unitedfiles = df_fichier1.union(df_fichier2.union(df_fichier3))
    //println(df_unitedfiles.count() + " "+ df_fichier3.count())

    df_joinOrders.withColumn("total_amount", round(col("numunits") * col("totalprice")))
      .groupBy("city")
      .sum("total_amount").as("Commandes totales ")
      .show()

    //opérations de fénétrage

    val wn_spec = Window.partitionBy(col("state"))
   /* val df_windows = df_joinOrders.withColumn("ventes_dep", sum(round(col("numunits") * col("totalprice"), 2)).over(wn_spec))
      .select(
        col("orderlineid"),
        col("zipcode"),
        col("PRODUCTGROUPNAME"),
        col("state"),
        col("ventes_dep")

      )

    */

    //df_windows.repartition(1).write.mode(SaveMode.Overwrite).option("header", "true").csv("C:\\Users\\mamad\\Downloads\\Formation Spark Big Data\\sorties\\Ecriture")

   //Ma,ipulation des dates avec Spark
    df_ordersGood.withColumn("date_lecture", date_format(current_date(), "dd/MM/yyyy"))
      .withColumn("date_lecture_complete", current_timestamp())
      .withColumn("périodes_secondes", window(col("orderdate"), "10 minutes"))
      .select(
        col("orderdate"),
        col("périodes_secondes"),
        col("périodes_secondes.start"),
        col("périodes_secondes.end")
      )
      .show(5)

    val df_united_files = df_fichier1.union(df_fichier2)
      .union(df_fichier3)

    df_united_files.show(5)
    df_united_files.printSchema()
    df_united_files.withColumn("InvoiceDate", to_date(col("InvoiceDate")))
      .withColumn("InvoiceTimeStamp", to_timestamp(col("InvoiceTimeStamp").cast(TimestampType)))
      .withColumn("Invoice_add_month", add_months(col("InvoiceDate"), 2))
      .withColumn("Invoice_add_date", date_add(col("InvoiceDate"), 30))
      .withColumn("Invoice_sub_date", date_sub(col("InvoiceDate"), 30))
      .withColumn("Invoice_datediff", datediff(current_date(),col("InvoiceDate")))
      .withColumn("InvoiceDateQuarter", quarter(col("InvoiceDate")))
      .withColumn("Invoice_id", unix_timestamp(col("InvoiceDate")))
      .withColumn("Invoice_format", from_unixtime(unix_timestamp(col("InvoiceDate")), "dd-MM-yyyy"))
      .show(10)


    df_products
        .withColumn("productGP", substring(col("PRODUCTGROUPNAME"),2, 2))
      .withColumn("productln", length(col("PRODUCTGROUPNAME")))
      .withColumn("concat_product", concat_ws("|", col("PRODUCTID"), col("INSTOCKFLAG")))
      .withColumn("PRODUCTGROUPNAMELOWER", lower(col("PRODUCTGROUPNAME")))
      //.where(regexp_extract(trim(col("PRODUCTID")), "[0-9]{9}", 0) ===  trim(col("PRODUCTID")))
      .where(! col("PRODUCTID").rlike("[0-9]{9}"))
      .show()



    def valid_phone(phone_to_test : String): Boolean = {
      var result : Boolean = false
      val motif_regex = "^0[0-9]{9}".r
      if(motif_regex.findAllIn(phone_to_test.trim)== phone_to_test.trim){
        result = true
      } else {
        result =false
      }
      return result

    }
    val valid_phone_udf : UserDefinedFunction= udf{(phone_to_test: String)=> valid_phone(phone_to_test: String)}
    import session_s.implicits._
    val phone_list:  DataFrame = List("062531423", "+330989213476", "0627892134099").toDF("phone_number")
    phone_list.withColumn("test_phone", valid_phone_udf(col("phone_number")))
    session_s.udf.register("valid_phone", valid_phone_udf)
    phone_list.createOrReplaceTempView("phone_table")
    session_s.sql("select valid_phone(phone_number) as valid_phone from phone_table").show()



    df_joinOrders.createOrReplaceTempView("orders")


    session_s.sql("select state, city from orders ").show()

/*
    df_joinOrders.withColumn("total_amount", round(col("numunits")* col("totalprice"), 2))
      .groupBy("state","city")
      .sum("total_amount").alias("commandes_totales")
*/






    def spark_hdfs(): Unit ={
    val config_fs = SessionSpark(true).sparkContext.hadoopConfiguration
    val fs = FileSystem.get(config_fs)
    val scr_path = new Path("/user/datalake/marketing")
    val dest_path = new Path("/user/datalake/index")
    val ren_scr = new Path("/user/datalake/marketing/fichier_reporting.parquet")
    val dest_scr = new Path("/user/datalake/marketing/reporting.parquet")

    //Lecture des fichiers d'un dossier
    val files_file = fs.listStatus(scr_path).map(x => x.getPath)
    for(i <- 1 to files_file.length){
      println(files_file(i))
    }
    //renommer un fichier
    fs.rename(ren_scr, dest_scr)
    //supprimer les fichiers d'un dossier
    fs.delete(dest_scr, true)
    //copier un fichier du répertoire local vers le cluster
    //fs.copyFromLocalFile(dest_scr, ren_scr)

      /*

      val df_hive = session_s.table("orders") //lire une table à partir du metastore Hive
      val df_sql : DataFrame = session_s.sql("select state, city from orders ")
      df_sql.write.mode(SaveMode.Overwrite).saveAsTable("report_orders")  //enregistrer et écrire un dataframe dans le metastore hive.
      */
  }





  }


  def manip_rdd(): Unit = {
    val session_s = SessionSpark(Env = true)
    val sc =SessionSpark(true).sparkContext
    sc.setLogLevel("OFF")
    val rdd_test : RDD[String] =sc.parallelize(List("Alain", "Juvenal", "Julien", "Anna"))
    rdd_test.foreach{
      l => println(l)
    }

    val rdd2 : RDD[String] = sc.parallelize(Array("lucie", "fabien", "julie"))
    rdd2.foreach(l => println(l))

    val  rdd3 = sc.parallelize(Seq(("Alain", "Math", 17), ("Mamadou", "HG", 12), ("Juvenal", "Physique", 17)))
    println("Premier element de mon rdd3")
    rdd3.take(1).foreach(l => println(l))
    if (rdd3.isEmpty()){
      println("Le rdd est vide")
    }else{
      rdd3.foreach(l=>println(l))
      //rdd3.saveAsTextFile("C://Users//mamad//Downloads//Formation Spark Big Data//sorties//rdd.txt")
      //rdd3.repartition(1).saveAsTextFile("C://Users//mamad//Downloads//Formation Spark Big Data//sorties//rdd3.txt")
    }
    //creation d'un RDD à partir d'une source de données
    val rdd_4 = sc.textFile("C:\\Users\\mamad\\Downloads\\Formation Spark Big Data\\sorties\\rdd3.txt\\ma_texte.txt")
    println("Lecture du RDD 4")
    rdd_4.foreach(l=>println(l))

    val rdd_5 = sc.textFile("C:\\Users\\mamad\\Downloads\\Formation Spark Big Data\\sorties\\rdd3.txt\\*")
    println("Lecture du contenu du RDD5")
    rdd_5.foreach{l => println(l)}

    //transformations des RDD
    val rdd_trans : RDD[String]=sc.parallelize(List("alain mange une banane", "la banane est bonne pour la santé", "acheter une bonne banane"))
    rdd_trans.foreach(l=> println("ligne de mon RDD :"+l))
    val rdd_map = rdd_trans.map(x => x.split(" "))
    println("Nbre d'elements : "+rdd_map.count())

    val rdd6 = rdd_trans.map(w=>(w, w.length,  w.contains("banane")))
    rdd6.foreach(l=>println(l))

    val rdd7= rdd6.map(x=>(x._1.toUpperCase(), x._2, x._3))
    rdd7.foreach(l=>println(l))

    val rdd8 = rdd6.map(x=>(x._1.split(" "), 1))
    rdd8.foreach(l=>println(l))
    val rdd_fm = rdd_trans.flatMap(x=>x.split(" ")).map(w=>(w,1))
    rdd_fm.foreach(l=>println(l))

    val rdd_compte = rdd_5.flatMap(x=>x.split(" ")).map(w=>(w,1)).reduceByKey((x,y)=>x+y)
    rdd_compte.foreach(l=>println(l))
    //rdd_compte.repartition(1).saveAsTextFile("C:\\Users\\mamad\\Downloads\\Formation Spark Big Data\\sorties\\rdd3.txt\\comptage.txt")
    val rdd_filtered = rdd_fm.filter(x=>x._1.equals("banane"))
    rdd_filtered.foreach(l=>println(l))

    val rdd_reduced = rdd_fm.reduceByKey((x,y)=>x+y)
    rdd_reduced.foreach(l=>println(l))

    import  session_s.implicits._
    val df = rdd_fm.toDF("texte", "valeur")
    df.show()
}
  /**
   * fonction qui initialise et intancie une session Spark
   * @param Env: C'est une variable qui indique l'environnement sur lequel l'application est déployée.
   *           Si Env = True, l'appli est déployée en local
   */
  def SessionSpark(Env: Boolean =true) : SparkSession = {
    if(Env==true){
      System.setProperty("hadoop.home.dir", "C://hadoop")
      ss = SparkSession.builder()
          .master("local[*]")
          .config("spark.sql.crossJoin.enabled", "true")
          //.enableHiveSupport()
          .getOrCreate()
    }
    else {
      ss = SparkSession.builder()
        .appName("Mon application Spark")
        .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        .config("spark.sql.crossJoin.enabled", "true")
        //.enableHiveSupport()
        .getOrCreate()
    }
    ss

  }

}
