package org.apache.directmemory.lightning.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.directmemory.lightning.Source;

public class ByteBufferSource
    implements Source
{

    private final ByteBuffer byteBuffer;

    public ByteBufferSource( ByteBuffer byteBuffer )
    {
        this.byteBuffer = byteBuffer;
    }

    @Override
    public ByteOrder byteOrder()
    {
        return byteBuffer.order();
    }

    @Override
    public long readableBytes()
    {
        return byteBuffer.limit();
    }

    @Override
    public boolean readBoolean()
        throws IOException
    {
        return readByte() == 1 ? true : false;
    }

    @Override
    public int readBytes( byte[] bytes )
        throws IOException
    {
        return readBytes( bytes, 0, bytes.length );
    }

    @Override
    public int readBytes( byte[] bytes, int offset, int length )
        throws IOException
    {
        byteBuffer.get( bytes, offset, length );
        return length - offset;
    }

    @Override
    public byte readByte()
        throws IOException
    {
        return byteBuffer.get();
    }

    @Override
    public short readUnsignedByte()
        throws IOException
    {
        return (short) ( readByte() & 0xFF );
    }

    @Override
    public short readShort()
        throws IOException
    {
        return byteBuffer.getShort();
    }

    @Override
    public char readChar()
        throws IOException
    {
        return (char) readShort();
    }

    @Override
    public int readInt()
        throws IOException
    {
        return byteBuffer.getInt();
    }

    @Override
    public long readLong()
        throws IOException
    {
        return byteBuffer.getLong();
    }

    @Override
    public float readFloat()
        throws IOException
    {
        return byteBuffer.getFloat();
    }

    @Override
    public double readDouble()
        throws IOException
    {
        return byteBuffer.getDouble();
    }

    @Override
    public void clear()
        throws IOException
    {
        byteBuffer.clear();
    }

    @Override
    public void free()
        throws IOException
    {
    }

}
