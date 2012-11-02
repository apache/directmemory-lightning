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
package org.apache.directmemory.lightning;

import java.io.IOException;
import java.nio.ByteOrder;

public interface Target
{

    ByteOrder byteOrder();

    long writtenBytes();

    void writeBoolean( boolean value )
        throws IOException;

    void writeBytes( byte[] bytes )
        throws IOException;

    void writeBytes( byte[] bytes, int offset, int length )
        throws IOException;

    void writeBoolean( boolean value )
        throws IOException;

    void writeByte( byte value )
        throws IOException;

    void writeUnsignedByte( short value )
        throws IOException;

    void writeShort( short value )
        throws IOException;

    void writeChar( char value )
        throws IOException;

    void writeInt( int value )
        throws IOException;

    void writeLong( long value )
        throws IOException;

    void writeFloat( float value )
        throws IOException;

    void writeDouble( double value )
        throws IOException;

    void writeString( String value )
        throws IOException;

    void clear()
        throws IOException;

    void free()
        throws IOException;

}
