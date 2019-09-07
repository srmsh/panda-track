package org.track.store.model;

/**
 * 线程绑定   目前不会有并发问题
 */
public class LinkedSpan {

    private Node<SpanData> first;

    private Node<SpanData> last;

    private int size = 0;

    private long id;

    static final class Node<SpanData> {

        SpanData data;

        Node<SpanData> prev;

        Node<SpanData> next;

        Node(SpanData data) {
            this.data = data;
        }
    }

    public LinkedSpan() {
        id = Thread.currentThread().getId();
    }

    public void join(SpanData data) {
        int prevSize = size++;
        data.setSpanId(size);
        Node<SpanData> newLast = new Node<>(data);
        if (null == first) {
            data.setParentSpanId(size);
            first = newLast;
            last = newLast;
            return;
        }
        data.setParentSpanId(prevSize);
        Node<SpanData> oldLast = last;
        newLast.prev = oldLast;
        last = newLast;
        oldLast.next = newLast;
    }

    public boolean hasSpan() {
        return null != first;
    }

    public SpanData read() {
        Node<SpanData> f = first;
        Node<SpanData> newFirst = f.next;
        SpanData data = f.data;
        f.data = null;
        f.next = f;
        first = newFirst;
        if (null == newFirst) {
            last = null;
        } else {
            newFirst.prev = null;
        }
        return data;
    }
}
