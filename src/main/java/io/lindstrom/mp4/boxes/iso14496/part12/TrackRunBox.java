package io.lindstrom.mp4.boxes.iso14496.part12;

import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.support.AbstractFullBox;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
            firstSampleFlags = new SampleFlags(Mp4Utils.readUInt32(content));
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
                sampleFlags = new SampleFlags(Mp4Utils.readUInt32(content));
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
            Mp4Utils.writeUInt32(byteBuffer, firstSampleFlags.asUInt32());
        }

        for (Entry entry : entries) {
            if ((flags & 0x100) == 0x100) { //sampleDurationPresent
                Mp4Utils.writeUInt32(byteBuffer, entry.sampleDuration);
            }
            if ((flags & 0x200) == 0x200) { //sampleSizePresent
                Mp4Utils.writeUInt32(byteBuffer, entry.sampleSize);
            }
            if ((flags & 0x400) == 0x400) { //sampleFlagsPresent
                Mp4Utils.writeUInt32(byteBuffer, entry.sampleFlags.asUInt32());
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

    public int getDataOffset() {
        return dataOffset;
    }

    public SampleFlags getFirstSampleFlags() {
        return firstSampleFlags;
    }

    public List<Entry> getEntries() {
        return entries;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TrackRunBox that = (TrackRunBox) o;
        return dataOffset == that.dataOffset &&
                Objects.equals(firstSampleFlags, that.firstSampleFlags) &&
                Objects.equals(entries, that.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dataOffset, firstSampleFlags, entries);
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

        public long getSampleDuration() {
            return sampleDuration;
        }

        public long getSampleSize() {
            return sampleSize;
        }

        public SampleFlags getSampleFlags() {
            return sampleFlags;
        }

        public long getSampleCompositionTimeOffset() {
            return sampleCompositionTimeOffset;
        }
    }
}
