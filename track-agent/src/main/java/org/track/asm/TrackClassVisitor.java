package org.track.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class TrackClassVisitor extends ClassVisitor implements Opcodes {

    private boolean isInterface;

    private String owner;

    private boolean isSkip = true;

    public TrackClassVisitor(ClassVisitor classVisitor) {
        super(ASM7, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        // TODO: 2019/8/28 可配置
        if (name.contains("com/company")) {
            isSkip = false;
        }
        super.visit(version, access, name, signature, superName, interfaces);
        owner = name;
        this.isInterface = (access & ACC_INTERFACE) != 0;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);

        if (!isInterface && null != methodVisitor && !"<init>".equals(name) && !"<clinit>".equals(name) && !isSkip) {
            TrackMethodVisitor spanMv = new TrackMethodVisitor(methodVisitor);
            spanMv.setAnalyzerAdapter(new AnalyzerAdapter(owner, access, name, descriptor, spanMv));
            spanMv.setLocalVariablesSorter(new LocalVariablesSorter(access, descriptor, spanMv.getAnalyzerAdapter()));
            spanMv.setMethodName(name);
            spanMv.setOwner(owner);
            return spanMv.getLocalVariablesSorter();
        }

        return methodVisitor;
    }
}
