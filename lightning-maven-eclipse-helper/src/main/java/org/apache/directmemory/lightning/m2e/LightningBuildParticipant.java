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
package org.apache.directmemory.lightning.m2e;

import java.io.File;
import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.codehaus.plexus.util.Scanner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.sonatype.plexus.build.incremental.BuildContext;

public class LightningBuildParticipant
    extends MojoExecutionBuildParticipant
{

    public LightningBuildParticipant( MojoExecution execution )
    {
        super( execution, true );
    }

    @Override
    public Set<IProject> build( int kind, IProgressMonitor monitor )
        throws Exception
    {
        IMaven maven = MavenPlugin.getMaven();
        BuildContext buildContext = getBuildContext();

        File classesDirectory =
            maven.getMojoParameterValue( getSession(), getMojoExecution(), "targetBuildDirectory", File.class );

        if ( classesDirectory == null )
        {
            return null;
        }

        Scanner scanner = buildContext.newScanner( classesDirectory );
        scanner.scan();

        String[] includedFiles = scanner.getIncludedFiles();
        if ( includedFiles == null || includedFiles.length == 0 )
        {
            return null;
        }

        Set<IProject> result = super.build( kind, monitor );

        File generatedDirectory =
            maven.getMojoParameterValue( getSession(), getMojoExecution(), "generatedSourceDirectory", File.class );
        if ( generatedDirectory != null )
        {
            buildContext.refresh( generatedDirectory );
        }

        buildContext.refresh( classesDirectory );

        return result;
    }

}
