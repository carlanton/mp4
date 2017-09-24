package io.lindstrom.mp4.box;

import io.lindstrom.mp4.Mp4Parser;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;

public class GenericContainerBox extends AbstractContainerBox {
    private final int type;

    public GenericContainerBox(ReadableByteChannel channel, int type, int contentLength, Mp4Parser mp4Parser) throws IOException {
        super(channel, contentLength, mp4Parser);
        this.type = type;
    }

    @Override
    public int getType() {
        return type;
    }
}
