package io.lindstrom.mp4.box;

import io.lindstrom.mp4.Mp4Utils;

import java.nio.ByteBuffer;

public class SampleFlags {
    private final byte reserved;
    private final byte isLeading;
    private final byte sampleDependsOn;
    private final byte sampleIsDependedOn;
    private final byte sampleHasRedundancy;
    private final byte samplePaddingValue;
    private final boolean sampleIsDifferenceSample;
    private final int sampleDegradationPriority;

    public SampleFlags(ByteBuffer byteBuffer) {
        long value = Mp4Utils.readUInt32(byteBuffer);
        reserved = (byte) ((value & 0xF0000000) >> 28);
        isLeading = (byte) ((value & 0x0C000000) >> 26);
        sampleDependsOn = (byte) ((value & 0x03000000) >> 24);
        sampleIsDependedOn = (byte) ((value & 0x00C00000) >> 22);
        sampleHasRedundancy = (byte) ((value & 0x00300000) >> 20);
        samplePaddingValue = (byte) ((value & 0x000e0000) >> 17);
        sampleIsDifferenceSample = ((value & 0x00010000) >> 16) > 0;
        sampleDegradationPriority = (int) (value & 0x0000ffff);
    }

    public void getContent(ByteBuffer byteBuffer) {
        long value = 0;
        value |= reserved << 28;
        value |= isLeading << 26;
        value |= sampleDependsOn << 24;
        value |= sampleIsDependedOn << 22;
        value |= sampleHasRedundancy << 20;
        value |= samplePaddingValue << 17;
        value |= (sampleIsDifferenceSample ? 1 : 0) << 16;
        value |= sampleDegradationPriority;
        Mp4Utils.writeUInt32(byteBuffer, value);
    }
}
