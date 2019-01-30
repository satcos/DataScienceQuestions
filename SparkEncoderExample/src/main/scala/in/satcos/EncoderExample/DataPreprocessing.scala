package in.satcos.EncoderExample

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory
import org.apache.spark.sql.functions.rand

object DataPreprocessing {

//  Prepares data for XG Boost models
//  1. Perform label encoding on training data
//  2. Apply same encoding to test data
  def main(args:Array[String]):Unit = {

  // Spark Context
    val spark = SparkSession
    .builder()
    .appName("EncoderExample")
    .getOrCreate()

    //    Setting option to supress INFO logs,
    //    Using WARN level to log application level logs
    spark.sparkContext.setLogLevel("WARN")
    val logger = LoggerFactory.getLogger(getClass.getName)

    //    Reading configuration using class
    val myconfig = PipelineConfig.fromJSONFile(spark)
    println(myconfig)

//     =======================================================
//     Work with train data
//     Read csv data
    logger.info("Loading Training data")
    logger.warn("Loading Training data")
    val trainInput = spark.read
      .format("csv")
      .option("sep", myconfig.dataSeperator)
      .schema(DataStructure.dfSchema)
      .option("header", "false")
      .load(myconfig.inputDir + "*")
      .na.drop()

    trainInput.show(10)

    // String indexers for categorical columns
    val indexers = DataStructure.catColumns.map {
      colName => new StringIndexer()
        .setInputCol(colName)
        .setOutputCol(colName + "_indexed")
        .setHandleInvalid("keep")
        .setStringOrderType("frequencyDesc")}

    // Pipeline
    val pipeline = new Pipeline().setStages(indexers.toArray)
    logger.info("Building string indexer")
    logger.warn("Building string indexer")
    val model = pipeline.fit(trainInput)

    // Save model
    logger.info("Saving model")
    logger.warn("Saving model")
    model.write.overwrite().save(myconfig.modelDir)

    logger.info("Performing training data transformation")
    logger.warn("Performing training data transformation")
    val trainEncoded = model.transform(trainInput).orderBy(rand)

    logger.info("Writing Fill Rate data, No aggregation")
    logger.warn("Writing Fill Rate data, No aggregation")
    trainEncoded.select("Record_ID", "RANDNUMBER", "TARGET",
      "COLUMN01_indexed", "COLUMN02_indexed", "COLUMN03_indexed", "COLUMN04_indexed", "COLUMN05_indexed", "COLUMN06_indexed",
      "COLUMN07_indexed", "COLUMN08_indexed", "COLUMN09_indexed", "COLUMN10_indexed",
      "COLUMN11", "COLUMN12", "COLUMN13", "COLUMN14", "COLUMN15", "COLUMN16", "COLUMN17", "COLUMN18", "COLUMN19", "COLUMN20")
      .write
      .mode("overwrite")
      .format("csv")
      .option("sep", myconfig.dataSeperator)
      .option("header", "false")
      .save(myconfig.outputDir)

//    Stopping the spark context
    spark.stop()
  }
}
