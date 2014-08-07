package com.bol.hbasetest;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author bkersbergen
 */
public class MyMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    private static final Logger LOG = LoggerFactory.getLogger(MyMapper.class);

    private HbaseDao hbaseDao;
    private Text output = new Text();


    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        try {
            hbaseDao = new HbaseDao();
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }
    }

    @Override
    protected void map(LongWritable bytePos, Text textLine, Context context) throws IOException, InterruptedException {
        String globalId = textLine.toString();
        String result = hbaseDao.getProductJson(globalId);
        if (result == null) {
            return;
        }
        output.set(result);
        context.write(output, NullWritable.get());
    }
}
