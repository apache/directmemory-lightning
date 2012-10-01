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
package org.apache.directmemory.lightning.internal.marshaller;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.directmemory.lightning.SerializationContext;
import org.apache.directmemory.lightning.base.AbstractObjectMarshaller;
import org.apache.directmemory.lightning.metadata.PropertyDescriptor;

public class SerializableMarshaller
    extends AbstractObjectMarshaller
{

    @Override
    public boolean acceptType( Class<?> type )
    {
        return Serializable.class.isAssignableFrom( type );
    }

    @Override
    public void marshall( Object value, PropertyDescriptor propertyDescriptor, DataOutput dataOutput,
                          SerializationContext serializationContext )
        throws IOException
    {

        ObjectOutputStream stream = new ObjectOutputStream( (OutputStream) dataOutput );
        stream.writeObject( value );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <V> V unmarshall( V value, PropertyDescriptor propertyDescriptor, DataInput dataInput,
                             SerializationContext serializationContext )
        throws IOException
    {
        ObjectInputStream stream = new ObjectInputStream( (InputStream) dataInput );
        try
        {
            return (V) stream.readObject();
        }
        catch ( ClassNotFoundException e )
        {
            throw new IOException( "Error while deserialization", e );
        }
    }
}
