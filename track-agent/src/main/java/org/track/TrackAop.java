package org.track;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.track.asm.TrackClassVisitor;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class TrackAop implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            ClassReader cr = new ClassReader(className);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            TrackClassVisitor visitor = new TrackClassVisitor(cw);
            cr.accept(visitor, ClassReader.EXPAND_FRAMES);
            return cw.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classfileBuffer;
    }

}
