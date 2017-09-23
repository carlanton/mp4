package io.lindstrom.mp4.box.iso;

import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.box.AbstractFullBox;
import io.lindstrom.mp4.box.SampleFlags;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackRunBox extends AbstractFullBox {
    public static final int TYPE = Mp4Utils.boxType("trun");

    private final int dataOffset;
    private final SampleFlags firstSampleFlags;
    private final List<Entry> entries;

    public TrackRunBox(int version, int flags, int dataOffset, SampleFlags firstSampleFlags, List<Entry> entries) {
        super(version, flags);
        this.dataOffset = dataOffset;
        this.firstSampleFlags = firstSampleFlags;
        this.entries = entries;
    }

    public TrackRunBox(ByteBuffer content) {
        super(content);

        long sampleCount = Mp4Utils.readUInt32(content);

        if ((flags & 0x1) == 1) { //dataOffsetPresent
            dataOffset = Math.toIntExact(Mp4Utils.readUInt32(content));
        } else {
            dataOffset = -1;
        }

        if ((flags & 0x4) == 0x4) { //firstSampleFlagsPresent
            firstSampleFlags = new SampleFlags(content);
        } else {
            firstSampleFlags = null;
        }

        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < sampleCount; i++) {
            long sampleDuration = 0;
            long sampleSize = 0;
            SampleFlags sampleFlags = null;
            int sampleCompositionTimeOffset = 0;

            if ((flags & 0x100) == 0x100) { //sampleDurationPresent
                sampleDuration = Mp4Utils.readUInt32(content);
            }
            if ((flags & 0x200) == 0x200) { //sampleSizePresent
                sampleSize = Mp4Utils.readUInt32(content);
            }
            if ((flags & 0x400) == 0x400) { //sampleFlagsPresent
                sampleFlags = new SampleFlags(content);
            }
            if ((flags & 0x800) == 0x800) { //sampleCompositionTimeOffsetPresent
                sampleCompositionTimeOffset = content.getInt();
            }

            entries.add(new Entry(sampleDuration, sampleSize, sampleFlags, sampleCompositionTimeOffset));
        }
        this.entries = Collections.unmodifiableList(entries);
    }

    @Override
    public long getSize() {
        long size = HEADER_SIZE;

        size += 8;

        if ((flags & 0x1) == 0x1) { //dataOffsetPresent
            size += 4;
        }
        if ((flags & 0x4) == 0x4) { //firstSampleFlagsPresent
            size += 4;
        }

        long entrySize = 0;
        if ((flags & 0x100) == 0x100) { //sampleDurationPresent
            entrySize += 4;
        }
        if ((flags & 0x200) == 0x200) { //sampleSizePresent
            entrySize += 4;
        }
        if ((flags & 0x400) == 0x400) { //sampleFlagsPresent
            entrySize += 4;
        }
        if ((flags & 0x800) == 0x800) { //sampleCompositionTimeOffsetPresent
            entrySize += 4;
        }
        size += entrySize * entries.size();
        return size;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    protected void writeContent(ByteBuffer byteBuffer) {
        Mp4Utils.writeUInt32(byteBuffer, entries.size());

        if ((flags & 0x1) == 1) { //dataOffsetPresent
            Mp4Utils.writeUInt32(byteBuffer, dataOffset);
        }

        if ((flags & 0x4) == 0x4) { //firstSampleFlagsPresent
            firstSampleFlags.getContent(byteBuffer);
        }

        for (Entry entry : entries) {
            if ((flags & 0x100) == 0x100) { //sampleDurationPresent
                Mp4Utils.writeUInt32(byteBuffer, entry.sampleDuration);
            }
            if ((flags & 0x200) == 0x200) { //sampleSizePresent
                Mp4Utils.writeUInt32(byteBuffer, entry.sampleSize);
            }
            if ((flags & 0x400) == 0x400) { //sampleFlagsPresent
                entry.sampleFlags.getContent(byteBuffer);
            }
            if ((flags & 0x800) == 0x800) { //sampleCompositionTimeOffsetPresent
                if (version == 0) {
                    Mp4Utils.writeUInt32(byteBuffer, entry.sampleCompositionTimeOffset);
                } else {
                    byteBuffer.putInt((int) entry.sampleCompositionTimeOffset);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "TrackRunBox{" +
                "version=" + version +
                ", flags=" + flags +
                ", dataOffset=" + dataOffset +
                ", firstSampleFlags=" + firstSampleFlags +
                ", entries=" + entries +
                '}';
    }

    public static class Entry {
        private final long sampleDuration;
        private final long sampleSize;
        private final SampleFlags sampleFlags;
        private final long sampleCompositionTimeOffset;

        public Entry(long sampleDuration, long sampleSize, SampleFlags sampleFlags, long sampleCompositionTimeOffset) {
            this.sampleDuration = sampleDuration;
            this.sampleSize = sampleSize;
            this.sampleFlags = sampleFlags;
            this.sampleCompositionTimeOffset = sampleCompositionTimeOffset;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "sampleDuration=" + sampleDuration +
                    ", sampleSize=" + sampleSize +
                    ", sampleFlags=" + sampleFlags +
                    ", sampleCompositionTimeOffset=" + sampleCompositionTimeOffset +
                    '}';
        }
    }
}
