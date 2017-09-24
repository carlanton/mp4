package io.lindstrom.mp4.boxes.iso14496.part12;

import io.lindstrom.mp4.Mp4Parser;
import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.support.AbstractContainerBox;
import io.lindstrom.mp4.Box;

import java.nio.ByteBuffer;
import java.util.List;

public class MovieBox extends AbstractContainerBox {
    private static final int TYPE = Mp4Utils.boxType("moov");

    public MovieBox(List<Box> boxes) {
        super(boxes);
    }

    public MovieBox(ByteBuffer content, Mp4Parser mp4Parser) {
        super(content, mp4Parser);
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
