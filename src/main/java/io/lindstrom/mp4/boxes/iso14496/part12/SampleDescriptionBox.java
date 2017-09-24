package io.lindstrom.mp4.boxes.iso14496.part12;

import io.lindstrom.mp4.Box;
import io.lindstrom.mp4.BoxParser;
import io.lindstrom.mp4.Container;
import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.support.AbstractFullBox;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
aligned(8) class SampleDescriptionBox (unsigned int(32) handler_type) extends FullBox('stsd', 0, 0){
  int i ;
  unsigned int(32) entry_count;
  for (i = 1 ; i <= entry_count; i++){
    switch (handler_type) {
      case ‘soun’: // for audio tracks
        AudioSampleEntry();
        break;
      case ‘vide’: // for video tracks
        VisualSampleEntry();
        break;
      case ‘hint’: // Hint track
        HintSampleEntry();
        break;
    }
  }
}
*/
public class SampleDescriptionBox extends AbstractFullBox implements Container {
    private static final int TYPE = Mp4Utils.boxType("stsd");

    private final List<Box> boxes;

    public SampleDescriptionBox(int version, int flags, List<Box> boxes) {
        super(version, flags);
        this.boxes = boxes;
    }

    public SampleDescriptionBox(ByteBuffer content, List<Box> boxes) {
        super(content);
        this.boxes = boxes;
    }

    public SampleDescriptionBox(ByteBuffer content, BoxParser boxParser) {
        super(content); // read version + flags
        long entryCount = Mp4Utils.readUInt32(content);

        List<Box> boxes = new ArrayList<>();
        while (content.remaining() > 0) {
            if (boxParser.parseBox(content, boxes) == -1) {
                throw new RuntimeException("Broken container box!");
            }
        }

        assert boxes.size() == entryCount;

        this.boxes = Collections.unmodifiableList(boxes);
    }

    @Override
    public long getSize() {
        return HEADER_SIZE + 4 + 4 + boxes.stream().mapToLong(Box::getSize).sum();
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public List<Box> getBoxes() {
        return boxes;
    }

    @Override
    public void write(WritableByteChannel channel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(HEADER_SIZE + 4 + 4);
        Mp4Utils.writeUInt32(byteBuffer, getSize());
        byteBuffer.putInt(getType());
        Mp4Utils.writeUInt32(byteBuffer, (version << 24) | flags);
        Mp4Utils.writeUInt32(byteBuffer, boxes.size());

        byteBuffer.flip();
        channel.write(byteBuffer);

        for (Box box : boxes) {
            box.write(channel);
        }
    }

    @Override
    protected void writeContent(ByteBuffer content) {
        // not used ...
    }

    @Override
    public String toString() {
        return "SampleDescriptionBox{" +
                "boxes=" + boxes +
                ", version=" + version +
                ", flags=" + flags +
                '}';
    }
}
