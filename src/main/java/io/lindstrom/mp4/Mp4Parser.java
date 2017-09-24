package io.lindstrom.mp4;

import io.lindstrom.mp4.boxes.GenericBox;
import io.lindstrom.mp4.boxes.GenericContainerBox;
import io.lindstrom.mp4.boxes.iso14496.part12.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mp4Parser implements BoxParser {
    private static final int UUID = Mp4Utils.boxType("uuid");
    private static final int MMAP_SIZE = 50 * 1024;

    @Override
    public void write(Container container, WritableByteChannel channel) throws IOException {
        for (Box box : container.getBoxes()) {
            box.write(channel);
        }
    }

    public Container parse(Path path) throws IOException {
        List<Box> boxes = new ArrayList<>();
        ByteBuffer byteBuffer;

        try (FileChannel channel = FileChannel.open(path)) {
            long size = channel.size();

            if (size > MMAP_SIZE) {
                byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
            } else {
                byteBuffer = ByteBuffer.allocate((int) size);
                while (byteBuffer.remaining() > 0) {
                    channel.read(byteBuffer);
                }
                byteBuffer.flip();
            }
        }

        while (true) {
            if (parseBox(byteBuffer, boxes) == -1) {
                break;
            }
        }

        return new BasicContainer(Collections.unmodifiableList(boxes));
    }

    private Box parse(ByteBuffer data, int type, int contentLength) {
        ByteBuffer content = data.slice();
        content.limit(contentLength);
        data.position(data.position() + contentLength);
        String boxType = Mp4Utils.boxType(type);

        switch (boxType) {
            case "moov":
                return new MovieBox(content, this);

            case "mvex":
            case "trak":
            case "minf":
            case "mdia":
            case "stbl":
            case "moof":
            case "traf":
                return new GenericContainerBox(content, type, this);

            case "stsd":
                return new SampleDescriptionBox(content, this);

            case "mdhd":
                return new MediaHeaderBox(content);
            case "trex":
                return new TrackExtendsBox(content);
            case "hdlr":
                return new HandlerBox(content);
            case "ftyp":
                return new FileTypeBox(content);
            case "styp":
                return new SegmentTypeBox(content);
            case "sidx":
                return new SegmentIndexBox(content);
            case "tfdt":
                return new TrackFragmentBaseMediaDecodeTimeBox(content);
            case "mfhd":
                return new MovieFragmentHeaderBox(content);
            case "trun":
                return new TrackRunBox(content);
            case "tfhd":
                return new TrackFragmentHeaderBox(content);
            default:
                return new GenericBox(type, content);
        }
    }

    public long parseBox(ByteBuffer data, List<Box> boxes) {
        if (data.remaining() < 8)
            return -1;

        long size = Integer.toUnsignedLong(data.getInt());
        int type = data.getInt();


        int contentLength = Math.toIntExact(size - 8);

        if (size == 1) {
            throw new UnsupportedOperationException("Unsupported box: size = 1");
        } else if (size == 0) {
            throw new UnsupportedOperationException("Unsupported box: size = 0");
        } else if (type == UUID) {
            throw new UnsupportedOperationException("Unsupported box: type = uuid");
        }

        Box box = parse(data, type, contentLength);
        boxes.add(box);

        return size;
    }

}
