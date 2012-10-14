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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.directmemory.lightning.metadata.PropertyDescriptor;

/**
 * Implementations of the Marshaller interface are used to serialize / deserialize object instances. Normally marshaller
 * implementations are autogenerated by Lightning using the descriptions given {@link AbstractSerializerDefinition}s.
 */
public interface Marshaller
{

    /**
     * Checks if a given type can be (de-) serialized with this marshaller implementation.
     * 
     * @param type The type to check for being serializable by this marshaller
     * @return true if the type can be (de-) serialized by this marshaller otherwise it returns false
     */
    boolean acceptType( Class<?> type );

    /**
     * Marshalls (serializes) an given object instance of a supported type, previously checked by acceptType.
     * 
     * @param value Object instance to serialize
     * @param propertyDescriptor The {@link PropertyDescriptor} of this object
     * @param dataOutput The {@link DataOutput} implementation to write to
     * @param serializationContext The {@link SerializationContext} used to serialize this object graph
     * @throws IOException Throws {@link IOException} if any exception occurs while writing to the {@link DataOutput}
     */
    void marshall( Object value, PropertyDescriptor propertyDescriptor, DataOutput dataOutput,
                   SerializationContext serializationContext )
        throws IOException;

    /**
     * Unmarshalls (deserializes) an object from the given {@link DataInput}.
     * 
     * @param propertyDescriptor The {@link PropertyDescriptor} of this object
     * @param dataInput The {@link DataInput} implementation to read from
     * @param serializationContext The {@link SerializationContext} used to deserialize this object graph
     * @return An object read from the given {@link DataInput}
     * @throws IOException Throws {@link IOException} if any exception occurs while reading from the {@link DataInput}
     */
    <V> V unmarshall( PropertyDescriptor propertyDescriptor, DataInput dataInput,
                      SerializationContext serializationContext )
        throws IOException;

}