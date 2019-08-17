package org.track;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class TrackAgent {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        ClassFileTransformer transformer = new TrackAop();
        instrumentation.addTransformer(transformer);
    }

}
