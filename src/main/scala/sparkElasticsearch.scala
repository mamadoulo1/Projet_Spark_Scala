import sparkBigData._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.commons.httpclient._
object sparkElasticsearch {
  def main(args: Array[String]): Unit = {
    val ss = SessionSpark(true)
    val df_ordres = ss.read
      .format("com.databricks.spark.csv")
      .option("delimiter", ";")
      .option("header", true)
      .option("inferschema", "true")
      .csv("C:\\Users\\mamad\\Downloads\\Formation Spark Big Data\\sources de données\\orders_csv.csv")
    df_ordres.show(10)

    df_ordres.write //ecriture du dataframe dans un index elasticsearch
      .mode("append")
      .format("org.elasticsearch.spark.sql")
      .option("es.port", "9200")
      .option("es.nodes", "localhost")
      .save("indexe/doc")
  }

}
