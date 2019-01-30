package in.satcos.EncoderExample

import org.apache.spark.sql.SparkSession

import scala.io.Source

case class PipelineConfig(
   inputDir: String,
   outputDir: String,
   modelDir: String,
   dataSeperator: String) {

  /** slightly more legible representation of the model */
  override def toString(): String =
    (
      "- inputDir: " + inputDir + "\n" +
      "- outputDir: " + outputDir + "\n" +
      "- modelDir: " + modelDir + "\n" +
      "- dataSeperator: " + dataSeperator + "\n"
    )
}

/** Pipeline factories.*/
object PipelineConfig {
  def fromJSONFile(spark: SparkSession):PipelineConfig = {
    PipelineConfig(
      inputDir = spark.sparkContext.getConf.get("spark.app.inputDir"),
      outputDir = spark.sparkContext.getConf.get("spark.app.outputDir"),
      modelDir = spark.sparkContext.getConf.get("spark.app.modelDir"),
      dataSeperator = "\u0001")
  }
}
