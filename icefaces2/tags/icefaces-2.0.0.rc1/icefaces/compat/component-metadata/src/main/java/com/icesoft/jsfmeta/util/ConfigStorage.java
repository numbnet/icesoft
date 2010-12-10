
/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package com.icesoft.jsfmeta.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * ConfigStorage pick up properties file
 */
public class ConfigStorage {
    
    private String fileName;
    
    private ConfigStorage(String fileName) {
        this.fileName = fileName;
    }
 
    public static ConfigStorage getInstance(String fileName){
        
        return new ConfigStorage(fileName);
    }
    
    public Properties loadProperties(){
        
        Properties properties = new Properties();
        try {
            properties.load(new BufferedInputStream(new FileInputStream(new File(fileName))));
        } catch (FileNotFoundException e){
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e){                        
            e.printStackTrace();
            System.exit(1);
        }
        
        return properties;
    }
    
    
    
}
