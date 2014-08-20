/**
* If you have data in the hbase table but you get 0 results back, then you dont have permission on hbase and need to use hbase shell grant to assing permission.
* echo "grant 'bkersbergen', 'R', 'reco_product_catalog'"| hbase shell
**/

bla = load 'hbase://reco_product_catalog' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('c:*','-loadKey true -limit 5') AS (id:bytearray, c_map:map[]);    
bla2 = LIMIT bla 10;                                                                                                                                                                        
dump bla2;                                                                                                                                                                                  

