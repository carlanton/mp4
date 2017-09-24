package io.lindstrom.mp4.boxes.iso14496.part12;

import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.support.AbstractBox;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/*
aligned(8) class FileTypeBox extends Box(‘ftyp’) {
 unsigned int(32) major_brand;
 unsigned int(32) minor_version;
 unsigned int(32) compatible_brands[]; // to end of the box
}
*/
public class FileTypeBox extends AbstractBox {
    private static final int TYPE = Mp4Utils.boxType("ftyp");

    private final String majorBrand;
    private final long minorVersion;
    private final List<String> compatibleBrands;

    public FileTypeBox(String majorBrand, long minorVersion, List<String> compatibleBrands) {
        this.majorBrand = majorBrand;
        this.minorVersion = minorVersion;
        this.compatibleBrands = compatibleBrands;
    }

    public FileTypeBox(ByteBuffer content) {
        majorBrand = Mp4Utils.read4cc(content);
        minorVersion = Mp4Utils.readUInt32(content);

        int compatibleBrandsCount = content.remaining() / 4;
        List<String> compatibleBrands = new ArrayList<>();
        for (int i = 0; i < compatibleBrandsCount; i++) {
            compatibleBrands.add(Mp4Utils.read4cc(content));
        }
        this.compatibleBrands = Collections.unmodifiableList(compatibleBrands);
    }

    @Override
    public long getSize() {
        return HEADER_SIZE + 8 + 4 * compatibleBrands.size();
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void write(ByteBuffer content) {
        content.put(Mp4Utils.write4cc(majorBrand));

        Mp4Utils.writeUInt32(content, minorVersion);
        for (String compatibleBrand : compatibleBrands) {
            content.put(Mp4Utils.write4cc(compatibleBrand));
        }
    }

    public String getMajorBrand() {
        return majorBrand;
    }

    public long getMinorVersion() {
        return minorVersion;
    }

    public List<String> getCompatibleBrands() {
        return compatibleBrands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileTypeBox that = (FileTypeBox) o;
        return minorVersion == that.minorVersion &&
                Objects.equals(majorBrand, that.majorBrand) &&
                Objects.equals(compatibleBrands, that.compatibleBrands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(majorBrand, minorVersion, compatibleBrands);
    }

    @Override
    public String toString() {
        return "FileTypeBox{" +
                "majorBrand='" + majorBrand + '\'' +
                ", minorVersion=" + minorVersion +
                ", compatibleBrands=" + compatibleBrands +
                '}';
    }

    public Builder buildUpon() {
        return new Builder()
                .withMajorBrand(majorBrand)
                .withMinorVersion(minorVersion)
                .withCompatibleBrands(compatibleBrands);
    }

    public static class Builder {
        private String majorBrand;
        private long minorVersion;
        private List<String> compatibleBrands;

        public Builder withMajorBrand(String majorBrand) {
            this.majorBrand = majorBrand;
            return this;
        }

        public Builder withMinorVersion(long minorVersion) {
            this.minorVersion = minorVersion;
            return this;
        }

        public Builder withCompatibleBrands(List<String> compatibleBrands) {
            this.compatibleBrands = compatibleBrands;
            return this;
        }

        public FileTypeBox build() {
            return new FileTypeBox(majorBrand, minorVersion, compatibleBrands);
        }
    }
}
