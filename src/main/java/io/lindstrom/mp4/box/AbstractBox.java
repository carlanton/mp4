package io.lindstrom.mp4.box;

import io.lindstrom.mp4.Mp4Utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public abstract class AbstractBox implements Box {
    protected static final int HEADER_SIZE = 8;

    @Override
    public void write(WritableByteChannel channel) throws IOException {
        ByteBuffer box = ByteBuffer.allocate(Math.toIntExact(getSize()));
        Mp4Utils.writeUInt32(box, getSize());
        box.putInt(getType());

        write(box);

        box.flip();
        channel.write(box);
    }

    public abstract void write(ByteBuffer content);


    protected int getContentSize() {
        return Math.toIntExact(getSize() - HEADER_SIZE);
    }
}
