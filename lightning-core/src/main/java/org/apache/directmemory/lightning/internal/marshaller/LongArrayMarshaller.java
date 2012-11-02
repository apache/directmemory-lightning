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

import java.io.IOException;

import org.apache.directmemory.lightning.SerializationContext;
import org.apache.directmemory.lightning.Source;
import org.apache.directmemory.lightning.Target;
import org.apache.directmemory.lightning.base.AbstractMarshaller;
import org.apache.directmemory.lightning.metadata.PropertyDescriptor;

public class LongArrayMarshaller
    extends AbstractMarshaller
{

    @Override
    public boolean acceptType( Class<?> type )
    {
        return long[].class == type || Long[].class == type;
    }

    @Override
    public void marshall( Object value, PropertyDescriptor propertyDescriptor, Target target,
                          SerializationContext serializationContext )
        throws IOException
    {

        if ( !writePossibleNull( value, target ) )
        {
            return;
        }

        if ( long[].class == propertyDescriptor.getType() )
        {
            long[] array = (long[]) value;
            target.writeInt( array.length );

            for ( long arrayValue : array )
            {
                target.writeLong( arrayValue );
            }
        }
        else
        {
            Long[] array = (Long[]) value;
            target.writeInt( array.length );

            for ( long arrayValue : array )
            {
                target.writeLong( arrayValue );
            }
        }
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <V> V unmarshall( PropertyDescriptor propertyDescriptor, Source source,
                             SerializationContext serializationContext )
        throws IOException
    {
        if ( isNull( source ) )
        {
            return null;
        }

        int size = source.readInt();
        if ( long[].class == propertyDescriptor.getType() )
        {
            long[] array = new long[size];
            for ( int i = 0; i < size; i++ )
            {
                array[i] = source.readLong();
            }

            return (V) array;
        }
        else
        {
            Long[] array = new Long[size];
            for ( int i = 0; i < size; i++ )
            {
                array[i] = source.readLong();
            }

            return (V) array;
        }
    }
}
