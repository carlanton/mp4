package io.lindstrom.mp4.boxes.iso14496.part12;

import io.lindstrom.mp4.Mp4Utils;
import io.lindstrom.mp4.support.AbstractFullBox;
import org.mp4parser.tools.DateHelper;
import org.mp4parser.tools.IsoTypeReader;
import org.mp4parser.tools.IsoTypeWriter;

import java.nio.ByteBuffer;
import java.time.Instant;

/*
aligned(8) class MediaHeaderBox extends FullBox(‘mdhd’, version, 0) {
 if (version==1) {
     unsigned int(64) creation_time;
     unsigned int(64) modification_time;
     unsigned int(32) timescale;
     unsigned int(64) duration;
 } else { // version==0
     unsigned int(32) creation_time;
     unsigned int(32) modification_time;
     unsigned int(32) timescale;
     unsigned int(32) duration;
 }
 bit(1) pad = 0;
 unsigned int(5)[3] language; // ISO-639-2/T language code
 unsigned int(16) pre_defined = 0;
}
*/
public class MediaHeaderBox extends AbstractFullBox {
    private static final int TYPE = Mp4Utils.boxType("mdhd");

    private final Instant creationTime;
    private final Instant modificationTime;
    private final long timescale;
    private final long duration;
    private final String language;

    public MediaHeaderBox(ByteBuffer content) {
        super(content);
        if (getVersion() == 1) {
            creationTime = DateHelper.convertInstant(IsoTypeReader.readUInt64(content));
            modificationTime = DateHelper.convertInstant(IsoTypeReader.readUInt64(content));
            timescale = IsoTypeReader.readUInt32(content);
            duration = content.getLong();
        } else {
            creationTime = DateHelper.convertInstant(IsoTypeReader.readUInt32(content));
            modificationTime = DateHelper.convertInstant(IsoTypeReader.readUInt32(content));
            timescale = IsoTypeReader.readUInt32(content);
            duration = content.getInt();
        }

        language = IsoTypeReader.readIso639(content);
        IsoTypeReader.readUInt16(content);
    }

    @Override
    protected void writeContent(ByteBuffer byteBuffer) {
        if (getVersion() == 1) {
            IsoTypeWriter.writeUInt64(byteBuffer, DateHelper.convertInstant(creationTime));
            IsoTypeWriter.writeUInt64(byteBuffer, DateHelper.convertInstant(modificationTime));
            IsoTypeWriter.writeUInt32(byteBuffer, timescale);
            byteBuffer.putLong(duration);
        } else {
            IsoTypeWriter.writeUInt32(byteBuffer, DateHelper.convertInstant(creationTime));
            IsoTypeWriter.writeUInt32(byteBuffer, DateHelper.convertInstant(modificationTime));
            IsoTypeWriter.writeUInt32(byteBuffer, timescale);
            byteBuffer.putInt((int) duration);
        }
        IsoTypeWriter.writeIso639(byteBuffer, language);
        IsoTypeWriter.writeUInt16(byteBuffer, 0);
    }

    @Override
    public long getSize() {
        long contentSize = HEADER_SIZE;

        contentSize += 4;
        if (getVersion() == 1) {
            contentSize += 8 + 8 + 4 + 8;
        } else {
            contentSize += 4 + 4 + 4 + 4;
        }
        contentSize += 2;
        contentSize += 2;
        return contentSize;
    }

    @Override
    public int getType() {
        return TYPE;
    }


    @Override
    public String toString() {
        return "MediaHeaderBox{" +
                "creationTime=" + creationTime +
                ", modificationTime=" + modificationTime +
                ", timescale=" + timescale +
                ", duration=" + duration +
                ", language='" + language + '\'' +
                ", version=" + version +
                ", flags=" + flags +
                '}';
    }
}
