package org.apache.directmemory.lightning.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.directmemory.lightning.Target;

public class ByteBufferTarget
    implements Target
{

    private final ByteBuffer byteBuffer;

    public ByteBufferTarget( ByteBuffer byteBuffer )
    {
        this.byteBuffer = byteBuffer;
    }

    @Override
    public ByteOrder byteOrder()
    {
        return byteBuffer.order();
    }

    @Override
    public long writtenBytes()
    {
        return byteBuffer.position();
    }

    @Override
    public void writeBoolean( boolean value )
        throws IOException
    {
        writeByte( (byte) ( value ? 1 : 0 ) );
    }

    @Override
    public void writeBytes( byte[] bytes )
        throws IOException
    {
        writeBytes( bytes, 0, bytes.length );
    }

    @Override
    public void writeBytes( byte[] bytes, int offset, int length )
        throws IOException
    {
        byteBuffer.put( bytes, offset, length );
    }

    @Override
    public void writeBoolean( boolean value )
        throws IOException
    {
        writeByte( (byte) ( value ? 1 : 0 ) );
    }

    @Override
    public void writeString( String value )
        throws IOException
    {
        // TODO: implementation of writeString is missing
    }

    @Override
    public void writeByte( byte value )
        throws IOException
    {
        byteBuffer.put( value );
    }

    @Override
    public void writeUnsignedByte( short value )
        throws IOException
    {
        byteBuffer.put( (byte) value );
    }

    @Override
    public void writeShort( short value )
        throws IOException
    {
        byteBuffer.putShort( value );
    }

    @Override
    public void writeChar( char value )
        throws IOException
    {
        writeShort( (short) value );
    }

    @Override
    public void writeInt( int value )
        throws IOException
    {
        byteBuffer.putInt( value );
    }

    @Override
    public void writeLong( long value )
        throws IOException
    {
        byteBuffer.putLong( value );
    }

    @Override
    public void writeFloat( float value )
        throws IOException
    {
        byteBuffer.putFloat( value );
    }

    @Override
    public void writeDouble( double value )
        throws IOException
    {
        byteBuffer.putDouble( value );
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
