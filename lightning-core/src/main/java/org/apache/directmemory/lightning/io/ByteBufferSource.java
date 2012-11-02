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

import org.apache.directmemory.lightning.Source;
import org.apache.directmemory.lightning.internal.util.UnicodeUtil;

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
    public boolean readBoolean()
        throws IOException
    {
        return readByte() == 1 ? true : false;
    }

    @Override
    public String readString()
        throws IOException
    {
        return UnicodeUtil.UTF8toUTF16( this );
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
