import sparkBigData._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import com.datastax.spark.connector._
import com.datastax.spark.connector.cql.CassandraConnector
import org.apache.spark.sql.cassandra._


object Spark_Cassandra {
  def main(args: Array[String]): Unit = {
    val ss = SessionSpark(true)
    ss.conf.set(s"ss.sql.catalog.jvc", "com.datastax.spark.connector.datasource.CassandraCatalog")
    ss.conf.set(s"ss.sql.catalog.jvc.spark.cassandra.connection.host", "127.0.0.100")
    ss.sparkContext.cassandraTable("demo", "spacecraft_journey_catalog")
    val df_cassandra = ss.read
      .format("org.apache.spark.sql.cassandra")
      .options(Map("keyspace" -> "demo", "table" -> "spacecraft_journey_catalog"))
      .load()
    df_cassandra.printSchema
    //df_cassandra.explain
    df_cassandra.show

  }
}
