hbase-kerberos-mapreduce-hadoop2
================================

Example of how to do HBase calls in a mapper or reducer with Kerberos security configured in hadoop.

Because we were having issues like:
http://grokbase.com/t/hbase/user/12be21k9wh/unable-to-access-hbase-from-a-mapper

    2012-11-14 10:55:24,486 ERROR org.apache.hadoop.security.UserGroupInformation: PriviledgedActionException as:subroto (auth:SIMPLE) cause:javax.security.sasl.SaslException: GSS initiate failed [Caused by GSSException: No valid credentials provided (Mechanism level: Failed to find any Kerberos tgt)]
    2012-11-14 10:55:24,490 WARN org.apache.hadoop.ipc.SecureClient: Exception encountered while connecting to the server : javax.security.sasl.SaslException: GSS initiate failed [Caused by GSSException: No valid credentials provided (Mechanism level: Failed to find any Kerberos tgt)]
    2012-11-14 10:55:24,493 FATAL org.apache.hadoop.ipc.SecureClient: SASL authentication failed. The most likely cause is missing or invalid credentials. Consider 'kinit'.


The solution was simple in the end, however it took me a while to figure it out.

First authenticate myself

    [bkersbergen@hdp211 hbase-hdp]$ kinit
    Password for bkersbergen@BOLCOM.NET: *************

Grant myself access to the Hbase table (hbase shell):

    grant 'bkersbergen', 'RWX', 'reco_product_catalog'

