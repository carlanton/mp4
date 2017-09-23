package io.lindstrom.mp4.box.iso;

import io.lindstrom.mp4.IsoTypeReader;
import io.lindstrom.mp4.IsoTypeWriter;
import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.box.AbstractFullBox;
import io.lindstrom.mp4.box.SampleFlags;

import java.nio.ByteBuffer;

public class TrackFragmentHeaderBox extends AbstractFullBox {
    public static final int TYPE = Mp4Utils.boxType("tfhd");

    private long trackId;
    private long baseDataOffset = -1;
    private long sampleDescriptionIndex;
    private long defaultSampleDuration = -1;
    private long defaultSampleSize = -1;
    private SampleFlags defaultSampleFlags;
    private boolean durationIsEmpty;
    private boolean defaultBaseIsMoof;

    public TrackFragmentHeaderBox(ByteBuffer content) {
        super(content);
        trackId = IsoTypeReader.readUInt32(content);
        if ((flags & 0x1) == 1) { //baseDataOffsetPresent
            baseDataOffset = IsoTypeReader.readUInt64(content);
        }
        if ((flags & 0x2) == 0x2) { //sampleDescriptionIndexPresent
            sampleDescriptionIndex = IsoTypeReader.readUInt32(content);
        }
        if ((flags & 0x8) == 0x8) { //defaultSampleDurationPresent
            defaultSampleDuration = IsoTypeReader.readUInt32(content);
        }
        if ((flags & 0x10) == 0x10) { //defaultSampleSizePresent
            defaultSampleSize = IsoTypeReader.readUInt32(content);
        }
        if ((flags & 0x20) == 0x20) { //defaultSampleFlagsPresent
            defaultSampleFlags = new SampleFlags(content);
        }
        if ((flags & 0x10000) == 0x10000) { //durationIsEmpty
            durationIsEmpty = true;
        }
        if ((flags & 0x20000) == 0x20000) { //defaultBaseIsMoof
            defaultBaseIsMoof = true;
        }
    }

    @Override
    protected void writeContent(ByteBuffer byteBuffer) {
        Mp4Utils.writeUInt32(byteBuffer, trackId);

        if ((flags & 0x1) == 1) { //baseDataOffsetPresent
            IsoTypeWriter.writeUInt64(byteBuffer, baseDataOffset);
        }
        if ((flags & 0x2) == 0x2) { //sampleDescriptionIndexPresent
            IsoTypeWriter.writeUInt32(byteBuffer, sampleDescriptionIndex);
        }
        if ((flags & 0x8) == 0x8) { //defaultSampleDurationPresent
            IsoTypeWriter.writeUInt32(byteBuffer, defaultSampleDuration);
        }
        if ((flags & 0x10) == 0x10) { //defaultSampleSizePresent
            IsoTypeWriter.writeUInt32(byteBuffer, defaultSampleSize);
        }
        if ((flags & 0x20) == 0x20) { //defaultSampleFlagsPresent
            defaultSampleFlags.getContent(byteBuffer);
        }
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public long getSize() {
        long size = HEADER_SIZE + 8;

        if ((flags & 0x1) == 1) { //baseDataOffsetPresent
            size += 8;
        }
        if ((flags & 0x2) == 0x2) { //sampleDescriptionIndexPresent
            size += 4;
        }
        if ((flags & 0x8) == 0x8) { //defaultSampleDurationPresent
            size += 4;
        }
        if ((flags & 0x10) == 0x10) { //defaultSampleSizePresent
            size += 4;
        }
        if ((flags & 0x20) == 0x20) { //defaultSampleFlagsPresent
            size += 4;
        }
        return size;
    }

    @Override
    public String toString() {
        return "TrackFragmentHeaderBox{" +
                "trackId=" + trackId +
                ", baseDataOffset=" + baseDataOffset +
                ", sampleDescriptionIndex=" + sampleDescriptionIndex +
                ", defaultSampleDuration=" + defaultSampleDuration +
                ", defaultSampleSize=" + defaultSampleSize +
                ", defaultSampleFlags=" + defaultSampleFlags +
                ", durationIsEmpty=" + durationIsEmpty +
                ", defaultBaseIsMoof=" + defaultBaseIsMoof +
                ", version=" + version +
                ", flags=" + flags +
                '}';
    }
}
