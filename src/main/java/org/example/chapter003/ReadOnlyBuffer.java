package org.example.chapter003;

import java.nio.ByteBuffer;

public class ReadOnlyBuffer {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        for(int i = 0; i < buffer.capacity(); ++i) {
            buffer.put((byte) i);
        }

        ByteBuffer readOnly = buffer.asReadOnlyBuffer();

        for (int i = 0; i < buffer.capacity(); ++i) {
            byte b = buffer.get(i);
            b *= 10;
            buffer.put(i, b);
        }

        readOnly.position(0);
        readOnly.limit(buffer.capacity());

        while (readOnly.remaining() > 0) {
            System.out.println(readOnly.get());
        }
    }
}
