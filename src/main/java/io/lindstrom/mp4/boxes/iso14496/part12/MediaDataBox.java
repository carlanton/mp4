package io.lindstrom.mp4.boxes.iso14496.part12;

import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.support.AbstractBox;

import java.nio.ByteBuffer;

public class MediaDataBox extends AbstractBox {
    private static final int TYPE = Mp4Utils.boxType("mdat");

    private final ByteBuffer content;

    public MediaDataBox(ByteBuffer content) {
        this.content = content;
    }


    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void write(ByteBuffer content) {

    }
}
