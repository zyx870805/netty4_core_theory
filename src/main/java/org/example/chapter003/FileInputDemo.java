package org.example.chapter003;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileInputDemo {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("E://test.txt");

        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);

        buffer.flip();

        while (buffer.remaining() > 0) {
            System.out.println(buffer.get());
        }
        fileInputStream.close();
    }
}
