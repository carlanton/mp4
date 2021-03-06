package io.lindstrom.mp4.boxes;

import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.support.AbstractBox;

import java.nio.ByteBuffer;
import java.util.Objects;

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

    public ByteBuffer getContent() {
        return content.asReadOnlyBuffer();
    }

    @Override
    public String toString() {
        return "GenericBox{" +
                "type=" + Mp4Utils.boxType(type) + ", size=" + getSize() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericBox that = (GenericBox) o;
        return type == that.type &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, content);
    }
}
