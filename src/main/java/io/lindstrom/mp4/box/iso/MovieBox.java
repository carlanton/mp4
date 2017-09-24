package io.lindstrom.mp4.box.iso;

import io.lindstrom.mp4.Mp4Parser;
import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.box.AbstractContainerBox;
import io.lindstrom.mp4.box.Box;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

public class MovieBox extends AbstractContainerBox {
    private static final int TYPE = Mp4Utils.boxType("moov");

    public MovieBox(List<Box> boxes) {
        super(boxes);
    }

    public MovieBox(ReadableByteChannel channel, int contentLength, Mp4Parser mp4Parser) throws IOException {
        super(channel, contentLength, mp4Parser);
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "MovieBox{" +
                "boxes=" + boxes +
                '}';
    }
}
