package org.apache.directmemory.lightning.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;

import org.apache.directmemory.lightning.Target;
import org.apache.directmemory.lightning.internal.io.ByteOrderUtils;

public class OutputStreamTarget
    implements Target
{

    private final OutputStream stream;

    private final ByteOrder byteOrder;

    private long writtenBytes = 0;

    public OutputStreamTarget( OutputStream stream )
    {
        this( stream, ByteOrder.BIG_ENDIAN );
    }

    public OutputStreamTarget( OutputStream stream, ByteOrder byteOrder )
    {
        this.stream = stream;
        this.byteOrder = byteOrder;
    }

    @Override
    public ByteOrder byteOrder()
    {
        return byteOrder;
    }

    @Override
    public long writtenBytes()
    {
        return writtenBytes;
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
        stream.write( bytes, offset, length );
        writtenBytes += ( length - offset );
    }

    @Override
    public void writeByte( byte value )
        throws IOException
    {
        stream.write( value );
        writtenBytes++;
    }

    @Override
    public void writeUnsignedByte( short value )
        throws IOException
    {
        stream.write( (byte) value );
        writtenBytes++;
    }

    @Override
    public void writeShort( short value )
        throws IOException
    {
        ByteOrderUtils.putShort( value, this, byteOrder == ByteOrder.BIG_ENDIAN );
        writtenBytes += 2;
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
        ByteOrderUtils.putInt( value, this, byteOrder == ByteOrder.BIG_ENDIAN );
        writtenBytes += 4;
    }

    @Override
    public void writeLong( long value )
        throws IOException
    {
        ByteOrderUtils.putLong( value, this, byteOrder == ByteOrder.BIG_ENDIAN );
        writtenBytes += 8;
    }

    @Override
    public void writeFloat( float value )
        throws IOException
    {
        writeInt( Float.floatToIntBits( value ) );
    }

    @Override
    public void writeDouble( double value )
        throws IOException
    {
        writeLong( Double.doubleToLongBits( value ) );
    }

    @Override
    public void clear()
        throws IOException
    {
        throw new UnsupportedOperationException( "OutputStreamTarget cannot be reused" );
    }

    @Override
    public void free()
        throws IOException
    {
    }

}
