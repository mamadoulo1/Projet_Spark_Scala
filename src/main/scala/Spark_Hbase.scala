import sparkBigData._
import org.apache.spark.sql.execution.datasources.hbase._

object Spark_Hbase {
  def catalog_orders = """{
                         |  "table":{"namespace":"default", "name":"table_orders"},
                         |  "rowkey":"key",
                         |  "columns":{
                         |	"order_id":{"cf":"rowkey", "col":"key", "type":"string"},
                         |	"customer_id":{"cf":"orders", "col":"customerid", "type":"string"},
                         |	"campaign_id":{"cf":"orders", "col":"campaignid", "type":"string"},
                         |	"order_date":{"cf":"orders", "col":"orderdate", "type":"string"},
                         |	"city":{"cf":"orders", "col":"city", "type":"string"},
                         |	"state":{"cf":"orders", "col":"state", "type":"string"}
                         |
                         |  }
                         |}
                         |""".stripMargin

  def main(args: Array[String]): Unit = {
    val ss = SessionSpark(true)
    val df_hbase = ss.read
      .options(Map(HBaseTableCatalog.tableCatalog ->catalog_orders))
      .format("org.apache.spark.sql.execution.datasources.hbase")
        .load()
  //df_hbase.show(false)
    df_hbase.createOrReplaceTempView("Orders")
    ss.sql("select * from Orders where state = 'MA'")
  }


}
