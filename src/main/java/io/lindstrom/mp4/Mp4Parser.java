package io.lindstrom.mp4;

import io.lindstrom.mp4.box.*;
import io.lindstrom.mp4.box.iso.*;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mp4Parser {

    public Mp4Parser() {

    }

    private Box parse(ReadableByteChannel channel, int type, int contentLength) throws IOException {
        String boxType = Mp4Utils.boxType(type);
        switch (boxType) {
            case "mvex":
            case "trak":
            case "minf":
            case "mdia":
            case "stbl":
            case "moof":
            case "moov":
            case "traf":
                return parseGenericContainerBox(channel, type, contentLength);
        }

        ByteBuffer content = ByteBuffer.allocate(contentLength);
        channel.read(content);
        content.flip();

        switch (boxType) {
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

    public Container parse(ReadableByteChannel channel) throws IOException {
        List<Box> boxes = new ArrayList<>();
        while (true) {
            try {
                parseBox(channel, boxes);
            } catch (EOFException e) {
                break;
            }
        }
        return new Container(Collections.unmodifiableList(boxes));
    }

    public long parseBox(ReadableByteChannel channel, List<Box> boxes) throws IOException {
        ByteBuffer header = ByteBuffer.allocate(8);
        int bytesRead = channel.read(header);

        if (bytesRead == -1) {
            throw new EOFException();
        } else if (bytesRead != 8) {
            throw new IOException("End of stream");
        }

        header.flip();
        long size = Integer.toUnsignedLong(header.getInt());
        int type = header.getInt();
        int contentLength = Math.toIntExact(size - 8);

        if (size == 1) {
            throw new UnsupportedOperationException("Unsupported box: size = 1");
        } else if (size == 0) {
            throw new UnsupportedOperationException("Unsupported box: size = 0");
        }

        boxes.add(parse(channel, type, contentLength));

        return size;
    }

    public void writeBoxes(List<Box> boxes, WritableByteChannel channel) throws IOException {
        for (Box box : boxes) {
            box.write(channel);
        }
    }

    private GenericContainerBox parseGenericContainerBox(ReadableByteChannel channel, int type, int contentLength) throws IOException {
        List<Box> boxes = new ArrayList<>();

        long r = 0;
        while (r < contentLength) {
            r += parseBox(channel, boxes);
        }

        if (r != contentLength) {
            throw new RuntimeException("error: read " + r + ", expected " + contentLength);
        }

        return new GenericContainerBox(type, boxes);
    }
}
