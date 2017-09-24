package io.lindstrom.mp4.boxes;

import io.lindstrom.mp4.Box;
import io.lindstrom.mp4.Mp4Parser;
import io.lindstrom.mp4.support.AbstractContainerBox;

import java.nio.ByteBuffer;
import java.util.List;

public class GenericContainerBox extends AbstractContainerBox {
    private final int type;

    public GenericContainerBox(int type, List<Box> boxes) {
        super(boxes);
        this.type = type;
    }

    public GenericContainerBox(ByteBuffer content, int type, Mp4Parser mp4Parser) {
        super(content, mp4Parser);
        this.type = type;
    }

    @Override
    public int getType() {
        return type;
    }
}
