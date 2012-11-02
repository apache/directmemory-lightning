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
package org.apache.directmemory.lightning.internal.io;

import java.io.IOException;

import org.apache.directmemory.lightning.Source;
import org.apache.directmemory.lightning.Target;

public class ByteOrderUtils
{

    public static void putShort( short value, Target target, boolean bigEndian )
        throws IOException
    {
        if ( bigEndian )
        {
            target.writeByte( (byte) ( value >> 8 ) );
            target.writeByte( (byte) ( value >> 0 ) );
        }
        else
        {
            target.writeByte( (byte) ( value >> 0 ) );
            target.writeByte( (byte) ( value >> 8 ) );
        }
    }

    public static short getShort( Source source, boolean bigEndian )
        throws IOException
    {
        if ( bigEndian )
        {
            byte b1 = source.readByte();
            byte b0 = source.readByte();
            return buildShort( b1, b0 );
        }
        else
        {
            byte b0 = source.readByte();
            byte b1 = source.readByte();
            return buildShort( b1, b0 );
        }
    }

    public static void putInt( int value, Target target, boolean bigEndian )
        throws IOException
    {
        if ( bigEndian )
        {
            target.writeByte( (byte) ( value >>> 24 ) );
            target.writeByte( (byte) ( value >>> 16 ) );
            target.writeByte( (byte) ( value >>> 8 ) );
            target.writeByte( (byte) ( value >>> 0 ) );
        }
        else
        {
            target.writeByte( (byte) ( value >>> 0 ) );
            target.writeByte( (byte) ( value >>> 8 ) );
            target.writeByte( (byte) ( value >>> 16 ) );
            target.writeByte( (byte) ( value >>> 24 ) );
        }
    }

    public static int getInt( Source source, boolean bigEndian )
        throws IOException
    {
        if ( bigEndian )
        {
            byte b3 = source.readByte();
            byte b2 = source.readByte();
            byte b1 = source.readByte();
            byte b0 = source.readByte();
            return buildInt( b3, b2, b1, b0 );
        }
        else
        {
            byte b0 = source.readByte();
            byte b1 = source.readByte();
            byte b2 = source.readByte();
            byte b3 = source.readByte();
            return buildInt( b3, b2, b1, b0 );
        }
    }

    public static void putLong( long value, Target target, boolean bigEndian )
        throws IOException
    {
        if ( bigEndian )
        {
            target.writeByte( (byte) ( value >> 56 ) );
            target.writeByte( (byte) ( value >> 48 ) );
            target.writeByte( (byte) ( value >> 40 ) );
            target.writeByte( (byte) ( value >> 32 ) );
            target.writeByte( (byte) ( value >> 24 ) );
            target.writeByte( (byte) ( value >> 16 ) );
            target.writeByte( (byte) ( value >> 8 ) );
            target.writeByte( (byte) ( value >> 0 ) );
        }
        else
        {
            target.writeByte( (byte) ( value >> 0 ) );
            target.writeByte( (byte) ( value >> 8 ) );
            target.writeByte( (byte) ( value >> 16 ) );
            target.writeByte( (byte) ( value >> 24 ) );
            target.writeByte( (byte) ( value >> 32 ) );
            target.writeByte( (byte) ( value >> 40 ) );
            target.writeByte( (byte) ( value >> 48 ) );
            target.writeByte( (byte) ( value >> 56 ) );
        }
    }

    public static long getLong( Source source, boolean bigEndian )
        throws IOException
    {
        if ( bigEndian )
        {
            byte b7 = source.readByte();
            byte b6 = source.readByte();
            byte b5 = source.readByte();
            byte b4 = source.readByte();
            byte b3 = source.readByte();
            byte b2 = source.readByte();
            byte b1 = source.readByte();
            byte b0 = source.readByte();
            return buildLong( b7, b6, b5, b4, b3, b2, b1, b0 );
        }
        else
        {
            byte b0 = source.readByte();
            byte b1 = source.readByte();
            byte b2 = source.readByte();
            byte b3 = source.readByte();
            byte b4 = source.readByte();
            byte b5 = source.readByte();
            byte b6 = source.readByte();
            byte b7 = source.readByte();
            return buildLong( b7, b6, b5, b4, b3, b2, b1, b0 );
        }
    }

    private static short buildShort( byte b1, byte b0 )
    {
        return (short) ( ( ( ( b1 & 0xFF ) << 8 ) | ( ( b0 & 0xFF ) << 0 ) ) );
    }

    private static int buildInt( byte b3, byte b2, byte b1, byte b0 )
    {
        return ( ( ( ( b3 & 0xFF ) << 24 ) | ( ( b2 & 0xFF ) << 16 ) | ( ( b1 & 0xFF ) << 8 ) | ( ( b0 & 0xFF ) << 0 ) ) );
    }

    private static long buildLong( byte b7, byte b6, byte b5, byte b4, byte b3, byte b2, byte b1, byte b0 )
    {
        return ( ( ( ( b7 & 0xFFL ) << 56 ) | ( ( b6 & 0xFFL ) << 48 ) | ( ( b5 & 0xFFL ) << 40 )
            | ( ( b4 & 0xFFL ) << 32 ) | ( ( b3 & 0xFFL ) << 24 ) | ( ( b2 & 0xFFL ) << 16 ) | ( ( b1 & 0xFFL ) << 8 ) | ( ( b0 & 0xFFL ) << 0 ) ) );
    }

}
