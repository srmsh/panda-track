package org.track.asm;

import org.panda.asm.ClassVisitor;
import org.panda.asm.MethodVisitor;
import org.panda.asm.Opcodes;
import org.panda.asm.commons.AnalyzerAdapter;
import org.panda.asm.commons.LocalVariablesSorter;

public class TrackClassVisitor extends ClassVisitor implements Opcodes {

    private boolean isInterface;

    private String owner;

    public TrackClassVisitor(ClassVisitor classVisitor) {
        super(ASM7, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        owner = name;
        this.isInterface = (access & ACC_INTERFACE) != 0;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);

        if (!isInterface && null != methodVisitor && !"<init>".equals(name) && !"<clinit>".equals(name)) {
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
