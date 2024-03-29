package org.track.asm;

import org.panda.asm.Label;
import org.panda.asm.MethodVisitor;
import org.panda.asm.Type;
import org.panda.asm.commons.AnalyzerAdapter;
import org.panda.asm.commons.LocalVariablesSorter;
import org.track.store.model.LinkedSpan;
import org.track.store.model.SpanData;

import static org.panda.asm.Opcodes.*;

public class TrackMethodVisitor extends MethodVisitor {

    private int time;

    private int linkedSpan;

    private String methodName;

    private int spanData;

    private String owner;

    private AnalyzerAdapter analyzerAdapter;

    private LocalVariablesSorter localVariablesSorter;

    private Label tryCatchStart,tryCatchEnd;

    TrackMethodVisitor(MethodVisitor methodVisitor) {
        super(ASM7, methodVisitor);
        tryCatchStart = new Label();
        tryCatchEnd = new Label();
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

        spanData = localVariablesSorter.newLocal(Type.getType(SpanData.class));
        mv.visitTypeInsn(NEW, "org/track/store/model/SpanData");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "org/track/store/model/SpanData", "<init>", "()V", false);
        mv.visitVarInsn(ASTORE, spanData);
        mv.visitLabel(tryCatchStart);
        mv.visitCode();
    }

    @Override
    public void visitInsn(int opcode) {
        if (((opcode >= IRETURN && opcode <= RETURN))) {
            endMethodVisitor();
        }
        super.visitInsn(opcode);
    }

    private void endMethodVisitor() {
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
        mv.visitVarInsn(LLOAD, time);
        mv.visitInsn(LSUB);
        mv.visitVarInsn(LSTORE, time);

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

    @Override
    public void visitEnd() {
        mv.visitLabel(tryCatchEnd);
        mv.visitTryCatchBlock(tryCatchStart, tryCatchEnd, tryCatchEnd, null);

        endMethodVisitor();

        mv.visitInsn(ATHROW);

        mv.visitEnd();
    }
}
