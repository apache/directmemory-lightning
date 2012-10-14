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

import java.lang.reflect.Type;

/**
 * The MarshallerContext is used to collect all {@link Marshaller}s by type.
 */
public interface MarshallerContext
{

    /**
     * Returns a {@link Marshaller} by given type.
     * 
     * @param type The type to search the {@link Marshaller} for
     * @return The {@link Marshaller} implementation
     */
    Marshaller getMarshaller( Type type );

    /**
     * Binds a new {@link Marshaller} implementation to the given type
     * 
     * @param type The type to bind the {@link Marshaller} to
     * @param marshaller The {@link Marshaller} implementation to bind to the given type
     */
    void bindMarshaller( Type type, Marshaller marshaller );

}
