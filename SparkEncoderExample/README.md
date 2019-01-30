#### Saprk Submit command
spark-submit \
  --class "in.satcos.EncoderExample.DataPreprocessing" \
  --master yarn \
  --deploy-mode cluster \
  --driver-memory 10G \
  --conf spark.rpc.message.maxSize=2047 \
  --num-executors 50 --executor-cores 5 --executor-memory 20G --queue default \
  --properties-file  Config.properties \
  sparkencoderexample_2.11-0.1.jar
  
#### Intellij Configuration
Tasks: clean compile "run"
VM parameters: -Xms512M -Xmx1024M -Xss1M -XX:+CMSClassUnloadingEnabled -Dspark.master=spark://localhost:7077
