package io.lindstrom.mp4.box;

import io.lindstrom.mp4.Mp4Utils;

import java.nio.ByteBuffer;

public class GenericBox extends AbstractBox {
    private final int type;
    private final ByteBuffer content;

    public GenericBox(int type, ByteBuffer content) {
        this.type = type;
        this.content = content;
    }

    @Override
    public long getSize() {
        return HEADER_SIZE + content.limit();
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void write(ByteBuffer content) {
        content.put(this.content);
    }

    @Override
    public String toString() {
        return "GenericBox{" +
                "type=" + Mp4Utils.boxType(type) +
                '}';
    }
}
