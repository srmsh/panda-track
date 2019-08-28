package org.track.store;

import org.track.store.model.LinkedSpan;

public class TraceHolder {

    private static PandaStore store = new PrintStore();

    private static ThreadLocal<LinkedSpan> local = new ThreadLocal<>();

    private static ThreadLocal<Integer> countLocal = new ThreadLocal<>();

    public static void set(LinkedSpan data) {
        local.set(data);
    }

    public static LinkedSpan get() {
        LinkedSpan span = local.get();
        if (null == span) {
            span = new LinkedSpan();
            countLocal.set(0);
            local.set(span);
        }
        Integer count =  countLocal.get();
        countLocal.set(++count);
        return span;
    }

    public static void countDown() {
        Integer count =  countLocal.get();
        --count;
        if (count == 0) {
            LinkedSpan linkedSpan = local.get();
            store.save(linkedSpan);
            clear();
        } else {
            countLocal.set(count);
        }
    }

    private static void clear() {
        local.remove();
        countLocal.remove();
    }

}
