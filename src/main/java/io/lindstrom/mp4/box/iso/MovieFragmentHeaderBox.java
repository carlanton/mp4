package io.lindstrom.mp4.box.iso;

import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.box.AbstractFullBox;

import java.nio.ByteBuffer;

public class MovieFragmentHeaderBox extends AbstractFullBox {
    public static final int TYPE = Mp4Utils.boxType("mfhd");

    private final long sequenceNumber;

    public MovieFragmentHeaderBox(int version, int flags, long sequenceNumber) {
        super(version, flags);
        this.sequenceNumber = sequenceNumber;
    }

    public MovieFragmentHeaderBox(ByteBuffer content) {
        super(content);
        sequenceNumber = Mp4Utils.readUInt32(content);
    }

    @Override
    public long getSize() {
        return HEADER_SIZE + 8;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void writeContent(ByteBuffer byteBuffer) {
        Mp4Utils.writeUInt32(byteBuffer, sequenceNumber);
    }
}
