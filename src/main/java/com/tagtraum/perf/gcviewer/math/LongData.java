package com.tagtraum.perf.gcviewer.math;

import java.io.Serializable;

/**
 *
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 * Date: May 20, 2005
 * Time: 5:08:33 PM
 *
 */
public class LongData implements Serializable  {

    private int n;  // @todo should this be long too?
    private long sum;
    private long sumSquares;
    private long min = Long.MAX_VALUE;
    private long max = Long.MIN_VALUE;

    public void add(long x) {
        sum += x;
        if( sum < 0 ) {
            // @todo this is happening when parsing large IBM logs (49.0: [undefined 49.0: [PSYoungGen: 83708K->7341693K(262154066K), 0.0 secs]49.0: [PSOldGen: 0K->0K(13797581K), 0.0 secs] 83708K->7341693K(275951648K), 5.577507 secs])
        }
        sumSquares += (x*x);
        n++;
        min = Math.min(min, x);
        max = Math.max(max, x);
    }

    public void add(long x, long weight) {
        sum += x * weight;
        n += weight;
        sumSquares += (x * x * weight);
        min = Math.min(min, x);
        max = Math.max(max, x);
    }

    public int getN() {
        return n;
    }

    public long getSum() {
        return sum;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    public double average() {
        if (n == 0) throw new IllegalStateException("n == 0");
        return ((double)sum) / ((double)n);
    }

    public double standardDeviation() {
        if (n == 0) throw new IllegalStateException("n == 0");
        if (n==1) return 0;
        return Math.sqrt(variance());
    }

    public double variance() {
        if (n == 0) throw new IllegalStateException("n == 0");
        if (n==1) return 0;
        final double dsum = sum;
        final double dn = n;
        return (sumSquares - dsum*dsum/dn)/(dn-1);
    }

    public void reset() {
        sum = 0;
        sumSquares = 0;
        n = 0;
    }

    public static long weightedAverage(long[] n, int[] weight) {
        long sum = 0;
        int m = 0;
        for (int i=0; i<n.length; i++) {
            sum += n[i]*weight[i];
            m += weight[i];
        }
        return sum / m;
    }
}
