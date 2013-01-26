package com.tagtraum.perf.gcviewer.model;

import java.util.Map;
import java.util.TreeMap;


/**
 * The GCEvent is the type of event that contains memory (preused, postused, total) and 
 * pause information.
 *
 * <p>Date: Jan 30, 2002</p>
 * <p>Time: 5:05:43 PM</p>
 * @author <a href="mailto:hs@tagtraum.com">Hendrik Schreiber</a>
 * @author <a href="mailto:gcviewer@gmx.ch">Joerg Wuethrich</a>
 */
public class GCEvent extends AbstractGCEvent<GCEvent> {

    /** Used before GC in KB */
    private long preUsed;
    
    /** Used after GC in KB */
    private long postUsed;
    
    /** Capacity in KB */
    private long total;
    
    /** Pause in seconds */
    private double pause;
    
    /** the generationMap contains all detail events and additionally the inferred events as well. */
    private Map<Generation, GCEvent> generationMap = new TreeMap<Generation, GCEvent>();
    
    public GCEvent() {
    }

    public GCEvent(double timestamp, long preUsed, long postUsed, long total, double pause, Type type) {
        this.setTimestamp(timestamp);
        this.preUsed = preUsed;
        this.postUsed = postUsed;
        this.total = total;
        this.pause = pause;
        this.setType(type);
    }

    @Override
    public void add(GCEvent event) {
        super.add(event);
        
        generationMap.put(event.getType().getGeneration(), event);
    }
    
    /**
     * Returns information on young generation. If it was not present in the gc log, but 
     * tenured was, it is inferred from there (with -XX:+PrintGCDetails). Otherwise it is 
     * <code>null</code> (without -XX:+PrintGCDetails).
     * 
     * @return Information on young generation if possible, <code>null</code> otherwise.
     */
    public GCEvent getYoung() {
        GCEvent young = generationMap.get(Generation.YOUNG);
        if (young == null) {
            GCEvent tenured = generationMap.get(Generation.TENURED);
            if (tenured != null) {
                young = new GCEvent();
                young.setTimestamp(tenured.getTimestamp());
                young.setPreUsed(preUsed - tenured.getPreUsed());
                young.setPostUsed(postUsed - tenured.getPostUsed());
                young.setTotal(total - tenured.getTotal());
                young.setPause(tenured.getPause());
                
                generationMap.put(Generation.YOUNG, young);
            }
        }
        
        return young;
    }
    
    /**
     * Returns information on young generation. If it was not present in the gc log, but 
     * tenured was, it is inferred from there (with -XX:+PrintGCDetails). Otherwise it 
     * is <code>null</code> (without -XX:+PrintGCDetails).
     * 
     * @return Information on young generation if possible, <code>null</code> otherwise.
     */
    public GCEvent getTenured() {
        GCEvent tenured = generationMap.get(Generation.TENURED);
        if (tenured == null) {
            GCEvent young = generationMap.get(Generation.YOUNG);
            if (young != null) {
                tenured = new GCEvent();
                tenured.setTimestamp(young.getTimestamp());
                tenured.setPreUsed(preUsed - young.getPreUsed());
                tenured.setPostUsed(postUsed - young.getPostUsed());
                tenured.setTotal(total - young.getTotal());
                tenured.setPause(young.getPause());
                
                generationMap.put(Generation.TENURED, tenured);
            }
        }
        
        return tenured;
    }
    
    /**
     * Returns information on perm generation. If it was not present in the gc log,
     * <code>null</code> will be returned, because the values cannot be inferred.
     * 
     * @return Information on perm generation or <code>null</code> if not present.
     */
    public GCEvent getPerm() {
        return generationMap.get(Generation.PERM);
    }
    
    public void setPreUsed(long preUsed) {
        this.preUsed = preUsed;
    }

    public void setPostUsed(long postUsed) {
        this.postUsed = postUsed;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPreUsed() {
        return preUsed;
    }

    public long getPostUsed() {
        return postUsed;
    }

    public long getTotal() {
        return total;
    }

    public void toStringBuffer(StringBuffer sb) {
        sb.append(getTimestamp());
        sb.append(": [");
        sb.append(getType());
        sb.append(' ');
        if (details != null) {
            for (GCEvent event : details) {
                event.toStringBuffer(sb);
            }
            sb.append(' ');
        }
        sb.append(preUsed);
        sb.append("K->");
        sb.append(postUsed);
        sb.append("K(");
        sb.append(total);
        sb.append("K), ");
        sb.append(pause);
        sb.append(" secs]");
    }
    
    public void setPause(double pause) {
        this.pause = pause;
    }

    public double getPause() {
        return pause;
    }

}
