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

public class IntegerArrayMarshaller
    extends AbstractMarshaller
{

    @Override
    public boolean acceptType( Class<?> type )
    {
        return int[].class == type || Integer[].class == type;
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

        if ( int[].class == propertyDescriptor.getType() )
        {
            int[] array = (int[]) value;
            target.writeInt( array.length );

            for ( int arrayValue : array )
            {
                target.writeInt( arrayValue );
            }
        }
        else
        {
            Integer[] array = (Integer[]) value;
            target.writeInt( array.length );

            for ( int arrayValue : array )
            {
                target.writeInt( arrayValue );
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
        if ( int[].class == propertyDescriptor.getType() )
        {
            int[] array = new int[size];
            for ( int i = 0; i < size; i++ )
            {
                array[i] = source.readInt();
            }

            return (V) array;
        }
        else
        {
            Integer[] array = new Integer[size];
            for ( int i = 0; i < size; i++ )
            {
                array[i] = source.readInt();
            }

            return (V) array;
        }
    }
}
