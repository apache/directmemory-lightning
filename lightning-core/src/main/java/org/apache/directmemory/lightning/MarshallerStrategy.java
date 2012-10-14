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

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * The MarshallerStrategy is used to find a marshaller for a given type in a {@link MarshallerContext}. This class is
 * used internally for finding generated {@link Marshaller} implementations.
 */
public interface MarshallerStrategy
{

    /**
     * Returns a {@link Marshaller} implementation found in the given {@link MarshallerContext} for type.
     * 
     * @param type The type to search with
     * @param marshallerContext The {@link MarshallerContext} to search in
     * @return The {@link Marshaller} implementation
     */
    Marshaller getMarshaller( Type type, MarshallerContext marshallerContext );

    /**
     * Returns a {@link Marshaller} implementation found in the given {@link MarshallerContext} for type. If
     * baseMarshallerOnly is set no {@link Serializable} {@link Marshaller} will be used in hope to find a more suitable
     * one.
     * 
     * @param type The type to search with
     * @param marshallerContext The {@link MarshallerContext} to search in
     * @param baseMarshallersOnly If set to false the {@link Serializable} {@link Marshaller} implementation will be
     *            skipped when searching for a suitable one
     * @return The {@link Marshaller} implementation
     */
    Marshaller getMarshaller( Type type, MarshallerContext marshallerContext, boolean baseMarshallersOnly );

}
