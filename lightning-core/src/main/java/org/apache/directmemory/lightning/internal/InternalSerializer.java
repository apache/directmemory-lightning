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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.directmemory.lightning.ClassComparisonStrategy;
import org.apache.directmemory.lightning.Marshaller;
import org.apache.directmemory.lightning.MarshallerStrategy;
import org.apache.directmemory.lightning.SerializationContext;
import org.apache.directmemory.lightning.SerializationStrategy;
import org.apache.directmemory.lightning.Source;
import org.apache.directmemory.lightning.Target;
import org.apache.directmemory.lightning.exceptions.ClassDefinitionInconsistentException;
import org.apache.directmemory.lightning.exceptions.SerializerExecutionException;
import org.apache.directmemory.lightning.instantiator.ObjectInstantiatorFactory;
import org.apache.directmemory.lightning.internal.generator.BytecodeMarshallerGenerator;
import org.apache.directmemory.lightning.internal.generator.MarshallerGenerator;
import org.apache.directmemory.lightning.internal.util.FastIntMap;
import org.apache.directmemory.lightning.logging.Logger;
import org.apache.directmemory.lightning.metadata.ClassDefinition;
import org.apache.directmemory.lightning.metadata.ClassDefinitionContainer;
import org.apache.directmemory.lightning.metadata.ClassDescriptor;
import org.apache.directmemory.lightning.metadata.PropertyDescriptor;
import org.apache.directmemory.lightning.metadata.ValueNullableEvaluator;

