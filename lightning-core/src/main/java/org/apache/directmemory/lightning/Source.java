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

public interface Source
{

    ByteOrder byteOrder();

    long readableBytes();

    int readBytes( byte[] bytes )
        throws IOException;

    int readBytes( byte[] bytes, int offset, int length )
        throws IOException;

    boolean readBoolean()
        throws IOException;

    byte readByte()
        throws IOException;

    short readUnsignedByte()
        throws IOException;

    short readShort()
        throws IOException;

    char readChar()
        throws IOException;

    int readInt()
        throws IOException;

    long readLong()
        throws IOException;

    float readFloat()
        throws IOException;

    double readDouble()
        throws IOException;

    String readString()
        throws IOException;

    void clear()
        throws IOException;

    void free()
        throws IOException;
}
