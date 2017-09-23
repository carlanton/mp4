package io.lindstrom.mp4.box;

import io.lindstrom.mp4.Mp4Utils;

import java.nio.ByteBuffer;

public abstract class AbstractFullBox extends AbstractBox {
    protected int version;
    protected int flags;

    public AbstractFullBox(int version, int flags) {
        this.version = version;
        this.flags = flags;
    }

    public AbstractFullBox(ByteBuffer content) {
        this.version = Mp4Utils.readUInt8(content);
        this.flags = Mp4Utils.readUInt24(content);
    }

    @Override
    public void write(ByteBuffer content) {
        Mp4Utils.writeUInt32(content, (version << 24) | flags);
        writeContent(content);
    }

    protected abstract void writeContent(ByteBuffer content);
}
