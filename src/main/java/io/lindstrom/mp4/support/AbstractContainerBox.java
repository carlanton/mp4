package io.lindstrom.mp4.support;

import io.lindstrom.mp4.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractContainerBox extends AbstractBox implements Container {
    protected final List<Box> boxes;

    public AbstractContainerBox(List<Box> boxes) {
        this.boxes = boxes;
    }

    public AbstractContainerBox(ByteBuffer content, BoxParser boxParser) {
        List<Box> boxes = new ArrayList<>();
        while (content.remaining() > 0) {
            if (boxParser.parseBox(content, boxes) == -1) {
                throw new RuntimeException("Broken container box!");
            }
        }
        this.boxes = Collections.unmodifiableList(boxes);
    }

    @Override
    public long getSize() {
        return HEADER_SIZE + boxes.stream()
                .mapToLong(Box::getSize)
                .sum();
    }

    @Override
    public List<Box> getBoxes() {
        return boxes;
    }

    @Override
    public void write(WritableByteChannel channel) throws IOException {
        ByteBuffer header = ByteBuffer.allocate(HEADER_SIZE);
        Mp4Utils.writeUInt32(header, getSize());
        header.putInt(getType());
        header.flip();
        channel.write(header);

        for (Box box : boxes) {
            box.write(channel);
        }
    }

    @Override
    public final void write(ByteBuffer content) {
        // not used
    }
}
