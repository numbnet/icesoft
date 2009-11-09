package org.icefaces.generator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.Writer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import org.icefaces.component.annotation.Component;

public class FileWriter {
    public static void write(String fileName, String path, StringBuilder contents) {
        Writer writer = null;
        try
        {
            String workingDir = URLDecoder.decode(getWorkingFolder()+ "../generated/");
            File folder = new File(workingDir+ path);
            System.out.println(folder + path+ fileName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            
            File file = new File(folder, fileName);

            writer = new BufferedWriter(new java.io.FileWriter(file));
            writer.write(contents.toString());
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
               if (writer != null)
                {
                    writer.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }
    
    public static String getWorkingFolder() {
        try {
            ClassLoader classLoader = Thread.currentThread()
            .getContextClassLoader();
            URL localUrl = classLoader.getResource(".");
            if(localUrl != null){
                return localUrl.getPath();
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    
    }
    
    public static void writeXML(Document doc, String filename, Properties properties) {
        try {
            // Prepare the DOM document for writing
            Source source = new DOMSource(doc);
            File folder = new File(URLDecoder.decode(getWorkingFolder()+"META-INF/"));
            
            if (!folder.exists()) {
                folder.mkdirs();
            }
            // Prepare the output file
            File file = new File(folder, filename);
            Result result = new StreamResult(file);
    
            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperties(properties);
            xformer.transform(source, result);

        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        }
    }
    
    public static void files(File folder) {

        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".java")) {
            System.out.println("File " + listOfFiles[i].getName() );
          } else if (listOfFiles[i].isDirectory()) {
            files(listOfFiles[i]);
          }
        }
    }
    
    public static String getPropertyValue(Class clazz, String fieldName, String methodName) {
        try {   
            Field field = clazz.getField(fieldName);
            return String.valueOf(field.get(field));
        } catch (Exception fe) {
            try {
                Method m = clazz.getMethod(methodName, null);
                Object o = clazz.newInstance();
                return String.valueOf(m.invoke(o, null));
            } catch (Exception me ) {
                me.printStackTrace();
            }
        }
    return null;
    }    
    
    public static List<Class> getAnnotatedCompsList() {
        File file = new File(URLDecoder.decode(FileWriter.getWorkingFolder()+"../../../generator/build"));

        URLClassLoader clazzLoader = null;
        try {
            URL url = file.toURL();
            clazzLoader = new URLClassLoader(new URL[]{url});
        } catch (Exception e) {
            e.printStackTrace();
        }    
        List<Class> componentsList = new ArrayList<Class>();
        processRequest(file, componentsList, clazzLoader);
       return componentsList; 
    }
    
    public static void processRequest(File file, List<Class> componentsList, URLClassLoader loader) {
        
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                String path = files[i].getPath();
                if (path.endsWith("class") && path.indexOf("org\\icefaces") > 1) {
                    path = path.substring(path.indexOf("org\\icefaces"), path.indexOf(".class"));
                    path = path.replace('\\', '.');
                     try {                    
                        Class c = loader.loadClass(path);
                        if (c.isAnnotationPresent(Component.class)) {
                            System.out.println("Meta class found = "+ path);
                            componentsList.add(c);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }                     
   
                }
               
                if (files[i].isDirectory()) {
                    processRequest(files[i], componentsList, loader);
                }
            }
        }
        
        

    }
}

