package io.lindstrom.mp4;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataBuffer {
    private final ByteBuffer byteBuffer;

    public DataBuffer(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public String read4cc() {
        byte[] codeBytes = new byte[4];
        byteBuffer.get(codeBytes);
        return new String(codeBytes, StandardCharsets.UTF_8);
    }

    public long getUInt32() {
        long i = byteBuffer.getInt();
        if (i < 0) {
            i += 1l << 32;
        }
        return i;
    }

    public int remaining() {
        return byteBuffer.remaining();
    }
}
