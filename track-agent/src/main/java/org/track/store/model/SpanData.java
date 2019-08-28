package org.track.store.model;

public class SpanData {
    /**
     * 耗时
     */
    private long time;

    private String className;

    private String methodName;

    private String args;

    private int traceId;

    private int spanId;

    private int parentSpanId;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public int getTraceId() {
        return traceId;
    }

    public void setTraceId(int traceId) {
        this.traceId = traceId;
    }

    public int getSpanId() {
        return spanId;
    }

    public void setSpanId(int spanId) {
        this.spanId = spanId;
    }

    public int getParentSpanId() {
        return parentSpanId;
    }

    public void setParentSpanId(int parentSpanId) {
        this.parentSpanId = parentSpanId;
    }

    @Override
    public String toString() {
        return "SpanData{" +
                "time=" + time +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", args='" + args + '\'' +
                ", traceId=" + traceId +
                ", spanId=" + spanId +
                ", parentSpanId=" + parentSpanId +
                '}';
    }
}
