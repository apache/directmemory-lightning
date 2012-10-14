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

/**
 * <p>
 * Defines the comparison strategy of classes between different serializers. The standard strategy of Java is
 * SerialVersionUID but Lightning has some lighter algorithm which only takes properties into account.
 * </p>
 * <p>
 * {@link #LightningChecksum} is a lightweight checksum only taking the defined attributes into account and is pretty
 * insensitive to other changes in classes but adding / removing of attributes is not supported. This strategy is used
 * by default.<br>
 * {@link #SerialVersionUID} is using the standard Java serialVersionUID fields (or if not provided calculates it's
 * value from the given class). Using this value you can force Lightning to see different versions of a class to be
 * compatible but only adding attributes in newer class versions is supported.<br>
 * {@link #SkipComparison} skips any kind of comparison check and can be used when user provided marshallers can handle
 * different versions of classes. This could be used to implement some kind of schema evolution but is only recommended
 * for export use.
 * </p>
 */
public enum ClassComparisonStrategy
{

    /**
     * Default Java Serialization like SerialVersionUID
     */
    SerialVersionUID,

    /**
     * Lightning checksum calculation
     */
    LightningChecksum,

    /**
     * Instructs Lightning to skip all kinds of comparison between the different ClassDefinitionContainers. This is not
     * recommended but is required for possible schema evolution features.
     */
    SkipComparison

}
