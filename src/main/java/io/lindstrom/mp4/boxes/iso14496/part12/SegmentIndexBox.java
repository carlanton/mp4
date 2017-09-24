package io.lindstrom.mp4.boxes.iso14496.part12;

import io.lindstrom.mp4.BitReaderBuffer;
import io.lindstrom.mp4.BitWriterBuffer;
import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.support.AbstractFullBox;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * aligned(8) class SegmentIndexBox extends FullBox(‘sidx’, version, 0) {
 *  unsigned int(32) reference_ID;
 *  unsigned int(32) timescale;
 *  if (version==0)
 *  {
 *   unsigned int(32) earliest_presentation_time;
 *   unsigned int(32) first_offset;
 *  }
 *  else
 *  {
 *   unsigned int(64) earliest_presentation_time;
 *   unsigned int(64) first_offset;
 *  }
 *  unsigned int(16) reserved = 0;
 *  unsigned int(16) reference_count;
 *  for(i=1; i &lt;= reference_count; i++)
 *  {
 *   bit (1)            reference_type;
 *   unsigned int(31)   referenced_size;
 *   unsigned int(32)   subsegment_duration;
 *   bit(1)             starts_with_SAP;
 *   unsigned int(3)    SAP_type;
 *   unsigned int(28)   SAP_delta_time;
 *  }
 * }
 */
public class SegmentIndexBox extends AbstractFullBox {
    private static final int TYPE = Mp4Utils.boxType("sidx");

    private final long referenceId;
    private final long timescale;
    private final long earliestPresentationTime;
    private final long firstOffset;
    private final int reserved;

    private final List<Entry> entries;

    public SegmentIndexBox(int version, int flags, long referenceId, long timescale, long earliestPresentationTime,
                           long firstOffset, int reserved, List<Entry> entries) {
        super(version, flags);
        this.referenceId = referenceId;
        this.timescale = timescale;
        this.earliestPresentationTime = earliestPresentationTime;
        this.firstOffset = firstOffset;
        this.reserved = reserved;
        this.entries = entries;
    }

    public SegmentIndexBox(ByteBuffer content) {
        super(content);
        referenceId = Mp4Utils.readUInt32(content);
        timescale = Mp4Utils.readUInt32(content);

        if (version == 0) {
            earliestPresentationTime = Mp4Utils.readUInt32(content);
            firstOffset = Mp4Utils.readUInt32(content);
        } else {
            earliestPresentationTime = Mp4Utils.putUnsignedLong(content);
            firstOffset = Mp4Utils.putUnsignedLong(content);
        }

        reserved = Mp4Utils.readUInt16(content);
        int numEntries = Mp4Utils.readUInt16(content);

        List<Entry> entries = new ArrayList<>(numEntries);

        for (int i = 0; i < numEntries; i++) {
            Entry.Builder builder = new Entry.Builder();

            BitReaderBuffer b = new BitReaderBuffer(content);

            builder.withReferenceType((byte) b.readBits(1))
                    .withReferencedSize(b.readBits(31))
                    .withSubsegmentDuration(Mp4Utils.readUInt32(content))
                    .build();

            b = new BitReaderBuffer(content);
            builder.withStartsWithSap((byte) b.readBits(1))
                    .withSapType((byte) b.readBits(3))
                    .withSapDeltaTime(b.readBits(28));

            entries.add(builder.build());
        }

        this.entries = Collections.unmodifiableList(entries);
    }

