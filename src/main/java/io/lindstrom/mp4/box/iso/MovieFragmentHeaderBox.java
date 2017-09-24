package io.lindstrom.mp4.box.iso;

import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.box.AbstractFullBox;

import java.nio.ByteBuffer;
import java.util.Objects;

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

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public String toString() {
        return "MovieFragmentHeaderBox{" +
                "sequenceNumber=" + sequenceNumber +
                ", version=" + version +
                ", flags=" + flags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MovieFragmentHeaderBox that = (MovieFragmentHeaderBox) o;
        return sequenceNumber == that.sequenceNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sequenceNumber);
    }

    public Builder buildUpon() {
        return new Builder()
                .withVersion(version)
                .withFlags(flags)
                .withSequenceNumber(sequenceNumber);
    }

    public static class Builder {
        private int version;
        private int flags;
        private long sequenceNumber;

        public Builder withVersion(int version) {
            this.version = version;
            return this;
        }

        public Builder withFlags(int flags) {
            this.flags = flags;
            return this;
        }

        public Builder withSequenceNumber(long sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
            return this;
        }

        public MovieFragmentHeaderBox build() {
            return new MovieFragmentHeaderBox(version, flags, sequenceNumber);
        }
    }
}
