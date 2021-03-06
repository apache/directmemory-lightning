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
package org.apache.directmemory.lightning.logging;

public interface Logger
{

    Logger getChildLogger( Class<?> clazz );

    Logger getChildLogger( String name );

    String getName();

    boolean isLogLevelEnabled( LogLevel logLevel );

    boolean isTraceEnabled();

    boolean isDebugEnabled();

    boolean isInfoEnabled();

    boolean isWarnEnabled();

    boolean isErrorEnabled();

    boolean isFatalEnabled();

    void trace( String message );

    void trace( String message, Throwable throwable );

    void debug( String message );

    void debug( String message, Throwable throwable );

    void info( String message );

    void info( String message, Throwable throwable );

    void warn( String message );

    void warn( String message, Throwable throwable );

    void error( String message );

    void error( String message, Throwable throwable );

    void fatal( String message );

    void fatal( String message, Throwable throwable );

}