verify I have access to the Hbase table (hbase shell)
    
    scan 'reco_product_catalog'
    SLF4J: Class path contains multiple SLF4J bindings.
    SLF4J: Found binding in [jar:file:/usr/lib/hadoop/lib/slf4j-log4j12-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
    SLF4J: Found binding in [jar:file:/usr/lib/zookeeper/lib/slf4j-log4j12-1.6.1.jar!/org/slf4j/impl/StaticLoggerBinder.class]
    SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
    ROW                      COLUMN+CELL
    0                       column=c:p, timestamp=1406657630994, value={"_id":"0","globalID":"0 (..and the rest of your hbase table)


Then run the hadoop job:

    [bkersbergen@hdp211 hbase-hdp]$ ./build_run.sh; hadoop fs -text output/part*
    [INFO] Scanning for projects...
    [INFO]
    [INFO] Using the builder org.apache.maven.lifecycle.internal.builder.singlethreaded.SingleThreadedBuilder with a thread count of 1
    [INFO]
    [INFO] ------------------------------------------------------------------------
    [INFO] Building hbasetest development
    [INFO] ------------------------------------------------------------------------
    [INFO]


    14/08/07 11:05:32 INFO zookeeper.ClientCnxn: Socket connection established to myhadoopcluster/10.10.200.33:2181, initiating session
    14/08/07 11:05:32 INFO zookeeper.ClientCnxn: Session establishment complete on server myhadoopcluster/10.10.200.33:2181, sessionid = 0x247a0b6b3ac0109, negotiated timeout = 300000
    14/08/07 11:05:33 INFO client.ConnectionManager$HConnectionImplementation: Closing zookeeper sessionid=0x247a0b6b3ac0109
    14/08/07 11:05:33 INFO zookeeper.ZooKeeper: Session: 0x247a0b6b3ac0109 closed
    14/08/07 11:05:33 INFO zookeeper.ClientCnxn: EventThread shut down
    14/08/07 11:05:33 INFO token.TokenUtil: Obtained token HBASE_AUTH_TOKEN for user bkersbergen@BOLCOM.NET on cluster ec65ff69-e715-425c-8277-afaa578a3135
    14/08/07 11:05:33 INFO hdfs.DFSClient: Created HDFS_DELEGATION_TOKEN token 3216 for bkersbergen on ha-hdfs:hdp-b
    14/08/07 11:05:33 INFO security.TokenCache: Got dt for hdfs://hdp-b:8020; Kind: HDFS_DELEGATION_TOKEN, Service: ha-hdfs:hdp-b, Ident: (HDFS_DELEGATION_TOKEN token 3216 for bkersbergen)
    14/08/07 11:05:59 INFO input.FileInputFormat: Total input paths to process : 1
    14/08/07 11:06:00 INFO mapreduce.JobSubmitter: number of splits:1
    14/08/07 11:06:00 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1407336338642_0221
    14/08/07 11:06:00 INFO mapreduce.JobSubmitter: Kind: HDFS_DELEGATION_TOKEN, Service: ha-hdfs:hdp-b, Ident: (HDFS_DELEGATION_TOKEN token 3216 for bkersbergen)
    14/08/07 11:06:00 WARN token.Token: Cannot find class for token kind HBASE_AUTH_TOKEN
    14/08/07 11:06:00 WARN token.Token: Cannot find class for token kind HBASE_AUTH_TOKEN
    Kind: HBASE_AUTH_TOKEN, Service: ec65ff69-e715-425c-8277-afaa578a3135, Ident: 00 00 00 2c 08 00 12 16 62 6b 65 72 73 62 65 72 67 65 6e 40 42 4f 4c 43 4f 4d 2e 4e 45 54 18 0f 20 c6 ad dd fd fa 28 28 c6 b5 8f 9e fd 28 30 3f
    14/08/07 11:06:02 INFO impl.YarnClientImpl: Submitted application application_1407336338642_0221
    14/08/07 11:06:02 INFO mapreduce.Job: The url to track the job: http://myhadoopcluster:8088/proxy/application_1407336338642_0221/
    14/08/07 11:06:02 INFO mapreduce.Job: Running job: job_1407336338642_0221
    14/08/07 11:06:11 INFO mapreduce.Job: Job job_1407336338642_0221 running in uber mode : false
    14/08/07 11:06:15 INFO mapreduce.Job:  map 0% reduce 0%
    14/08/07 11:06:25 INFO mapreduce.Job:  map 100% reduce 0%
    14/08/07 11:06:30 INFO mapreduce.Job:  map 100% reduce 100%
    14/08/07 11:06:34 INFO mapreduce.Job: Job job_1407336338642_0221 completed successfully
    14/08/07 11:06:34 INFO mapreduce.Job: Counters: 49
        File System Counters
                FILE: Number of bytes read=3726
                FILE: Number of bytes written=266595
                FILE: Number of read operations=0
                FILE: Number of large read operations=0
                FILE: Number of write operations=0
                HDFS: Number of bytes read=236
                HDFS: Number of bytes written=2506
                HDFS: Number of read operations=6
                HDFS: Number of large read operations=0
                HDFS: Number of write operations=2
        Job Counters
                Launched map tasks=1
                Launched reduce tasks=1
                Rack-local map tasks=1
                Total time spent by all maps in occupied slots (ms)=6900
                Total time spent by all reduces in occupied slots (ms)=7378
                Total time spent by all map tasks (ms)=6900
                Total time spent by all reduce tasks (ms)=3689
                Total vcore-seconds taken by all map tasks=6900
                Total vcore-seconds taken by all reduce tasks=3689
                Total megabyte-seconds taken by all map tasks=28262400
                Total megabyte-seconds taken by all reduce tasks=30220288
        Map-Reduce Framework
                Map input records=8
                Map output records=8
                Map output bytes=11034
                Map output materialized bytes=3722
                Input split bytes=115
                Combine input records=0
                Combine output records=0
                Reduce input groups=8
                Reduce shuffle bytes=3722
                Reduce input records=8
                Reduce output records=8
                Spilled Records=16
                Shuffled Maps =1
                Failed Shuffles=0
                Merged Map outputs=1
                GC time elapsed (ms)=98
                CPU time spent (ms)=3050
                Physical memory (bytes) snapshot=1438912512
                Virtual memory (bytes) snapshot=12341719040
                Total committed heap usage (bytes)=3063414784
        Shuffle Errors
                BAD_ID=0
                CONNECTION=0
                IO_ERROR=0
                WRONG_LENGTH=0
                WRONG_MAP=0
                WRONG_REDUCE=0
        File Input Format Counters
                Bytes Read=121
        File Output Format Counters
                Bytes Written=2506



