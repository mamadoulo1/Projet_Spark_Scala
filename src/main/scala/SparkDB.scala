import sparkBigData._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import java.util._

object SparkDB {
  def main(args: Array[String]): Unit = {
    val sc = SessionSpark(Env = true).sparkContext
    sc.setLogLevel("OFF")

    val ss = SessionSpark(true)

    val props_mysql = new Properties
    props_mysql.put("user","consultant")
    props_mysql.put("password", "pwd#86")
    val df_mysql = ss.read.jdbc("jdbc:mysql://127.0.0.1:3306/jea_db?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC","jea_db.orders", props_mysql)
    //df_mysql.show(15)
    //df_mysql.printSchema()
/*
    val df_mysql2 = ss.read
      .format("jdbc")
      .option("url", "jdbc:mysql://127.0.0.1:3306/jea_db?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC" )
      .option("user", "consultant")
      .option("password", "pwd#86")
      .option("dbtable", "(select state, city , sum(round(numunits * totalprice)) as commandes_totales from jea_db.orders group by state, city) table_summary")
      .load()
    //df_mysql2.show()

*/
    val props_postgres = new Properties
    props_postgres.put("user","postgres")
    props_postgres.put("password", "betclic")
    val df_postgres = ss.read.jdbc("jdbc:postgresql://127.0.0.1:5432/jea_db","orders", props_postgres)
    //df_postgres.show(15)


    val df_postgresql2 = ss.read
      .format("jdbc")
      .option("url", "jdbc:postgresql://127.0.0.1:5432/jea_db" )
      .option("user", "postgres")
      .option("password", "betclic")
      .option("dbtable", "(select state, city , sum(round(numunits * totalprice)) as commandes_totales from orders group by state, city) table_postgres")
      .load()

    //df_postgresql2.show()

    val props_sqlserver = new Properties
    props_sqlserver.put("user","consultant")
    props_sqlserver.put("password", "pwd#86")
    //val df_sqlserver = ss.read.jdbc("jdbc:sqlserver://LAPTOP-68A7PLSM:1433;databaseName=jea_db;","orders",props_sqlserver)

    val df_sqlserver1 = ss.read
      .format("jdbc")
      .option("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver")
      .option("url", "jdbc:sqlserver://LAPTOP-68A7PLSM:1433;databaseName=jea_db; IntegratedSecurity=true"  )
      .option("dbtable", "(select state, city , sum(numunits * totalprice) as commandes_totales from orders group by state, city) table_sqlserver")
      .load()

    df_sqlserver1.show(10)




  }

}
