package in.satcos.EncoderExample

import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.DataFrame

object DataStructure {

  val dfSchema = StructType(Array(
//    ID Column
    StructField("Record_ID", StringType, true),
//    Random number for sampling
    StructField("RANDNUMBER", DoubleType, true),
//    Target
    StructField("TARGET", DoubleType, true),
//    Categorical Column
    StructField("COLUMN01", StringType, true),
    StructField("COLUMN02", StringType, true),
    StructField("COLUMN03", StringType, true),
    StructField("COLUMN04", StringType, true),
    StructField("COLUMN05", StringType, true),
    StructField("COLUMN06", StringType, true),
    StructField("COLUMN07", StringType, true),
    StructField("COLUMN08", StringType, true),
    StructField("COLUMN09", StringType, true),
    StructField("COLUMN10", StringType, true),
//    Double
    StructField("COLUMN11", DoubleType, true),
    StructField("COLUMN12", DoubleType, true),
    StructField("COLUMN13", DoubleType, true),
    StructField("COLUMN14", DoubleType, true),
    StructField("COLUMN15", DoubleType, true),
//    Integer
    StructField("COLUMN16", DoubleType, true),
    StructField("COLUMN17", DoubleType, true),
    StructField("COLUMN18", DoubleType, true),
    StructField("COLUMN19", DoubleType, true),
    StructField("COLUMN20", DoubleType, true)
    ))

  val catColumns = Seq("COLUMN01", "COLUMN02", "COLUMN03", "COLUMN04", "COLUMN05", "COLUMN06", "COLUMN07", "COLUMN08", "COLUMN09", "COLUMN10")

  val labelCol = "TARGET"

  val seed = 123L
}