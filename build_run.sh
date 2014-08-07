#!/bin/bash
mvn install
hadoop jar target/hbasetest-development-job.jar input output
