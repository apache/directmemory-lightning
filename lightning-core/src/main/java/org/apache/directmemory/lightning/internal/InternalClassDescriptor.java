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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.directmemory.lightning.Marshaller;
import org.apache.directmemory.lightning.logging.Logger;
import org.apache.directmemory.lightning.metadata.ClassDefinition;
import org.apache.directmemory.lightning.metadata.ClassDescriptor;
import org.apache.directmemory.lightning.metadata.PropertyDescriptor;

public class InternalClassDescriptor
    implements ClassDescriptor
{

    private final Set<PropertyDescriptor> propertyDescriptors = new HashSet<PropertyDescriptor>();

    private final Logger logger;

    private final Class<?> type;

    private ClassDefinition classDefinition;

    private Marshaller marshaller;

    public InternalClassDescriptor( Class<?> type, Logger logger )
    {
        this.type = type;
        this.logger = logger;
    }

    @Override
    public ClassDefinition getClassDefinition()
    {
        return classDefinition;
    }

    @Override
    public Class<?> getType()
    {
        return type;
    }

    @Override
    public List<PropertyDescriptor> getPropertyDescriptors()
    {
        return new ArrayList<PropertyDescriptor>( propertyDescriptors );
    }

    @Override
    public Marshaller getMarshaller()
    {
        return marshaller;
    }

    public boolean push( PropertyDescriptor propertyDescriptor )
    {
        return propertyDescriptors.add( propertyDescriptor );
    }

    public void setMarshaller( Marshaller marshaller )
    {
        this.marshaller = marshaller;
    }

    public ClassDescriptor build( ClassDefinition[] classDefinitions )
    {
        for ( ClassDefinition classDefinition : classDefinitions )
        {
            if ( classDefinition.getType() == type )
            {
                this.classDefinition = classDefinition;
                return this;
            }
        }

        classDefinition = new InternalClassDefinition( getType(), getPropertyDescriptors(), logger );
        return this;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( type == null ) ? 0 : type.hashCode() );
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
        InternalClassDescriptor other = (InternalClassDescriptor) obj;
        if ( type == null )
        {
            if ( other.type != null )
            {
                return false;
            }
        }
        else if ( !type.equals( other.type ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "InternalClassDescriptor [propertyDescriptors=" + propertyDescriptors + ", type=" + type
            + ", classDefinition=" + classDefinition + ", marshaller=" + marshaller + "]";
    }
}
