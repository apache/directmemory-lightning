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

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.directmemory.lightning.ClassComparisonStrategy;
import org.apache.directmemory.lightning.SerializationStrategy;
import org.apache.directmemory.lightning.Serializer;
import org.apache.directmemory.lightning.base.DefaultValueNullableEvaluator;
import org.apache.directmemory.lightning.configuration.SerializerDefinition;
import org.apache.directmemory.lightning.internal.InternalSerializerCreator;
import org.apache.directmemory.lightning.logging.Logger;
import org.apache.directmemory.lightning.logging.NoOpLogger;
import org.apache.directmemory.lightning.metadata.PropertyDescriptor;
import org.apache.directmemory.lightning.metadata.ValueNullableEvaluator;

public final class Lightning
{

    private Lightning()
    {
    }

    /**
     * Creates a new Lightning {@link Builder} for more complex configuration of a new {@link Serializer} instance.
     * 
     * @return A Builder instance for configuring a new {@link Serializer} instance.
     */
    public static Builder newBuilder()
    {
        return new Builder();
    }

    /**
     * Provides fast configuration of a new {@link Serializer} instance by just providing a bunch of
     * {@link SerializerDefinition}s.
     * 
     * @param serializerDefinitions The {@link SerializerDefinition}s to use for configuring the {@link Serializer}
     *            instance
     * @return A new {@link Serializer} instance configured using the given {@link SerializerDefinition}s
     */
    public static Serializer createSerializer( SerializerDefinition... serializerDefinitions )
    {
        return createSerializer( Arrays.asList( serializerDefinitions ) );
    }

    /**
     * Provides fast configuration of a new {@link Serializer} instance by just providing a bunch of
     * {@link SerializerDefinition}s.
     * 
     * @param serializerDefinitions The {@link SerializerDefinition}s to use for configuring the {@link Serializer}
     *            instance
     * @return A new {@link Serializer} instance configured using the given {@link SerializerDefinition}s
     */
    public static Serializer createSerializer( Iterable<? extends SerializerDefinition> serializerDefinitions )
    {
        return new Builder().serializerDefinitions( serializerDefinitions ).build();
    }

    public static class Builder
    {

        private Set<SerializerDefinition> serializerDefinitions = new HashSet<SerializerDefinition>();

        private SerializationStrategy serializationStrategy = SerializationStrategy.SpeedOptimized;

        private Class<? extends Annotation> attributeAnnotation = null;

        private ClassComparisonStrategy classComparisonStrategy = ClassComparisonStrategy.LightningChecksum;

        private ValueNullableEvaluator valueNullableEvaluator = new DefaultValueNullableEvaluator();

        private File debugCacheDirectory = null;

        private Logger logger = new NoOpLogger();

        private Builder()
        {
        }

        /**
         * Defines a basic {@link Annotation} to describe an class attribute (instance field) as an attribute (de-)
         * serialized by Lightning.
         * 
         * @param attributeAnnotation {@link Annotation} to describe a field as an serializable attribute
         * @return The actual builder instance for chaining
         */
        public Builder describesAttributs( Class<? extends Annotation> attributeAnnotation )
        {
            this.attributeAnnotation = attributeAnnotation;
            return this;
        }

        /**
         * Defines a directory where generated classes should be stored for debug purpose.
         * 
         * @param debugCacheDirectory Directory for storing generated class files
         * @return The actual builder instance for chaining
         */
        public Builder debugCacheDirectory( File debugCacheDirectory )
        {
            this.debugCacheDirectory = debugCacheDirectory;
            return this;
        }

        /**
         * <p>
         * The strategy to be used for serializing objects. There are two different strategies provided:
         * </p>
         * <p>
         * {@link SerializationStrategy#SpeedOptimized} does not look for same instances to be (de-) serialized only
         * once but written many times to the stream. This maybe needs more bytes in the stream but is faster because no
         * additional metadata needs to be managened.<br>
         * {@link SerializationStrategy#SizeOptimized} manages metadata of the allready serialized data and will not
         * serialize them again (but just writing an object-id to the stream). This will likely save bytes but managing
         * the metadata is using some time.
         * </p>
         * 
         * @param serializationStrategy The strategy to be used when serialize data
         * @return The actual builder instance for chaining
         */
        public Builder serializationStrategy( SerializationStrategy serializationStrategy )
        {
            this.serializationStrategy = serializationStrategy;
            return this;
        }

