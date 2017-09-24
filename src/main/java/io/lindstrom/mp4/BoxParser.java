package io.lindstrom.mp4;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.util.List;

public interface BoxParser {
    Container parse(Path path) throws IOException;

    long parseBox(ByteBuffer content, List<Box> boxes);

    void write(Container container, WritableByteChannel channel) throws IOException;
}
