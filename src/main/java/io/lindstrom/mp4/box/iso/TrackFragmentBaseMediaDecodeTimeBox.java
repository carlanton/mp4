package io.lindstrom.mp4.box.iso;

import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.box.AbstractFullBox;

import java.nio.ByteBuffer;

public class TrackFragmentBaseMediaDecodeTimeBox extends AbstractFullBox {
    private static final int TYPE = Mp4Utils.boxType("tfdt");

    private final long baseMediaDecodeTime;

    public TrackFragmentBaseMediaDecodeTimeBox(int version, int flags, long baseMediaDecodeTime) {
        super(version, flags);
        this.baseMediaDecodeTime = baseMediaDecodeTime;
    }

    public TrackFragmentBaseMediaDecodeTimeBox(ByteBuffer content) {
        super(content);
        if (version == 1) {
            baseMediaDecodeTime = Mp4Utils.putUnsignedLong(content);
        } else {
            baseMediaDecodeTime = Mp4Utils.readUInt32(content);
        }
    }

    @Override
    protected void writeContent(ByteBuffer content) {
        if (version == 1) {
            Mp4Utils.writeUInt64(content, baseMediaDecodeTime);
        } else {
            Mp4Utils.writeUInt32(content, baseMediaDecodeTime);
        }
    }

    @Override
    public long getSize() {
        return HEADER_SIZE + (version == 0 ? 8 : 12);
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "TrackFragmentBaseMediaDecodeTimeBox{" +
                "version=" + version +
                ", flags=" + flags +
                ", baseMediaDecodeTime=" + baseMediaDecodeTime +
                '}';
    }
}