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
package org.apache.directmemory.lightning.internal.generator;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.directmemory.lightning.Marshaller;
import org.apache.directmemory.lightning.SerializationStrategy;
import org.apache.directmemory.lightning.instantiator.ObjectInstantiatorFactory;
import org.apache.directmemory.lightning.internal.ClassDescriptorAwareSerializer;
import org.apache.directmemory.lightning.logging.Logger;
import org.apache.directmemory.lightning.metadata.PropertyDescriptor;

public interface MarshallerGenerator
{

    Marshaller generateMarshaller( Class<?> type, List<PropertyDescriptor> propertyDescriptors,
                                   Map<Class<?>, Marshaller> marshallers, ClassDescriptorAwareSerializer serializer,
                                   SerializationStrategy serializationStrategy,
                                   ObjectInstantiatorFactory objectInstantiatorFactory, File debugCacheDirectory,
                                   Logger logger );

}
