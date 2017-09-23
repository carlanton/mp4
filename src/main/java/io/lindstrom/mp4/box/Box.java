package io.lindstrom.mp4.box;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

/*
aligned(8) class Box (unsigned int(32) boxtype, optional unsigned int(8)[16] extended_type) {
 unsigned int(32) size;
 unsigned int(32) type = boxtype;

 if (size==1) {
   unsigned int(64) largesize;
 } else if (size==0) {
   // box extends to end of file
 }
 if (boxtype==‘uuid’) {
   unsigned int(8)[16] usertype = extended_type;
 }
}
*/
public interface Box {
    long getSize();

    int getType();

    void write(WritableByteChannel channel) throws IOException;
}
