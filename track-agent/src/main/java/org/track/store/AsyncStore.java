package org.track.store;

import org.track.store.model.LinkedSpan;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AsyncStore implements PandaStore {

    protected BlockingQueue<LinkedSpan> queue = new LinkedBlockingQueue<>();

    protected volatile boolean started = false;

    @Override
    public boolean save(LinkedSpan data) {
        return queue.offer(data);
    }

    @Override
    public void start() {
        started = true;
        new Thread(() -> {
            while (started) {
                try {
                    LinkedSpan data = queue.take();
                    report(data);
                } catch (Throwable e) {
                    //
                }
            }
        }).start();
    }

//    public void drain() {
//        for (int i = 0; i < 5; i++) {
//            System.out.println("---------------");
//            List<LinkedSpan> data = new ArrayList<>();
//            while (!queue.isEmpty() && data.size() < 20) {
//                try {
//                    data.add(queue.poll(1, TimeUnit.SECONDS));
//                } catch (InterruptedException e) {
//                    //容错
//                }
//            }
//            if (!data.isEmpty()) {
//                reportQueue.offer(data);
//            }
//            data.clear();
//        }
//    }

    public abstract void report(LinkedSpan span);
}
