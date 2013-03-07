/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icemobile.samples.cyberspace;

import java.io.IOException;
import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.Map;
import java.util.LinkedList;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

public class Format3dTransformer  {
	private static final Logger log = Logger.getLogger(
        Format3dTransformer.class.getName());

    public static String MESH_COMMAND = "org.icemobile.meshSimplifyCommand";
    public static String MESH_DEFAULT = "slimmesh %1$s %2$s 1000";

    public static void simplifyMesh(File inFile, File outFile) 
            throws IOException {
        simplifyMesh((String) null, inFile, outFile);
    }

    public static void simplifyMesh(ServletContext servletContext, 
            File inFile, File outFile) throws IOException {
        String meshCommand = servletContext.getInitParameter(MESH_COMMAND);
        simplifyMesh(meshCommand, inFile, outFile);
    }

    public static void simplifyMesh(Map initParameterMap, 
            File inFile, File outFile) throws IOException {
        String meshCommand = (String) initParameterMap
            .get(MESH_COMMAND);
        simplifyMesh(meshCommand, inFile, outFile);
    }
    
    public static void simplifyMesh(String commandTemplate, 
            File inFile, File outFile) throws IOException {
        if (null == commandTemplate)  {
            commandTemplate = MESH_DEFAULT;
        }
        File offIn = getSiblingTempFile(inFile, ".off");
        File offOut = getSiblingTempFile(inFile, ".off");
        obj2off(inFile, offIn);
		StringBuilder command = new StringBuilder();
        Formatter formatter = new Formatter(command);
        formatter.format(commandTemplate, offIn.getAbsolutePath(),
                        offOut.getAbsolutePath());
System.out.println("COMMAND " + command.toString());
        Process process = Runtime.getRuntime().exec(command.toString());
        try {
            int exitValue = process.waitFor();
            off2Obj(offOut, outFile);
        } catch (Exception e)  {
            throw new IOException("Mesh simplification failed.", e);
        }
    }

    public static File getSiblingTempFile(File baseFile, String extension) 
            throws IOException  {
        File parentDir = baseFile.getParentFile();
        File tempFile = File.createTempFile("ice", extension, parentDir);
        return tempFile;
    }

    public static void off2Obj(File inFile, File outFile) throws IOException {
        log.info("off2Obj " + inFile + " to " + outFile);
        try {
            BufferedReader input = new BufferedReader(
                    new FileReader(inFile) );
            PrintWriter output = new PrintWriter(
                new BufferedWriter(new FileWriter(outFile)));
            off2Obj(input, output);
        } catch (IOException e)  {
            throw new IOException("Unable to transform " + inFile.getName()
            + " to " + outFile.getName(), e);
        }
    }

    public static void off2Obj(BufferedReader input, 
            PrintWriter output) throws IOException  {

        String line;

        line = input.readLine();
        if (!"OFF".equals(line))  {
            System.err.println("File does not start with off");
            System.out.println("[" + line + "]");
            throw new IOException("Malformed OFF input");
        }
        line = input.readLine();
        String[] geomCounts = line.split(" ");
        int vCount = Integer.parseInt(geomCounts[0]);
        int fCount = Integer.parseInt(geomCounts[1]);
        int eCount = Integer.parseInt(geomCounts[2]);
        
        //read blank line
        line = input.readLine();

        for (int i = 0; i < vCount; i++) {
            line = input.readLine();
            output.println("v " + line);
        }
        for (int i = 0; i < fCount; i++) {
            line = input.readLine();
            String[] faceParams = line.split(" +");
            int faceCount = Integer.parseInt(faceParams[0]);
            int v1 = Integer.parseInt(faceParams[1]) + 1;
            int v2 = Integer.parseInt(faceParams[2]) + 1;
            int v3 = Integer.parseInt(faceParams[3]) + 1;
            output.println("f " + v1 + " " + v2 + " " + v3);
        }
        
        output.flush();
        output.close();
    }


    public static void obj2off(File inFile, File outFile) throws IOException {
        log.info("obj2off " + inFile + " to " + outFile);
        BufferedReader input = new BufferedReader(
                new FileReader(inFile) );
        PrintWriter output = new PrintWriter(
            new BufferedWriter(new FileWriter(outFile)));
        obj2off(input, output);
    }

    public static void obj2off(BufferedReader input, 
            PrintWriter output) throws IOException {
    

        //need to store all this in memory to write out Nv Nf at the top
        //consider writing the file and then prepending this one line

        LinkedList<String> outList = new LinkedList();

        String line;
        String vertex = null;
        String fullVertex = null;
        String face = null;
        int vertexCount = 0;
        int faceCount = 0;
        while (null != (line = input.readLine())) {
            if (line.startsWith("v "))  {
                if (null != vertex)  {
                    fullVertex = vertex;
                }
                vertex = line.substring(2);
                //taking out normals
                fullVertex = vertex;
            }
            if (line.startsWith("vn"))  {
                //taking out normals
//                    fullVertex = "N " + vertex + line.substring(2);
                vertex = null;
            }
            if (line.startsWith("f "))  {
                String objFace = line.substring(2);
                //vertex texture normal
                String[] vtn = objFace.split(" ");
                int v0 = Integer.parseInt(vtn[0].split("/")[0]) - 1;
                int v1 = Integer.parseInt(vtn[1].split("/")[0]) - 1;
                int v2 = Integer.parseInt(vtn[2].split("/")[0]) - 1;
                face = "3 " + v0 + " " + v1 + " " + v2;
            }
            if (null != fullVertex) {
                outList.add(fullVertex);
                vertexCount++;
                fullVertex = null;
            }
            if (null != face) {
                outList.add(face);
                faceCount++;
                face = null;
            }
        }

        output.println("OFF");
        output.println(vertexCount + " " + faceCount + " " + 0);
        output.println();
        for (String outLine : outList)  {
            output.println(outLine);
        }

        output.flush();
        output.close();

    }

}