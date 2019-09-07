package org.track;

import org.panda.asm.ClassReader;
import org.panda.asm.ClassWriter;
import org.track.asm.TrackClassVisitor;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;

public class TrackAop implements ClassFileTransformer {

    // TODO: 2019/9/7 缓存class信息   缓存会不会造成热部署失效？

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        String[] packages = System.getProperty("panda.packages", "com/company").split(",");
        // TODO: 2019/8/28 可配置
        if (Arrays.stream(packages).noneMatch(className::startsWith)) {
            return classfileBuffer;
        }
        ClassReader cr;
        try {
            cr = new ClassReader(className);
        } catch (IOException e) {
            // 找不到的类 可能是已经被代理的类修改了类名
            return classfileBuffer;
        }
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        TrackClassVisitor visitor = new TrackClassVisitor(cw);
        cr.accept(visitor, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }

}