class InternalSerializer
    implements ClassDescriptorAwareSerializer
{

    private final AtomicReference<ClassDefinitionContainer> classDefinitionContainer =
        new AtomicReference<ClassDefinitionContainer>();

    private final MarshallerGenerator marshallerGenerator = new BytecodeMarshallerGenerator();

    private final ObjectInstantiatorFactory objectInstantiatorFactory;

    private final ClassComparisonStrategy classComparisonStrategy;

    private final Map<Class<?>, ClassDescriptor> classDescriptors;

    private final SerializationStrategy serializationStrategy;

    private final FastIntMap<Marshaller> definedMarshallers;

    private final MarshallerStrategy marshallerStrategy;

    private final ValueNullableEvaluator valueNullableEvaluator;

    InternalSerializer( ClassDefinitionContainer classDefinitionContainer, SerializationStrategy serializationStrategy,
                        ClassComparisonStrategy classComparisonStrategy,
                        Map<Class<?>, ClassDescriptor> classDescriptors, Map<Type, Marshaller> marshallers,
                        ObjectInstantiatorFactory objectInstantiatorFactory, Logger logger,
                        MarshallerStrategy marshallerStrategy, File debugCacheDirectory,
                        ValueNullableEvaluator valueNullableEvaluator )
    {

        this.classDefinitionContainer.set( classDefinitionContainer );
        this.classComparisonStrategy = classComparisonStrategy;
        this.classDescriptors = Collections.unmodifiableMap( classDescriptors );
        this.serializationStrategy = serializationStrategy;
        this.valueNullableEvaluator = valueNullableEvaluator;

        for ( ClassDescriptor classDescriptor : classDescriptors.values() )
        {
            if ( classDescriptor instanceof InternalClassDescriptor && classDescriptor.getMarshaller() == null )
            {
                Marshaller marshaller =
                    marshallerGenerator.generateMarshaller( classDescriptor.getType(),
                                                            classDescriptor.getPropertyDescriptors(), marshallers,
                                                            this, serializationStrategy, objectInstantiatorFactory,
                                                            debugCacheDirectory, logger );

                ( (InternalClassDescriptor) classDescriptor ).setMarshaller( marshaller );
                marshallers.put( classDescriptor.getType(), marshaller );
            }
        }

        this.definedMarshallers = new FastIntMap<Marshaller>( marshallers.size() );
        for ( Entry<Type, Marshaller> entry : marshallers.entrySet() )
        {
            this.definedMarshallers.put( System.identityHashCode( entry.getKey() ), entry.getValue() );
        }

        this.marshallerStrategy = marshallerStrategy;
        this.objectInstantiatorFactory = objectInstantiatorFactory;
    }

    @Override
    public ClassDefinitionContainer getClassDefinitionContainer()
    {
        return classDefinitionContainer.get();
    }

    @Override
    public void setClassDefinitionContainer( ClassDefinitionContainer classDefinitionContainer )
    {
        // Pre-check if checksums of remote classes passing
        ClassDefinitionContainer oldClassDefinitionContainer = getClassDefinitionContainer();
        consistencyCheckClassChecksums( oldClassDefinitionContainer, classDefinitionContainer );

        // Set new ClassDefinitionContainer if checking succeed
        this.classDefinitionContainer.set( classDefinitionContainer );
    }

    @Override
    public <V> void serialize( V value, Target target )
    {
        try
        {
            SerializationContext serializationContext =
                new InternalSerializationContext( classDefinitionContainer.get(), serializationStrategy,
                                                  marshallerStrategy, objectInstantiatorFactory,
                                                  valueNullableEvaluator, definedMarshallers );

            Class<?> type = value.getClass();
            ClassDescriptor classDescriptor = findClassDescriptor( type );
            Marshaller marshaller = classDescriptor.getMarshaller();
            PropertyDescriptor pd = new CheatPropertyDescriptor( "serialize", classDescriptor.getType(), marshaller );

            target.writeLong( classDescriptor.getClassDefinition().getId() );
            marshaller.marshall( value, pd, target, serializationContext );
        }
        catch ( IOException e )
        {
            throw new SerializerExecutionException( "Error while serializing value", e );
        }
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <V> V deserialize( Source source )
    {
        try
        {
            SerializationContext serializationContext =
                new InternalSerializationContext( classDefinitionContainer.get(), serializationStrategy,
                                                  marshallerStrategy, objectInstantiatorFactory,
                                                  valueNullableEvaluator, definedMarshallers );

            long typeId = source.readLong();
            Class<?> clazz = classDefinitionContainer.get().getTypeById( typeId );
            ClassDescriptor classDescriptor = findClassDescriptor( clazz );
            Marshaller marshaller = classDescriptor.getMarshaller();
            PropertyDescriptor pd = new CheatPropertyDescriptor( "serialize", classDescriptor.getType(), marshaller );

            return (V) marshaller.unmarshall( pd, source, serializationContext );
        }
        catch ( IOException e )
        {
            throw new SerializerExecutionException( "Error while deserializing value", e );
        }
    }

    @Override
    public ClassDescriptor findClassDescriptor( Class<?> type )
    {
        return classDescriptors.get( type );
    }

    private void consistencyCheckClassChecksums( ClassDefinitionContainer oldClassDefinitionContainer,
                                                 ClassDefinitionContainer classDefinitionContainer )
    {
        for ( ClassDefinition classDefinition : classDefinitionContainer.getClassDefinitions() )
        {
            ClassDefinition oldClassDefinition =
                oldClassDefinitionContainer.getClassDefinitionByCanonicalName( classDefinition.getCanonicalName() );
            if ( oldClassDefinition == null )
            {
                throw new ClassDefinitionInconsistentException( "No ClassDefinition for type "
                    + classDefinition.getCanonicalName() + " was found" );
            }

            if ( classComparisonStrategy != ClassComparisonStrategy.SkipComparison )
            {
                if ( classComparisonStrategy == ClassComparisonStrategy.SerialVersionUID )
                {
                    long serialVersionUID = classDefinition.getSerialVersionUID();
                    long oldSerialVersionUID = oldClassDefinition.getSerialVersionUID();
                    if ( serialVersionUID != oldSerialVersionUID )
                    {
                        throw new ClassDefinitionInconsistentException( "SerialVersionUID of type "
                            + classDefinition.getCanonicalName() + " is not constistent" );
                    }
                }
                else
                {
                    byte[] checksum = classDefinition.getChecksum();
                    byte[] oldChecksum = oldClassDefinition.getChecksum();
                    if ( !Arrays.equals( checksum, oldChecksum ) )
                    {
                        throw new ClassDefinitionInconsistentException( "Signature checksum of type "
                            + classDefinition.getCanonicalName() + " is not constistent" );
                    }
                }
            }
        }
    }
}
