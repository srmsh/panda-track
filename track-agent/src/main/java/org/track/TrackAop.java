package org.track;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.track.asm.TrackClassVisitor;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;

public class TrackAop implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        String[] packages = System.getProperty("panda.packages", "com/company").split(",");
        // TODO: 2019/8/28 可配置
        if (Arrays.stream(packages).noneMatch(className::contains)) {
            return classfileBuffer;
        }
        System.out.println(className);
        ClassReader cr;
        try {
            cr = new ClassReader(className);
        } catch (IOException e) {
            // 被代理的类
            return classfileBuffer;
        }
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        TrackClassVisitor visitor = new TrackClassVisitor(cw);
        cr.accept(visitor, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }

}
