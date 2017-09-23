package io.lindstrom.mp4.box;

import io.lindstrom.mp4.Mp4Utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.List;

public class GenericContainerBox implements Box {
    private final int type;
    private final List<Box> boxes;

    public GenericContainerBox(int type, List<Box> boxes) {
        this.type = type;
        this.boxes = boxes;
    }

    @Override
    public long getSize() {
        return 8 + boxes.stream()
                .mapToLong(Box::getSize)
                .sum();
    }

    @Override
    public int getType() {
        return type;
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

    public List<Box> getBoxes() {
        return boxes;
    }

    @Override
    public String toString() {
        return "ContainerBox{" +
                "type=" + Mp4Utils.boxType(type) +
                ", boxes=" + boxes +
                '}';
    }
}
