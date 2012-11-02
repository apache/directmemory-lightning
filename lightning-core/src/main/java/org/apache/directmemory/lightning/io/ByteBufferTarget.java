/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.directmemory.lightning.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.directmemory.lightning.Target;
import org.apache.directmemory.lightning.internal.util.UnicodeUtil;

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
        UnicodeUtil.UTF16toUTF8( value, this );
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
