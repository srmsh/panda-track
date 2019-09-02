package org.track.store;

import org.track.store.model.LinkedSpan;

public interface PandaStore {

    void start();

    boolean save(LinkedSpan data);

}
