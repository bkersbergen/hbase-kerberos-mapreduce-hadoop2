package com.bol.hbasetest;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author bkersbergen
 */
public class HbaseDao {

    private static final Logger LOG = LoggerFactory.getLogger(HbaseDao.class);

    private static final byte[] B_CF_JSON = Bytes.toBytes("c"); // catalog
    private static final byte[] B_COL_JSON = Bytes.toBytes("p"); // products
    private HTable table = null;
    private static final String dbName = "reco_product_catalog";

    public HbaseDao() throws IOException {
        Configuration hbaseConfig = HBaseConfiguration.create();
        // log the hbase config
        for (Map.Entry<String, String> entry : hbaseConfig) {
            LOG.info(entry.getKey() + ","  +entry.getValue());
        }
        table = new HTable(hbaseConfig, dbName );
    }

    public String getProductJson(String globalId) throws IOException {
        String reverseGlobalId = StringUtils.reverse(globalId);
        Get get = new Get(Bytes.toBytes(reverseGlobalId));
        get.setMaxVersions(1);
        Result result = table.get(get);
        if (!result.isEmpty()) {
            return Bytes.toString(result.getValue(B_CF_JSON, B_COL_JSON));
        } else {
            return null;
        }
    }
}
