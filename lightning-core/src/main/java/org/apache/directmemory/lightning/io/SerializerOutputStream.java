package org.apache.directmemory.lightning.io;

import java.io.IOException;
import java.io.ObjectOutput;

import org.apache.directmemory.lightning.Serializer;
import org.apache.directmemory.lightning.Target;

public class SerializerOutputStream
    implements ObjectOutput
{

    private final Serializer serializer;

    private final Target target;

    public SerializerOutputStream( Serializer serializer, Target target )
    {
        this.serializer = serializer;
        this.target = target;
    }

    @Override
    public void write( int b )
        throws IOException
    {
        target.writeByte( (byte) b );
    }

    @Override
    public void writeBoolean( boolean v )
        throws IOException
    {
        target.writeBoolean( v );
    }

    @Override
    public void writeByte( int v )
        throws IOException
    {
        target.writeByte( (byte) v );
    }

    @Override
    public void writeShort( int v )
        throws IOException
    {
        target.writeShort( (short) v );
    }

    @Override
    public void writeChar( int v )
        throws IOException
    {
        target.writeChar( (char) v );
    }

    @Override
    public void writeInt( int v )
        throws IOException
    {
        target.writeInt( v );
    }

    @Override
    public void writeLong( long v )
        throws IOException
    {
        target.writeLong( v );
    }

    @Override
    public void writeFloat( float v )
        throws IOException
    {
        target.writeFloat( v );
    }

    @Override
    public void writeDouble( double v )
        throws IOException
    {
        target.writeDouble( v );
    }

    @Override
    public void writeBytes( String s )
        throws IOException
    {
        target.writeString( s );
    }

    @Override
    public void writeChars( String s )
        throws IOException
    {
        char[] chars = s.toCharArray();
        for ( int i = 0; i < chars.length; i++ )
        {
            target.writeChar( chars[i] );
        }
    }

    @Override
    public void writeUTF( String s )
        throws IOException
    {
        target.writeString( s );
    }

    @Override
    public void writeObject( Object obj )
        throws IOException
    {
        serializer.serialize( obj, target );
    }

    @Override
    public void write( byte[] b )
        throws IOException
    {
        write( b, 0, b.length );
    }

    @Override
    public void write( byte[] b, int off, int len )
        throws IOException
    {
        target.writeBytes( b, off, len );
    }

    @Override
    public void flush()
        throws IOException
    {
        // nothing to implement here
    }

    @Override
    public void close()
        throws IOException
    {
        // nothing to implement here
    }

}
