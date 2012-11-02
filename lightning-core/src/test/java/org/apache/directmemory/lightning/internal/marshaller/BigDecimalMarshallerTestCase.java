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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.util.Random;

import org.apache.directmemory.lightning.Lightning;
import org.apache.directmemory.lightning.Serializer;
import org.apache.directmemory.lightning.base.AbstractSerializerDefinition;
import org.apache.directmemory.lightning.internal.util.DebugLogger;
import org.apache.directmemory.lightning.io.InputStreamSource;
import org.apache.directmemory.lightning.io.OutputStreamTarget;
import org.apache.directmemory.lightning.metadata.Attribute;
import org.junit.Test;

public class BigDecimalMarshallerTestCase
{

    @Test
    public void testBigInteger()
        throws Exception
    {
        Serializer serializer =
            Lightning.newBuilder().logger( new DebugLogger() ).debugCacheDirectory( new File( "target" ) ).serializerDefinitions( new AbstractSerializerDefinition()
                                                                                                                                  {

                                                                                                                                      @Override
                                                                                                                                      protected void configure()
                                                                                                                                      {
                                                                                                                                          serialize(
                                                                                                                                                     BigDecimalHolder.class ).attributes();
                                                                                                                                      }
                                                                                                                                  } ).build();

        Random random = new Random( -System.nanoTime() );

        BigDecimalHolder value = new BigDecimalHolder();
        value.setValue1( BigDecimal.valueOf( random.nextLong() ).multiply( BigDecimal.valueOf( random.nextLong() ) ) );
        value.setValue2( null );
        value.setValue3( BigDecimal.valueOf( random.nextLong() ).subtract( BigDecimal.valueOf( random.nextLong() ) ) );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamTarget target = new OutputStreamTarget( baos );
        serializer.serialize( value, target );

        ByteArrayInputStream bais = new ByteArrayInputStream( baos.toByteArray() );
        InputStreamSource source = new InputStreamSource( bais );
        Object result = serializer.deserialize( source );

        assertNotNull( result );
        assertEquals( value, result );

        value = new BigDecimalHolder();
        value.setValue1( BigDecimal.valueOf( random.nextLong() ).min( BigDecimal.valueOf( random.nextLong() ) ) );
        value.setValue2( BigDecimal.valueOf( random.nextLong() ).divideToIntegralValue( BigDecimal.valueOf( random.nextLong() ) ) );
        value.setValue3( null );

        baos = new ByteArrayOutputStream();
        target = new OutputStreamTarget( baos );
        serializer.serialize( value, target );

        bais = new ByteArrayInputStream( baos.toByteArray() );
        source = new InputStreamSource( bais );
        result = serializer.deserialize( source );

        assertNotNull( result );
        assertEquals( value, result );

        value = new BigDecimalHolder();
        value.setValue1( null );
        value.setValue2( BigDecimal.valueOf( random.nextLong() ).max( BigDecimal.valueOf( random.nextLong() ) ) );
        value.setValue3( BigDecimal.valueOf( random.nextLong() ).add( BigDecimal.valueOf( random.nextLong() ) ) );

        baos = new ByteArrayOutputStream();
        target = new OutputStreamTarget( baos );
        serializer.serialize( value, target );

        bais = new ByteArrayInputStream( baos.toByteArray() );
        source = new InputStreamSource( bais );
        result = serializer.deserialize( source );

        assertNotNull( result );
        assertEquals( value, result );
    }

    public static class BigDecimalHolder
    {

        @Attribute
        private BigDecimal value1;

        @Attribute
        private BigDecimal value2;

        @Attribute
        private BigDecimal value3;

        public BigDecimal getValue1()
        {
            return value1;
        }

        public void setValue1( BigDecimal value1 )
        {
            this.value1 = value1;
        }

        public BigDecimal getValue2()
        {
            return value2;
        }

        public void setValue2( BigDecimal value2 )
        {
            this.value2 = value2;
        }

        public BigDecimal getValue3()
        {
            return value3;
        }

        public void setValue3( BigDecimal value3 )
        {
            this.value3 = value3;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ( ( value1 == null ) ? 0 : value1.hashCode() );
            result = prime * result + ( ( value2 == null ) ? 0 : value2.hashCode() );
            result = prime * result + ( ( value3 == null ) ? 0 : value3.hashCode() );
            return result;
        }

        @Override
        public boolean equals( Object obj )
        {
            if ( this == obj )
                return true;
            if ( obj == null )
                return false;
            if ( getClass() != obj.getClass() )
                return false;
            BigDecimalHolder other = (BigDecimalHolder) obj;
            if ( value1 == null )
            {
                if ( other.value1 != null )
                    return false;
            }
            else if ( !value1.equals( other.value1 ) )
                return false;
            if ( value2 == null )
            {
                if ( other.value2 != null )
                    return false;
            }
            else if ( !value2.equals( other.value2 ) )
                return false;
            if ( value3 == null )
            {
                if ( other.value3 != null )
                    return false;
            }
            else if ( !value3.equals( other.value3 ) )
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "BigIntegerHolder [value1=" + value1 + ", value2=" + value2 + ", value3=" + value3 + "]";
        }
    }
}
