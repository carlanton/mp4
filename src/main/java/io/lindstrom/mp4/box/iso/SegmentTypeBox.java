package io.lindstrom.mp4.box.iso;

import io.lindstrom.mp4.Mp4Utils;

import java.nio.ByteBuffer;
import java.util.List;

public class SegmentTypeBox extends FileTypeBox {
    private static final int TYPE = Mp4Utils.boxType("styp");

    public SegmentTypeBox(String majorBrand, long minorVersion, List<String> compatibleBrands) {
        super(majorBrand, minorVersion, compatibleBrands);
    }

    public SegmentTypeBox(ByteBuffer content) {
        super(content);
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "SegmentTypeBox{" +
                "majorBrand='" + majorBrand + '\'' +
                ", minorVersion=" + minorVersion +
                ", compatibleBrands=" + compatibleBrands +
                '}';
    }
}