    @Override
    public long getSize() {
        long size = HEADER_SIZE;
        size += 4; // version + flags
        size += 4; // reference id
        size += 4; // timescale
        size += version == 0 ? 8 : 16; // ept + first offset
        size += 2; // reserved
        size += 2; // reference count
        size += entries.size() * 12; // entries
        return size;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void writeContent(ByteBuffer byteBuffer) {
        Mp4Utils.writeUInt32(byteBuffer, referenceId);
        Mp4Utils.writeUInt32(byteBuffer, timescale);
        if (version == 0) {
            Mp4Utils.writeUInt32(byteBuffer, earliestPresentationTime);
            Mp4Utils.writeUInt32(byteBuffer, firstOffset);
        } else {
            Mp4Utils.writeUInt64(byteBuffer, earliestPresentationTime);
            Mp4Utils.writeUInt64(byteBuffer, firstOffset);
        }
        Mp4Utils.writeUInt16(byteBuffer, reserved);
        Mp4Utils.writeUInt16(byteBuffer, entries.size());
        for (Entry entry : entries) {

            BitWriterBuffer b = new BitWriterBuffer(byteBuffer);
            b.writeBits(entry.getReferenceType(), 1);
            b.writeBits(entry.getReferencedSize(), 31);

            Mp4Utils.writeUInt32(byteBuffer, entry.getSubsegmentDuration());

            b = new BitWriterBuffer(byteBuffer);
            b.writeBits(entry.getStartsWithSap(), 1);
            b.writeBits(entry.getSapType(), 3);
            b.writeBits(entry.getSapDeltaTime(), 28);
        }
    }

    public long getReferenceId() {
        return referenceId;
    }

    public long getTimescale() {
        return timescale;
    }

    public long getEarliestPresentationTime() {
        return earliestPresentationTime;
    }

    public long getFirstOffset() {
        return firstOffset;
    }

    public int getReserved() {
        return reserved;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SegmentIndexBox that = (SegmentIndexBox) o;
        return referenceId == that.referenceId &&
                timescale == that.timescale &&
                earliestPresentationTime == that.earliestPresentationTime &&
                firstOffset == that.firstOffset &&
                reserved == that.reserved &&
                Objects.equals(entries, that.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), referenceId, timescale, earliestPresentationTime, firstOffset, reserved, entries);
    }

    @Override
    public String toString() {
        return "SegmentIndexBox{" +
                "version=" + version +
                ", flags=" + flags +
                ", referenceId=" + referenceId +
                ", timescale=" + timescale +
                ", earliestPresentationTime=" + earliestPresentationTime +
                ", firstOffset=" + firstOffset +
                ", reserved=" + reserved +
                ", entries=" + entries +
                '}';
    }

    public static class Entry {
        private final byte referenceType;
        private final int referencedSize;
        private final long subsegmentDuration;
        private final byte startsWithSap;
        private final byte sapType;
        private final int sapDeltaTime;

        public Entry(byte referenceType, int referencedSize, long subsegmentDuration, byte startsWithSap, byte sapType, int sapDeltaTime) {
            this.referenceType = referenceType;
            this.referencedSize = referencedSize;
            this.subsegmentDuration = subsegmentDuration;
            this.startsWithSap = startsWithSap;
            this.sapType = sapType;
            this.sapDeltaTime = sapDeltaTime;
        }

        public byte getReferenceType() {
            return referenceType;
        }

        public int getReferencedSize() {
            return referencedSize;
        }

        public long getSubsegmentDuration() {
            return subsegmentDuration;
        }

        public byte getStartsWithSap() {
            return startsWithSap;
        }

        public byte getSapType() {
            return sapType;
        }

        public int getSapDeltaTime() {
            return sapDeltaTime;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "referenceType=" + referenceType +
                    ", referencedSize=" + referencedSize +
                    ", subsegmentDuration=" + subsegmentDuration +
                    ", startsWithSap=" + startsWithSap +
                    ", sapType=" + sapType +
                    ", sapDeltaTime=" + sapDeltaTime +
                    '}';
        }

        public static class Builder {
            private byte referenceType;
            private int referencedSize;
            private long subsegmentDuration;
            private byte startsWithSap;
            private byte sapType;
            private int sapDeltaTime;

            public Builder withReferenceType(byte referenceType) {
                this.referenceType = referenceType;
                return this;
            }

            public Builder withReferencedSize(int referencedSize) {
                this.referencedSize = referencedSize;
                return this;
            }

            public Builder withSubsegmentDuration(long subsegmentDuration) {
                this.subsegmentDuration = subsegmentDuration;
                return this;
            }

            public Builder withStartsWithSap(byte startsWithSap) {
                this.startsWithSap = startsWithSap;
                return this;
            }

            public Builder withSapType(byte sapType) {
                this.sapType = sapType;
                return this;
            }

            public Builder withSapDeltaTime(int sapDeltaTime) {
                this.sapDeltaTime = sapDeltaTime;
                return this;
            }

            public Entry build() {
                return new Entry(referenceType, referencedSize, subsegmentDuration, startsWithSap, sapType, sapDeltaTime);
            }
        }
    }
}
