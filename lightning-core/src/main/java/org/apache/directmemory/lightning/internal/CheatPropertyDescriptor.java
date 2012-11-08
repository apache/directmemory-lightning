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

import java.lang.annotation.Annotation;

import org.apache.directmemory.lightning.Marshaller;
import org.apache.directmemory.lightning.metadata.AccessorType;
import org.apache.directmemory.lightning.metadata.PropertyAccessor;
import org.apache.directmemory.lightning.metadata.PropertyDescriptor;

public class CheatPropertyDescriptor
    implements PropertyDescriptor
{

    private final String name;

    private final String propertyName;

    private final String internalSignature;

    private final String declaringCanonicalClassname;

    private final PropertyAccessor propertyAccessor;

    private final Marshaller marshaller;

    public CheatPropertyDescriptor( String propertyName, final Class<?> type, Marshaller marshaller )
    {
        this.name = propertyName;
        this.propertyName = propertyName;
        this.marshaller = marshaller;
        this.propertyAccessor = new PropertyAccessor()
        {

            @Override
            public boolean isArrayType()
            {
                return type.isArray();
            }

            @Override
            public Class<?> getType()
            {
                return type;
            }

            @Override
            public Class<?> getDefinedClass()
            {
                return type;
            }

            @Override
            public Class<?> getDeclaringClass()
            {
                return type;
            }

            @Override
            public AccessorType getAccessorType()
            {
                return AccessorType.Field;
            }
        };

        this.declaringCanonicalClassname = null;
        this.internalSignature = null;
    }

    @Override
    public int compareTo( PropertyDescriptor o )
    {
        return propertyName.compareTo( o.getPropertyName() );
    }

    @Override
    public Annotation[] getAnnotations()
    {
        return new Annotation[0];
    }

    @Override
    public Class<?> getDefinedClass()
    {
        return propertyAccessor.getDefinedClass();
    }

    @Override
    public Class<?> getDeclaringClass()
    {
        return propertyAccessor.getDeclaringClass();
    }

    @Override
    public PropertyAccessor getPropertyAccessor()
    {
        return propertyAccessor;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getPropertyName()
    {
        return propertyName;
    }

    @Override
    public Class<?> getType()
    {
        return propertyAccessor.getType();
    }

    @Override
    public String getInternalSignature()
    {
        return internalSignature;
    }

    @Override
    public Marshaller getMarshaller()
    {
        return marshaller;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result =
            prime * result + ( ( declaringCanonicalClassname == null ) ? 0 : declaringCanonicalClassname.hashCode() );
        result = prime * result + ( ( internalSignature == null ) ? 0 : internalSignature.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + ( ( propertyName == null ) ? 0 : propertyName.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        CheatPropertyDescriptor other = (CheatPropertyDescriptor) obj;
        if ( declaringCanonicalClassname == null )
        {
            if ( other.declaringCanonicalClassname != null )
            {
                return false;
            }
        }
        else if ( !declaringCanonicalClassname.equals( other.declaringCanonicalClassname ) )
        {
            return false;
        }
        if ( internalSignature == null )
        {
            if ( other.internalSignature != null )
            {
                return false;
            }
        }
        else if ( !internalSignature.equals( other.internalSignature ) )
        {
            return false;
        }
        if ( name == null )
        {
            if ( other.name != null )
            {
                return false;
            }
        }
        else if ( !name.equals( other.name ) )
        {
            return false;
        }
        if ( propertyName == null )
        {
            if ( other.propertyName != null )
            {
                return false;
            }
        }
        else if ( !propertyName.equals( other.propertyName ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "CheatingPropertyDescriptor [name=" + name + ", propertyName=" + propertyName + ", internalSignature="
            + internalSignature + ", declaringCanonicalClassname=" + declaringCanonicalClassname
            + ", propertyAccessor=" + propertyAccessor + ", marshaller=" + marshaller + "]";
    }
}
