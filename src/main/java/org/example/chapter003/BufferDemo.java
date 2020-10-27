package org.example.chapter003;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BufferDemo {

    public static void main(String[] args) throws IOException {
        FileInputStream fin = new FileInputStream("E://test.txt");

        FileChannel fc = fin.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(10);
        output("初始化", buffer);
        fc.read(buffer);

        output("调用read（）", buffer);

        buffer.flip();
        output("调用flip（）", buffer);

        while(buffer.remaining() > 0) {
            byte b = buffer.get();
            System.out.println(b);
        }

        output("调用get()", buffer);
        buffer.clear();
        output("调用clear()", buffer);

        fin.close();
    }

    public static void output(String step, Buffer buffer) {
        System.out.println(step + " : ");
        System.out.print("capacity: " + buffer.capacity() + ", ");
        System.out.print("position: " + buffer.position() + ", ");
        System.out.print("limit: " + buffer.limit());
        System.out.println();
    }
}
