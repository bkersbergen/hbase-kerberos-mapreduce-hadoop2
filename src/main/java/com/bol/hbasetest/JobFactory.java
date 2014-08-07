package com.bol.hbasetest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.security.User;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

/**
 * @author bkersbergen
 */
public class JobFactory {

    public static Job createJob(final Configuration conf, final String inPath, final String outPath) throws IOException, InterruptedException {
        final Job job = Job.getInstance(conf);

        job.setJarByClass(JobFactory.class);

        FileInputFormat.setInputPaths(job, inPath);

        job.setInputFormatClass(TextInputFormat.class);
        job.setMapperClass(MyMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setNumReduceTasks(0);

        FileSystem.get(conf).delete(new Path(outPath), true);
        FileOutputFormat.setOutputPath(job, new Path(outPath));

        job.setOutputFormatClass(TextOutputFormat.class);

        if (User.isSecurityEnabled()) {
            User.getCurrent().obtainAuthTokenForJob(conf, job);
        }

        return job;
    }
}
