package org.track.store;

import org.track.store.model.LinkedSpan;

public class PrintStore implements PandaStore {
    @Override
    public void start() {

    }

    @Override
    public boolean save(LinkedSpan data) {
        while (data.hasSpan()) {
            System.out.println(data.read());
        }
        return true;
    }
}
