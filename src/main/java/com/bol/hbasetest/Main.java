package com.bol.hbasetest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Example of how to do HBase calls in a mapper or reducer with Kerberos security configured in hadoop.
 *
 * @author bkersbergen
 */
public class Main extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            return -1;
        }
        String input = args[0];
        String output = args[1];
        Job job = JobFactory.createJob(getConf(), input, output );

        boolean success = job.waitForCompletion(true);

        int exitcode = success ? 0 : -1;
        return exitcode;
    }
    public static void main(final String[] args) throws Exception {
        Configuration config = HBaseConfiguration.create();
        System.exit(ToolRunner.run(config, new Main(), args));
    }
}
