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
package org.apache.directmemory.lightning.maven.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.Charset;

import org.junit.Test;

public class MavenGeneratorTestCase
{

    @Test
    public void testGeneration()
        throws Exception
    {
        File target = new File( "target/classes" );
        assertTrue( recursiveSearchClassFile( "FooLightningGeneratedMarshaller.class", target ) );

        File testfile = new File( getClass().getClassLoader().getResource( "generated.java.out" ).toURI() );
        File generatedFile =
            new File(
                      "target/generated-sources/lightning/org/apache/directmemory/lightning/maven/integration/FooLightningGeneratedMarshaller.java" );
        String expected = SupportUtil.readAllText( testfile, Charset.forName( "UTF-8" ) );
        String result = SupportUtil.readAllText( generatedFile, Charset.forName( "UTF-8" ) );
        assertEquals( expected, result );
    }

    private boolean recursiveSearchClassFile( String classFile, File path )
    {
        if ( path.isFile() && path.getName().equals( classFile ) )
        {
            return true;
        }

        if ( path.isDirectory() )
        {
            for ( File childPath : path.listFiles() )
            {
                if ( recursiveSearchClassFile( classFile, childPath ) )
                {
                    return true;
                }
            }
        }

        return false;
    }
}
