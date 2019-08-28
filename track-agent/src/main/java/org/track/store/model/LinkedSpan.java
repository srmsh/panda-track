package org.track.store.model;

public class LinkedSpan {

    private transient Node<SpanData> first;

    private transient Node<SpanData> last;

    private transient long id;

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
        Node<SpanData> newLast = new Node<>(data);
        if (null == first) {
            first = newLast;
            last = newLast;
            return;
        }
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
