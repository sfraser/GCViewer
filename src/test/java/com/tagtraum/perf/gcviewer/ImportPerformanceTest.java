package com.tagtraum.perf.gcviewer;

import com.tagtraum.perf.gcviewer.imp.DataReader;
import com.tagtraum.perf.gcviewer.imp.DataReaderFactory;
import com.tagtraum.perf.gcviewer.math.LongData;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Jun 7, 2005
 * Time: 11:42:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImportPerformanceTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        LongData performanceData = new LongData();
        for (int i=0; i<10; i++) {
            long start = System.currentTimeMillis();
            DataReader dataReader = new DataReaderFactory().getDataReader(new FileInputStream(args[0]));
            dataReader.read();
            performanceData.add(System.currentTimeMillis() - start);
        }
        
        printIntData(args[0], performanceData);
    }
    
    private static void printIntData(String filename, LongData data) {
        System.out.printf("results for %1$s in ms: %2$d runs%nmin, max, avg, stddev, sum%n" +
        		"%3$s, %4$s, %5$s, %6$s, %7$s",
        		filename, data.getN(), 
        		data.getMin(), data.getMax(), data.average(), data.standardDeviation(), data.getSum());
    }
}