        /**
         * <p>
         * The strategy to be used to compare class informations of different serializers (for example in clustered
         * environments). Lightning offers three different strategies:
         * </p>
         * <p>
         * {@link ClassComparisonStrategy#LightningChecksum} is a lightweight checksum only taking the defined
         * attributes into account and is pretty insensitive to other changes in classes but adding / removing of
         * attributes is not supported. This strategy is used by default.<br>
         * {@link ClassComparisonStrategy#SerialVersionUID} is using the standard Java serialVersionUID fields (or if
         * not provided calculates it's value from the given class). Using this value you can force Lightning to see
         * different versions of a class to be compatible but only adding attributes in newer class versions is
         * supported.<br>
         * {@link ClassComparisonStrategy#SkipComparison} skips any kind of comparison check and can be used when user
         * provided marshallers can handle different versions of classes. This could be used to implement some kind of
         * schema evolution but is only recommended for export use.
         * </p>
         * 
         * @param classComparisonStrategy The strategy to be used when compare class information
         * @return The actual builder instance for chaining
         */
        public Builder classComparisonStrategy( ClassComparisonStrategy classComparisonStrategy )
        {
            this.classComparisonStrategy = classComparisonStrategy;
            return this;
        }

        /**
         * Defines a bunch of {@link SerializerDefinition}s to be used by the final {@link Serializer}. Multiple calls
         * of this methods are possible.
         * 
         * @param serializerDefinitions The {@link SerializerDefinition}s to use for configuring the {@link Serializer}
         * @return The actual builder instance for chaining
         */
        public Builder serializerDefinitions( SerializerDefinition... serializerDefinitions )
        {
            return serializerDefinitions( Arrays.asList( serializerDefinitions ) );
        }

        /**
         * Defines a bunch of {@link SerializerDefinition}s to be used by the final {@link Serializer}. Multiple calls
         * of this methods are possible.
         * 
         * @param serializerDefinitions The {@link SerializerDefinition}s to use for configuring the {@link Serializer}
         * @return The actual builder instance for chaining
         */
        public Builder serializerDefinitions( Iterable<? extends SerializerDefinition> serializerDefinitions )
        {
            for ( SerializerDefinition serializerDefinition : serializerDefinitions )
            {
                this.serializerDefinitions.add( serializerDefinition );
            }
            return this;
        }

        /**
         * Defines a user implementation of the {@link ValueNullableEvaluator} interface which is used to evaluate if a
         * {@link PropertyDescriptor} defined attribute can be null. A custom implementation should extends
         * {@link DefaultValueNullableEvaluator} to inherit all standard behavior. Custom implementations are needed to
         * give Lightning an information if a value is nullable when using customized attribute annotations.
         * 
         * @param valueNullableEvaluator A {@link ValueNullableEvaluator} implementation to evaluate a value is nullable
         *            or not
         * @return The actual builder instance for chaining
         */
        public Builder setValueNullableEvaluator( ValueNullableEvaluator valueNullableEvaluator )
        {
            this.valueNullableEvaluator = valueNullableEvaluator;
            return this;
        }

        /**
         * A custom logger implementation can be given to the {@link Serializer} to redirect logging output to any other
         * logging framework like slf4j, log4j, java.util.Logging or even a custom framework.
         * 
         * @param logger The {@link Logger} implementation to be used for logging events.
         * @return The actual builder instance for chaining
         */
        public Builder logger( Logger logger )
        {
            this.logger = logger;
            return this;
        }

        /**
         * Finally builds the {@link Serializer} using the given configurations. In that step all {@link Marshaller}
         * implementations (that are not provided by the user) are autogenerated using the given
         * {@link SerializerDefinition}s.
         * 
         * @return The configured and ready to use {@link Serializer}
         */
        public Serializer build()
        {
            return new InternalSerializerCreator().setLogger( logger ).setSerializationStrategy( serializationStrategy ).setClassComparisonStrategy( classComparisonStrategy ).setAttributeAnnotation( attributeAnnotation ).setDebugCacheDirectory( debugCacheDirectory ).setValueNullableEvaluator( valueNullableEvaluator ).addSerializerDefinitions( serializerDefinitions ).build();
        }
    }

}
