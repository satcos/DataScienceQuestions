-- Insert output to local text file
INSERT OVERWRITE LOCAL DIRECTORY '/tmp/result1/'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\u0001'
STORED AS TEXTFILE
SELECT sales_date, COUNT(*), SUM(volume)
FROM sales_data
GROUP BY sales_date;

-- Sample records randomly (1 out of 100) and write it to a file
INSERT OVERWRITE LOCAL DIRECTORY '/tmp/result2/'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\u0001'
STORED AS TEXTFILE
SELECT sales_date, sku_id, price, volume, amount
FROM sales_data
WHERE rand() <= 1/100
DISTRIBUTE by rand()
SORT by rand();

-- Insert result of one select query to a file
INSERT INTO TABLE sku_sales
SELECT sku_id, price, volume
FROM sales_data;

-- Store result of a query to HDFS
INSERT OVERWRITE DIRECTORY 'hdfs://nn1:8080/tmp/result3/'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\u0001'
STORED AS TEXTFILE
SELECT *
FROM sales_data;

-- Nested left join
INSERT INTO TABLE output_table
SELECT a.column_a, a.column_b, a.column_c, b.column_d_log_avg
FROM (
    SELECT column_a, column_b, column_c
    FROM table1
) a
LEFT JOIN (
    SELECT column_a, column_b, column_c, AVG(LOG(column_d)) AS column_d_log_avg
    FROM table2
    GROUP BY column_a, column_b, column_c
) b
ON a.column_a = b.column_a AND
    a.column_b = b.column_b AND
    a.column_c = b.column_c;
    
    
