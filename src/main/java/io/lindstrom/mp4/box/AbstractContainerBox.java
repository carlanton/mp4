package io.lindstrom.mp4.box;

import io.lindstrom.mp4.Container;
import io.lindstrom.mp4.Mp4Parser;
import io.lindstrom.mp4.Mp4Utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainerBox extends Container implements Box {

    public AbstractContainerBox(List<Box> boxes) {
        super(boxes);
    }

    public AbstractContainerBox(ReadableByteChannel channel, int contentLength, Mp4Parser mp4Parser) throws IOException {
        super(parse(channel, contentLength, mp4Parser));
    }

    private static List<Box> parse(ReadableByteChannel channel, int contentLength, Mp4Parser mp4Parser) throws IOException {
        List<Box> boxes = new ArrayList<>();

        long bytesRead = 0;
        while (bytesRead < contentLength) {
            bytesRead += mp4Parser.parseBox(channel, boxes);
        }

        if (bytesRead != contentLength) {
            throw new RuntimeException("error: read " + bytesRead + ", expected " + contentLength);
        }

        return boxes;
    }

    @Override
    public long getSize() {
        return 8 + boxes.stream()
                .mapToLong(Box::getSize)
                .sum();
    }

    @Override
    public void write(WritableByteChannel channel) throws IOException {
        ByteBuffer header = ByteBuffer.allocate(8);
        Mp4Utils.writeUInt32(header, getSize());
        header.putInt(getType());
        header.flip();
        channel.write(header);

        for (Box box : boxes) {
            box.write(channel);
        }
    }
}
