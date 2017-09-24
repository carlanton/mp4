package io.lindstrom.mp4.boxes.iso14496.part12;

import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.support.AbstractFullBox;
import org.mp4parser.tools.IsoTypeReader;
import org.mp4parser.tools.IsoTypeWriter;

import java.nio.ByteBuffer;

public class TrackExtendsBox extends AbstractFullBox {
    private static final int TYPE = Mp4Utils.boxType("trex");

    private final long trackId;
    private final long defaultSampleDescriptionIndex;
    private final long defaultSampleDuration;
    private final long defaultSampleSize;
    private final SampleFlags defaultSampleFlags;

    public TrackExtendsBox(ByteBuffer content) {
        super(content);
        trackId = IsoTypeReader.readUInt32(content);
        defaultSampleDescriptionIndex = IsoTypeReader.readUInt32(content);
        defaultSampleDuration = IsoTypeReader.readUInt32(content);
        defaultSampleSize = IsoTypeReader.readUInt32(content);
        defaultSampleFlags = new SampleFlags(IsoTypeReader.readUInt32(content));
    }

    @Override
    protected void writeContent(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeUInt32(byteBuffer, trackId);
        IsoTypeWriter.writeUInt32(byteBuffer, defaultSampleDescriptionIndex);
        IsoTypeWriter.writeUInt32(byteBuffer, defaultSampleDuration);
        IsoTypeWriter.writeUInt32(byteBuffer, defaultSampleSize);
        IsoTypeWriter.writeUInt32(byteBuffer, defaultSampleFlags.asUInt32());
    }

    @Override
    public long getSize() {
        return HEADER_SIZE + 5 * 4 + 4;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "TrackExtendsBox{" +
                "trackId=" + trackId +
                ", defaultSampleDescriptionIndex=" + defaultSampleDescriptionIndex +
                ", defaultSampleDuration=" + defaultSampleDuration +
                ", defaultSampleSize=" + defaultSampleSize +
                ", defaultSampleFlags=" + defaultSampleFlags +
                ", version=" + version +
                ", flags=" + flags +
                '}';
    }
}
