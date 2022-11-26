import org.apache.avro.generic.GenericData.StringType
import org.apache.calcite.avatica.ColumnMetaData.StructType
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest._

import scala.collection.JavaConverters._

trait SparkProvider {
  val sst = SparkSession.builder.
    master("local[*]")
    .getOrCreate()
}

class SparkTestUnitaire extends FlatSpecTest with SparkProvider {
  var Env : Boolean = true
  it should("instance a Spark Session") in {
    var sst = sparkBigData.SessionSpark(Env)

  }
  it should("Comare two dataframes ") in {
    val structure_df = List(
      StructField("Salaire", IntegerType, true)
    )


  }
val data_df = Seq(
  Row(1),
  Row(2),
  Row(3)
)



}
