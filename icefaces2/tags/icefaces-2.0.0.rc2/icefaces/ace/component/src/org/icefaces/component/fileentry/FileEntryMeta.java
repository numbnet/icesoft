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

package org.icefaces.component.fileentry;

import org.icefaces.component.annotation.*;
import javax.el.MethodExpression;

import javax.faces.application.ResourceDependency;
import javax.faces.application.ResourceDependencies;

import org.icefaces.component.baseMeta.UIComponentBaseMeta;

@Component(
    tagName         = "fileEntry",
    componentClass  = "org.icefaces.component.fileentry.FileEntry",
    rendererClass   = "org.icefaces.component.fileentry.FileEntryRenderer",
    generatedClass  = "org.icefaces.component.fileentry.FileEntryBase",
    extendsClass    = "javax.faces.component.UIComponentBase",
    componentType   = "org.icefaces.FileEntry",
    rendererType    = "org.icefaces.FileEntryRenderer",
    componentFamily = "org.icefaces.FileEntry",
    tlddoc = "The FileEntry component allows for uploading files to the " +
        "server within a form submit POST. The actual POST is non-AJAX, " +
        "but leverages ICEfaces to return incremental page updates. " +
        "The upload occurs within as single JSF lifecycle, along with " +
        "the form's other component processing, allowing applications " +
        "to handle input field validation together with file content " +
        "validation, for model coherency. As well, server push is not " +
        "necessary for receiving the rendered view from the lifecycle " +
        "in which the files were uploaded.")
@ResourceDependency(name="fileEntry.js",library="org.icefaces.component.fileentry")
public class FileEntryMeta extends UIComponentBaseMeta {
    /*
    @Property(defaultValue="false")
    private boolean singleSubmit;
    */

    @Property(defaultValue="false",
        tlddoc="When disabled, files are not selectable for upload.")
    private boolean disabled;
    
    @Property(defaultValue="0", tlddoc="tabindex of the component")
    private int tabindex;
    
    @Property(tlddoc="style will be rendered on the root element of this " +
        "component.")
    private String style;
    
    @Property(tlddoc="style class will be rendered on the root element of " +
        "this component.")
    private String styleClass;
    
    @Property(defaultValue="false", tlddoc="When immediate is true, the " +
        "fileEntryListener will be invoked at the end of the " +
        "ApplyRequestValues phase. Otherwise, it will be invoked just " +
        "before rendering, so that the application will receive the event, " +
        "regardless of whether the form has passed validation or not.")
    private boolean immediate;

    @Property(tlddoc="A MethodExpression, which evaluates to a method in a " +
    	"bean, which takes an FileEntryEvent as a parameter. Invoked after " +
    	"file(s) have been uploaded, during a lifecycle phase that is " +
    	"determined by the immediate property. It can be used to retrieve the " +
    	"FileEntryResults object from the results property of the FileEntry " +
    	"component, giving access to the status information of the " +
    	"successfully, and unsuccessfully, uploaded files.",
    	expression=Expression.METHOD_EXPRESSION, methodExpressionArgument=
        "org.icefaces.component.fileentry.FileEntryEvent")
    private MethodExpression fileEntryListener;


    @Property(tlddoc="The absolute path, into the file-system, where the " +
        "files should be stored. If specified, this takes precedence over " +
        "the alternative property relativePath.")
    private String absolutePath;

    @Property(tlddoc="The relative path, inside of the web application " +
    	"deployment root directory, in the file-system, where the files " +
    	"should be stored. If specified, the alternative property, " +
    	"absolutePath, takes precedence over this property. If neither are " +
    	"specified, then the file is stored directly inside the deployment " +
    	"root directory.")
    private String relativePath;

    @Property(defaultValue="true", tlddoc="When constructing the path in " +
        "which to save the files, whether it be in the directory specified " +
        "by the absolutePath property, or inside of the web application " +
    	"deployment root directory, or inside a sub-directory of that, as " +
    	"specified by the relativePath property, when useSessionSubdir is " +
    	"true, then an additional sub-directory, will be used, that is the " +
    	"session id, to separate file uploads from different sessions, from " +
    	"each other.")
    private boolean useSessionSubdir;

    @Property(defaultValue="false", tlddoc="Uploaded files' names, as they " +
        "were on the user's file-system, are always provided to the " +
        "application, via the FileEntryResults.FileInfo.fileName property. " +
        "By default, the fileEntry component will store the " +
        "uploaded files on the server's file-system using a unique naming " +
        "convention, to ensure that new files do not over-write older files, " +
        "and that the names do not create security issues. The application " +
        "may then implement its own policy of maintaining old files or " +
        "over-writing them, as well as vetting file names based on any " +
        "particular rules specific to their deployment operating system. " +
        "Alternatively, they application may simply set this property to " +
        "true, so that uploaded files will be saved using the user's file " +
        "name, which will cause any pre-existing file using that name to be " +
        "over-written.")
    private boolean useOriginalFilename;

    @Property(tlddoc="Maintains the results of the most recent file upload " +
        "operation. From this, applications can retrieve the uploaded " +
        "files' information, such as the file name, MIME content type, " +
        "size, location where the file has been stored, and status of the " +
        "success of the upload. If saving a FileEntryResults object or " +
        "FileEntryResults.FileInfo objects, in your application, then save " +
        "a clone of the objects, instead.")
    private FileEntryResults results;

    //TODO
    //private FileEntryCallback callback;

    @Property(defaultValue="Long.MAX_VALUE", tlddoc="The maximum amount of " +
        "bytes allowed, in total, for all of the files uploaded, together. " +
        "If, for example, three files are uploaded, and the second one " +
        "exceeds maxTotalSize, then the second and third files will be " +
        "discarded.")
    private long maxTotalSize;

    @Property(defaultValue="Long.MAX_VALUE", tlddoc="The maximum amount of " +
        "bytes allowed, that each individual file may have. If a file " +
        "exceeds both maxFileSize and maxTotalSize, then maxFileSize will " +
        "be the reported error. If a file size exceeds maxFileSize, it is " +
        "completely discarded.")
    private long maxFileSize;

    @Property(defaultValue="10", tlddoc="The maximum number of files that " +
        "may be uploaded, per form submit upload operation, by this one " +
        "component. Any files uploaded, beyond this count, will be " +
        "discarded. Any subsequent form submit which uploads files will " +
        "restart the counting at zero.")
    private int maxFileCount;

    @Property(defaultValue="false", tlddoc="Similar to required property on " +
        "input components, when true, this states that at least one file " +
        "must be selected, and uploaded, by this component, when the form " +
        "is submitted.")
    private boolean required;

    //TODO
    //private String fileNamePattern;
    //private String acceptableFileExtensions;

    @Property(tlddoc="When faces messages are shown for this component, " +
        "the label is how this component is represented to the user")
    private String label;

    @Property(tlddoc="The localised message format string to use when " +
        "showing a faces message, when the maxSizeTotal has been exceeded")
    private String maxTotalSizeMessage;

    @Property(tlddoc="The localised message format string to use when " +
        "showing a faces message, when the maxSizePerFile has been exceeded")
    private String maxFileSizeMessage;

    @Property(tlddoc="The localised message format string to use when " +
        "showing a faces message, when the maxFileCount has been exceeded")
    private String maxFileCountMessage;

    @Property(tlddoc="The localised message format string to use when " +
        "showing a faces message, when the component has become invalid, " +
        "due to no files being uploaded")
    private String requiredMessage;
}
