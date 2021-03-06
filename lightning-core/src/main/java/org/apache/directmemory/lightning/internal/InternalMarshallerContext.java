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
package org.apache.directmemory.lightning.internal;

import java.lang.reflect.Type;
import org.apache.directmemory.lightning.Marshaller;
import org.apache.directmemory.lightning.MarshallerContext;
import org.apache.directmemory.lightning.internal.util.FastIntMap;

public class InternalMarshallerContext
    implements MarshallerContext
{

    private final MarshallerContext parentMarshallerContext;

    // private final ObjectObjectMap<Type, Marshaller> marshallers = new ObjectObjectOpenHashMap<Type, Marshaller>();
    // private Map<Type, Marshaller> marshallers;
    private final FastIntMap<Marshaller> marshallers;

    public InternalMarshallerContext( FastIntMap<Marshaller> marshallers )
    {
        this( null, marshallers );
    }

    public InternalMarshallerContext( MarshallerContext parentMarshallerContext )
    {
        this( parentMarshallerContext, new FastIntMap<Marshaller>() );
    }

    public InternalMarshallerContext( MarshallerContext parentMarshallerContext, FastIntMap<Marshaller> marshallers )
    {
        this.parentMarshallerContext = parentMarshallerContext;
        this.marshallers = marshallers;
    }

    @Override
    public Marshaller getMarshaller( Type type )
    {
        Marshaller marshaller = marshallers.get( System.identityHashCode( type ) );
        if ( marshaller != null )
        {
            return marshaller;
        }

        if ( parentMarshallerContext != null )
        {
            return parentMarshallerContext.getMarshaller( type );
        }

        return null;
    }

    @Override
    public void bindMarshaller( Type type, Marshaller marshaller )
    {
        marshallers.put( System.identityHashCode( type ), marshaller );
    }

}
