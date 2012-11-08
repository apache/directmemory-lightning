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
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.directmemory.lightning.Marshaller;
import org.apache.directmemory.lightning.SerializationContext;
import org.apache.directmemory.lightning.Source;
import org.apache.directmemory.lightning.Target;
import org.apache.directmemory.lightning.TypeBindableMarshaller;
import org.apache.directmemory.lightning.base.AbstractMarshaller;
import org.apache.directmemory.lightning.exceptions.SerializerExecutionException;
import org.apache.directmemory.lightning.internal.CheatPropertyDescriptor;
import org.apache.directmemory.lightning.internal.util.TypeUtil;
import org.apache.directmemory.lightning.metadata.ClassDefinition;
import org.apache.directmemory.lightning.metadata.PropertyDescriptor;

public class MapMarshaller
    extends AbstractMarshaller
    implements TypeBindableMarshaller
{

    private final Type mapKeyType;

    private final Type mapValueType;

    private Marshaller mapKeyTypeMarshaller;

    private Marshaller mapValueTypeMarshaller;

    public MapMarshaller()
    {
        this( null, null );
    }

    private MapMarshaller( Type mapKeyType, Type mapValueType )
    {
        this.mapKeyType = mapKeyType;
        this.mapValueType = mapValueType;
    }

    @Override
    public boolean acceptType( Class<?> type )
    {
        return Map.class.isAssignableFrom( type );
    }

    @Override
    public void marshall( Object value, PropertyDescriptor propertyDescriptor, Target target,
                          SerializationContext serializationContext )
        throws IOException
    {

        if ( writePossibleNull( value, target ) )
        {
            Map<?, ?> map = (Map<?, ?>) value;
            target.writeInt( map.size() );

            Marshaller keyMarshaller = null;
            ClassDefinition keyClassDefinition = null;
            PropertyDescriptor keyPd = null;
            Marshaller valueMarshaller = null;
            ClassDefinition valueClassDefinition = null;
            PropertyDescriptor valuePd = null;
            if ( mapKeyType != null )
            {
                ensureMarshallersInitialized( serializationContext );
                keyMarshaller = mapKeyTypeMarshaller;
                Class<?> baseType = TypeUtil.getBaseType( mapKeyType );
                keyClassDefinition =
                    serializationContext.getClassDefinitionContainer().getClassDefinitionByType( baseType );
                keyPd =
                    new CheatPropertyDescriptor( propertyDescriptor.getPropertyName() + "Key", baseType, keyMarshaller );

                valueMarshaller = mapValueTypeMarshaller;
                baseType = TypeUtil.getBaseType( mapValueType );
                valueClassDefinition =
                    serializationContext.getClassDefinitionContainer().getClassDefinitionByType( baseType );
                valuePd =
                    new CheatPropertyDescriptor( propertyDescriptor.getPropertyName() + "Value", baseType,
                                                 valueMarshaller );
            }

            for ( Entry<?, ?> entry : map.entrySet() )
            {
                if ( mapKeyType == null )
                {
                    keyMarshaller =
                        entry.getKey() != null ? serializationContext.findMarshaller( entry.getKey().getClass() )
                                        : null;
                    keyClassDefinition =
                        serializationContext.getClassDefinitionContainer().getClassDefinitionByType( entry.getKey().getClass() );
                    keyPd =
                        new CheatPropertyDescriptor( propertyDescriptor.getPropertyName() + "Key",
                                                     entry.getKey().getClass(), keyMarshaller );

                    if ( entry.getValue() != null )
                    {
                        valueMarshaller =
                            entry.getValue() != null ? serializationContext.findMarshaller( entry.getValue().getClass() )
                                            : null;
                        valueClassDefinition =
                            serializationContext.getClassDefinitionContainer().getClassDefinitionByType( entry.getValue().getClass() );
                        valuePd =
                            new CheatPropertyDescriptor( propertyDescriptor.getPropertyName() + "Value",
                                                         entry.getValue().getClass(), valueMarshaller );
                    }
                }

                if ( writePossibleNull( entry.getKey(), target ) )
                {
                    target.writeLong( keyClassDefinition.getId() );
                    keyMarshaller.marshall( entry.getKey(), keyPd, target, serializationContext );
                }

                if ( writePossibleNull( entry.getValue(), target ) )
                {
                    target.writeLong( valueClassDefinition.getId() );
                    valueMarshaller.marshall( entry.getValue(), valuePd, target, serializationContext );
                }
            }
        }
    }

    @Override
    @SuppressWarnings( { "rawtypes", "unchecked" } )
    public <V> V unmarshall( PropertyDescriptor propertyDescriptor, Source source,
                             SerializationContext serializationContext )
        throws IOException
    {
        if ( isNull( source ) )
        {
            return null;
        }

        int size = source.readInt();
        Map map = new LinkedHashMap( size );
        if ( size > 0 )
        {
            for ( int i = 0; i < size; i++ )
            {
                Object key = null;
                if ( !isNull( source ) )
                {
                    long keyClassId = source.readLong();
                    ClassDefinition keyClassDefinition =
                        serializationContext.getClassDefinitionContainer().getClassDefinitionById( keyClassId );

                    Marshaller keyMarshaller;
                    if ( mapKeyType != null )
                    {
                        ensureMarshallersInitialized( serializationContext );
                        keyMarshaller = mapKeyTypeMarshaller;
                    }
                    else
                    {
                        keyMarshaller = serializationContext.findMarshaller( keyClassDefinition.getType() );
                    }

                    PropertyDescriptor pd =
                        new CheatPropertyDescriptor( propertyDescriptor.getPropertyName() + "Key",
                                                     keyClassDefinition.getType(), keyMarshaller );
                    key = keyMarshaller.unmarshall( pd, source, serializationContext );
                }

                Object value = null;
                if ( !isNull( source ) )
                {
                    long valueClassId = source.readLong();
                    ClassDefinition valueClassDefinition =
                        serializationContext.getClassDefinitionContainer().getClassDefinitionById( valueClassId );

                    Marshaller valueMarshaller;
                    if ( mapKeyType != null )
                    {
                        ensureMarshallersInitialized( serializationContext );
                        valueMarshaller = mapValueTypeMarshaller;
                    }
                    else
                    {
                        valueMarshaller = serializationContext.findMarshaller( valueClassDefinition.getType() );
                    }

                    PropertyDescriptor pd =
                        new CheatPropertyDescriptor( propertyDescriptor.getPropertyName() + "Value",
                                                     valueClassDefinition.getType(), valueMarshaller );
                    value = valueMarshaller.unmarshall( pd, source, serializationContext );
                }

                map.put( key, value );
            }
        }

        return (V) map;
    }

    @Override
    public Marshaller bindType( Type... bindingTypes )
    {
        if ( bindingTypes == null || bindingTypes.length == 0 )
        {
            return new MapMarshaller();
        }

        if ( bindingTypes.length != 2 )
        {
            throw new SerializerExecutionException( "Map type binding has no double generic: "
                + Arrays.toString( bindingTypes ) );
        }

        Class<?> mapKeyType = (Class<?>) bindingTypes[0];
        Class<?> mapValueType = (Class<?>) bindingTypes[1];
        return new MapMarshaller( mapKeyType, mapValueType );
    }

    private void ensureMarshallersInitialized( SerializationContext serializationContext )
    {
        if ( mapKeyTypeMarshaller != null && mapValueTypeMarshaller != null )
        {
            return;
        }

        mapKeyTypeMarshaller = serializationContext.findMarshaller( mapKeyType );
        mapValueTypeMarshaller = serializationContext.findMarshaller( mapValueType );
    }
}
