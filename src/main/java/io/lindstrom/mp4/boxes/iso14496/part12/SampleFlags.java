package io.lindstrom.mp4.boxes.iso14496.part12;

import java.util.Objects;

public class SampleFlags {
    private final byte reserved;
    private final byte isLeading;
    private final byte sampleDependsOn;
    private final byte sampleIsDependedOn;
    private final byte sampleHasRedundancy;
    private final byte samplePaddingValue;
    private final boolean sampleIsDifferenceSample;
    private final int sampleDegradationPriority;

    public SampleFlags(long value) {
        reserved = (byte) ((value & 0xF0000000) >> 28);
        isLeading = (byte) ((value & 0x0C000000) >> 26);
        sampleDependsOn = (byte) ((value & 0x03000000) >> 24);
        sampleIsDependedOn = (byte) ((value & 0x00C00000) >> 22);
        sampleHasRedundancy = (byte) ((value & 0x00300000) >> 20);
        samplePaddingValue = (byte) ((value & 0x000e0000) >> 17);
        sampleIsDifferenceSample = ((value & 0x00010000) >> 16) > 0;
        sampleDegradationPriority = (int) (value & 0x0000ffff);
    }

    public long asUInt32() {
        long value = 0;
        value |= reserved << 28;
        value |= isLeading << 26;
        value |= sampleDependsOn << 24;
        value |= sampleIsDependedOn << 22;
        value |= sampleHasRedundancy << 20;
        value |= samplePaddingValue << 17;
        value |= (sampleIsDifferenceSample ? 1 : 0) << 16;
        value |= sampleDegradationPriority;
        return value;
    }

    public byte getReserved() {
        return reserved;
    }

    public byte getIsLeading() {
        return isLeading;
    }

    public byte getSampleDependsOn() {
        return sampleDependsOn;
    }

    public byte getSampleIsDependedOn() {
        return sampleIsDependedOn;
    }

    public byte getSampleHasRedundancy() {
        return sampleHasRedundancy;
    }

    public byte getSamplePaddingValue() {
        return samplePaddingValue;
    }

    public boolean isSampleIsDifferenceSample() {
        return sampleIsDifferenceSample;
    }

    public int getSampleDegradationPriority() {
        return sampleDegradationPriority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleFlags that = (SampleFlags) o;
        return reserved == that.reserved &&
                isLeading == that.isLeading &&
                sampleDependsOn == that.sampleDependsOn &&
                sampleIsDependedOn == that.sampleIsDependedOn &&
                sampleHasRedundancy == that.sampleHasRedundancy &&
                samplePaddingValue == that.samplePaddingValue &&
                sampleIsDifferenceSample == that.sampleIsDifferenceSample &&
                sampleDegradationPriority == that.sampleDegradationPriority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reserved, isLeading, sampleDependsOn, sampleIsDependedOn, sampleHasRedundancy, samplePaddingValue, sampleIsDifferenceSample, sampleDegradationPriority);
    }

    @Override
    public String toString() {
        return "SampleFlags{" +
                "reserved=" + reserved +
                ", isLeading=" + isLeading +
                ", sampleDependsOn=" + sampleDependsOn +
                ", sampleIsDependedOn=" + sampleIsDependedOn +
                ", sampleHasRedundancy=" + sampleHasRedundancy +
                ", samplePaddingValue=" + samplePaddingValue +
                ", sampleIsDifferenceSample=" + sampleIsDifferenceSample +
                ", sampleDegradationPriority=" + sampleDegradationPriority +
                '}';
    }
}
