package org.example.chapter003;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class DirectBuffer {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
//        String infile = "E://test.txt";
        String infile = "E://软件//CentOS-8.1.1911-x86_64-dvd1.iso";
        FileInputStream fin = new FileInputStream(infile);
        FileChannel fcin = fin.getChannel();

        String outfile = String.format("E://CentOS-8.1.1911-x86_64-dvd1.iso");
        FileOutputStream fout = new FileOutputStream(outfile);
        FileChannel fcout = fout.getChannel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(102400);

        while (true) {
            buffer.clear();

            int r = fcin.read(buffer);

            if (r == -1) {
                break;
            }

            buffer.flip();
            fcout.write(buffer);
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
