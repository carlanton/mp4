package io.lindstrom.mp4;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Mp4Utils {
    public static int boxType(String string) {
        ByteBuffer buffer = ByteBuffer.wrap(string.getBytes(StandardCharsets.UTF_8));
        return buffer.getInt();
    }

    public static String boxType(int type) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(type);
        return new String(buffer.array(), StandardCharsets.UTF_8);
    }

    public static String read4cc(ByteBuffer byteBuffer) {
        byte[] codeBytes = new byte[4];
        byteBuffer.get(codeBytes);
        return new String(codeBytes, StandardCharsets.UTF_8);
    }

    public static byte[] write4cc(String fourCC) {
        byte[] bytes = fourCC.getBytes(StandardCharsets.UTF_8);
        if (bytes.length != 4) {
            throw new RuntimeException("Invalid four-character code:" + fourCC);
        }
        return bytes;
    }

    public static short getUnsignedByte(ByteBuffer bb) {
        return ((short) (bb.get() & 0xff));
    }

    public static void putUnsignedByte(ByteBuffer bb, int value) {
        bb.put((byte) (value & 0xff));
    }

    public static short getUnsignedByte(ByteBuffer bb, int position) {
        return ((short) (bb.get(position) & (short) 0xff));
    }

    public static void putUnsignedByte(ByteBuffer bb, int position, int value) {
        bb.put(position, (byte) (value & 0xff));
    }

    // ---------------------------------------------------------------

    public static int getUnsignedShort(ByteBuffer bb) {
        return (bb.getShort() & 0xffff);
    }

    public static void putUnsignedShort(ByteBuffer bb, int value) {
        bb.putShort((short) (value & 0xffff));
    }

    public static int getUnsignedShort(ByteBuffer bb, int position) {
        return (bb.getShort(position) & 0xffff);
    }

    public static void putUnsignedShort(ByteBuffer bb, int position, int value) {
        bb.putShort(position, (short) (value & 0xffff));
    }

    // ---------------------------------------------------------------

    public static long getUnsignedInt(ByteBuffer bb) {
        return ((long) bb.getInt() & 0xffffffffL);
    }

    public static void writeUInt32(ByteBuffer bb, long value) {
        bb.putInt((int) (value & 0xffffffffL));
    }








    public static long readUInt32(ByteBuffer bb) {
        long i = bb.getInt();
        if (i < 0) {
            i += 1L << 32;
        }
        return i;
    }

    public static int readUInt24(ByteBuffer bb) {
        int result = 0;
        result += readUInt16(bb) << 8;
        result += byte2int(bb.get());
        return result;
    }


    public static int readUInt16(ByteBuffer bb) {
        int result = 0;
        result += byte2int(bb.get()) << 8;
        result += byte2int(bb.get());
        return result;
    }


    public static int readUInt8(ByteBuffer bb) {
        return byte2int(bb.get());
    }

    public static int byte2int(byte b) {
        return b < 0 ? b + 256 : b;
    }


    public static long putUnsignedLong(ByteBuffer byteBuffer) {
        long result = byteBuffer.getLong();
        if (result < 0) {
            throw new RuntimeException("UInt64 is not supported");
        }
        return result;
    }

    public static void writeUInt64(ByteBuffer byteBuffer, long u) {
        byteBuffer.putLong(u);
    }


    public static void writeUInt24(ByteBuffer bb, int i) {
        i = i & 0xFFFFFF;
        writeUInt16(bb, i >> 8);
        writeUInt8(bb, i);

    }

    public static void writeUInt16(ByteBuffer bb, int i) {
        i = i & 0xFFFF;
        writeUInt8(bb, i >> 8);
        writeUInt8(bb, i & 0xFF);
    }

    public static void writeUInt8(ByteBuffer bb, int i) {
        i = i & 0xFF;
        bb.put((byte) i);
    }



}
