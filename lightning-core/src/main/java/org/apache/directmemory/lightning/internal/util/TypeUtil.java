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
package org.apache.directmemory.lightning.internal.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class TypeUtil
{

    private TypeUtil()
    {
    }

    public static Class<?> getBaseType( Type type )
    {
        if ( type instanceof Class )
        {
            return (Class<?>) type;
        }

        if ( type instanceof ParameterizedType )
        {
            return (Class<?>) ( (ParameterizedType) type ).getRawType();
        }

        throw new IllegalStateException( "The requested type is an generic array or wildcard which is not supported" );
    }

    public static Type[] getTypeArgument( Type type )
    {
        if ( type instanceof Class )
        {
            return new Type[0];
        }

        if ( type instanceof ParameterizedType )
        {
            return ( (ParameterizedType) type ).getActualTypeArguments();
        }

        throw new IllegalStateException( "The requested type is an generic array or wildcard which is not supported" );
    }

}
