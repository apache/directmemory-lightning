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
import java.io.InputStream;
import java.nio.ByteOrder;

import org.apache.directmemory.lightning.Source;
import org.apache.directmemory.lightning.internal.io.ByteOrderUtils;

public class InputStreamSource
    implements Source
{

    private final InputStream stream;

    private final ByteOrder byteOrder;

    public InputStreamSource( InputStream stream )
    {
        this( stream, ByteOrder.BIG_ENDIAN );
    }

    public InputStreamSource( InputStream stream, ByteOrder byteOrder )
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
    public long readableBytes()
    {
        try
        {
            return stream.available();
        }
        catch ( IOException e )
        {
            return -1;
        }
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
        return stream.read( bytes, offset, length );
    }

    @Override
    public byte readByte()
        throws IOException
    {
        return (byte) stream.read();
    }

    @Override
    public short readUnsignedByte()
        throws IOException
    {
        return  (short) ( readByte() & 0xFF );
    }

    @Override
    public short readShort()
        throws IOException
    {
        return ByteOrderUtils.getShort( this, byteOrder == ByteOrder.BIG_ENDIAN );
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
        return ByteOrderUtils.getInt( this, byteOrder == ByteOrder.BIG_ENDIAN );
    }

    @Override
    public long readLong()
        throws IOException
    {
        return ByteOrderUtils.getLong( this, byteOrder == ByteOrder.BIG_ENDIAN );
    }

    @Override
    public float readFloat()
        throws IOException
    {
        return Float.intBitsToFloat( readInt() );
    }

    @Override
    public double readDouble()
        throws IOException
    {
        return Double.longBitsToDouble( readLong() );
    }

    @Override
    public void clear()
        throws IOException
    {
        throw new UnsupportedOperationException( "InputStreamSource cannot be reused" );
    }

    @Override
    public void free()
        throws IOException
    {
    }

}
