package org.track.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.track.store.model.LinkedSpan;
import org.track.store.model.SpanData;

import static org.objectweb.asm.Opcodes.*;

public class TrackMethodVisitor extends MethodVisitor {

    private int time;

    private int linkedSpan;

    private String methodName;

    private String owner;

    private AnalyzerAdapter analyzerAdapter;

    private LocalVariablesSorter localVariablesSorter;

    TrackMethodVisitor(MethodVisitor methodVisitor) {
        super(ASM7, methodVisitor);
    }

    void setAnalyzerAdapter(AnalyzerAdapter analyzerAdapter) {
        this.analyzerAdapter = analyzerAdapter;
    }

    void setLocalVariablesSorter(LocalVariablesSorter localVariablesSorter) {
        this.localVariablesSorter = localVariablesSorter;
    }

    AnalyzerAdapter getAnalyzerAdapter() {
        return analyzerAdapter;
    }

    LocalVariablesSorter getLocalVariablesSorter() {
        return localVariablesSorter;
    }

    void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public void visitCode() {
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
        time = localVariablesSorter.newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(LSTORE, time);
        linkedSpan = localVariablesSorter.newLocal(Type.getType(LinkedSpan.class));
        mv.visitMethodInsn(INVOKESTATIC, "org/track/store/TraceHolder", "get", "()Lorg/track/store/model/LinkedSpan;", false);
        mv.visitVarInsn(ASTORE, linkedSpan);
        mv.visitCode();
    }

    @Override
    public void visitInsn(int opcode) {
        if (((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW)) {
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
            mv.visitVarInsn(LLOAD, time);
            mv.visitInsn(LSUB);
            mv.visitVarInsn(LSTORE, time);
            int spanData = localVariablesSorter.newLocal(Type.getType(SpanData.class));
            mv.visitTypeInsn(NEW, "org/track/store/model/SpanData");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "org/track/store/model/SpanData", "<init>", "()V", false);
            mv.visitVarInsn(ASTORE, spanData);
            mv.visitVarInsn(ALOAD, spanData);
            mv.visitLdcInsn(owner);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/track/store/model/SpanData", "setClassName", "(Ljava/lang/String;)V", false);
            mv.visitVarInsn(ALOAD, spanData);
            mv.visitLdcInsn(methodName);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/track/store/model/SpanData", "setMethodName", "(Ljava/lang/String;)V", false);
            mv.visitVarInsn(ALOAD, spanData);
            mv.visitVarInsn(LLOAD, time);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/track/store/model/SpanData", "setTime", "(J)V", false);
            mv.visitVarInsn(ALOAD, linkedSpan);
            mv.visitVarInsn(ALOAD, spanData);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/track/store/model/LinkedSpan", "join", "(Lorg/track/store/model/SpanData;)V", false);
            mv.visitMethodInsn(INVOKESTATIC, "org/track/store/TraceHolder", "countDown", "()V", false);
        }
        super.visitInsn(opcode);
    }
}
