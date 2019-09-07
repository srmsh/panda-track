package org.track;

import com.company.Main;
import org.junit.jupiter.api.Test;
import org.panda.asm.ClassReader;
import org.panda.asm.ClassWriter;
import org.track.asm.TrackClassVisitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TrackAopTest {

    @Test
    void transform() throws IOException {
        ClassReader cr = new ClassReader(Main.class.getName());
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        TrackClassVisitor visitor = new TrackClassVisitor(cw);
        cr.accept(visitor, ClassReader.EXPAND_FRAMES);
        File file = new File("./Main.class");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(cw.toByteArray());
        fos.close();
    }
}