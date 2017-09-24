package io.lindstrom.mp4.boxes.iso14496.part12;

import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.support.AbstractFullBox;
import org.mp4parser.tools.IsoTypeReader;
import org.mp4parser.tools.IsoTypeWriter;
import org.mp4parser.tools.Utf8;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/*
aligned(8) class HandlerBox extends FullBox(‘hdlr’, version = 0, 0) {
 unsigned int(32) pre_defined = 0;
 unsigned int(32) handler_type;
 const unsigned int(32)[3] reserved = 0;
 string name;
}
*/
public class HandlerBox extends AbstractFullBox {
    private static final int TYPE = Mp4Utils.boxType("hdlr");

    private long preDefined;
    private final String handlerType;
    private final String name;
    private final long a, b, c;
    private final boolean zeroTerm;

    public HandlerBox(ByteBuffer content) {
        super(content);

        // isoparser note: should be zero but apple writes here some value
        preDefined = IsoTypeReader.readUInt32(content);
        handlerType = IsoTypeReader.read4cc(content);
        a = IsoTypeReader.readUInt32(content);
        b = IsoTypeReader.readUInt32(content);
        c = IsoTypeReader.readUInt32(content);
        String name = null;

        if (content.remaining() > 0) {
            name = IsoTypeReader.readString(content, content.remaining());
            if (name.endsWith("\0")) {
                name = name.substring(0, name.length() - 1);
                zeroTerm = true;
            } else {
                zeroTerm = false;
            }
        } else {
            zeroTerm = false; //No string at all, not even zero term char
        }
        this.name = name;
    }

    @Override
    protected void writeContent(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeUInt32(byteBuffer, preDefined);
        byteBuffer.put(Mp4Utils.write4cc(handlerType));
        IsoTypeWriter.writeUInt32(byteBuffer, a);
        IsoTypeWriter.writeUInt32(byteBuffer, b);
        IsoTypeWriter.writeUInt32(byteBuffer, c);
        if (name != null) {
            byteBuffer.put(name.getBytes(StandardCharsets.UTF_8));
        }
        if (zeroTerm) {
            byteBuffer.put((byte) 0);
        }
    }

    @Override
    public long getSize() {
        if (zeroTerm) {
            return HEADER_SIZE + 25 + Utf8.utf8StringLengthInBytes(name);
        } else {
            return HEADER_SIZE + 24 + Utf8.utf8StringLengthInBytes(name);
        }
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "HandlerBox{" +
                "handlerType='" + handlerType + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
