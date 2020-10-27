package org.example.chapter003;

import java.nio.ByteBuffer;

public class BufferWrap {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        byte array[] = new byte[10];

        ByteBuffer buffer1 = ByteBuffer.wrap(array);
    }
}
