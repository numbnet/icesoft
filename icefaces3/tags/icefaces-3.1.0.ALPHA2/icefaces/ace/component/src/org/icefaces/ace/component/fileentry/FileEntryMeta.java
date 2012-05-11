/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

package org.icefaces.ace.component.fileentry;

import javax.el.MethodExpression;

import javax.faces.application.ResourceDependency;
import javax.faces.application.ResourceDependencies;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Implementation;
import org.icefaces.ace.meta.annotation.Property;

@Component(
    tagName         = "fileEntry",
    componentClass  = "org.icefaces.ace.component.fileentry.FileEntry",
    rendererClass   = "org.icefaces.ace.component.fileentry.FileEntryRenderer",
    generatedClass  = "org.icefaces.ace.component.fileentry.FileEntryBase",
    extendsClass    = "javax.faces.component.UIComponentBase",
    componentType   = "org.icefaces.ace.component.FileEntry",
    rendererType    = "org.icefaces.ace.component.FileEntryRenderer",
    componentFamily = "org.icefaces.ace.FileEntry",
    tlddoc = "<p>The FileEntry component allows for uploading files to the " +
        "server within a form submit POST. The actual POST is non-AJAX, " +
        "but leverages ICEfaces to return incremental page updates. " +
        "The upload occurs within as single JSF lifecycle, along with " +
        "the form's other component processing, allowing applications " +
        "to handle input field validation together with file content " +
        "validation, for model coherency. As well, server push is not " +
        "necessary for receiving the rendered view from the lifecycle " +
        "in which the files were uploaded. The progress indicator is " +
        "built-in, and will default to indeterminate mode. If server push " +
        "is enabled, then the component will automatically push progress " +
        "information to the indicator, and once that has been received, " +
        "the indicator will automatically switch to display the incremental" +
        "progress bar. The progress pushes are rate limited to be no more " +
        "frequent than once every 2 seconds, and are only sent at 1% " +
        "progress increments. <p>For more information, see the " +
        "<a href=\"http://wiki.icefaces.org/display/ICE/FileEntry\">FileEntry Wiki Documentation</a>.")
@ResourceDependencies({
    @ResourceDependency(library="icefaces.ace", name="fileentry/fileEntry.css"),
    @ResourceDependency(library="icefaces.ace", name="fileentry/fileEntry.js")
})
public class FileEntryMeta extends UIComponentBaseMeta {

    @Property(tlddoc="Defines whether or not the component is disabled. " +
        "When disabled='true', this component is unable to receive focus " +
        "and cannot be interacted with by the user.",
        defaultValue="false")
    private boolean disabled;
    
    @Property(tlddoc="tabindex of the component")
    private Integer tabindex;
    
    @Property(tlddoc="Custom inline CSS styles to use for this component. " +
        "These styles are generally applied to the root DOM element of the " +
        "component. This is intended for per-component basic style " +
        "customizations. Note that due to browser CSS precedence rules, CSS " +
        "rendered on a DOM element will take precedence over the external " +
        "stylesheets used to provide the ThemeRoller theme on this " +
        "component. If the CSS properties applied with this attribute do " +
        "not affect the DOM element you want to style, you may need to " +
        "create a custom theme styleClass for the theme CSS class that " +
        "targets the particular DOM elements you wish to customize.")
    private String style;
    
    @Property(tlddoc="Custom CSS style class(es) to use for this " +
        "component. These style classes can be defined in your page or in " +
        "a theme CSS file.")
    private String styleClass;

    @Property(tlddoc="When true, the fileEntryListener will be invoked at " +
        "the end of the APPLY_REQUEST_VALUES phase. Otherwise, it will be " +
        "invoked just before rendering, so that the application will " +
        "receive the event, regardless of whether the form has passed " +
        "validation or not.",
        defaultValue="false")
    private boolean immediate;

    @Property(tlddoc="MethodExpression, which must evaluate to a public " +
        "method that takes an FileEntryEvent as a parameter, with a return " +
        "type of void. Invoked after file(s) have been uploaded, during " +
        "a lifecycle phase that is determined by the immediate property. " +
        "It can be used to retrieve the FileEntryResults object from the " +
        "results property of the FileEntry component, giving access to the " +
        "status information of the successfully, and unsuccessfully, " +
        "uploaded files.",
        expression=Expression.METHOD_EXPRESSION,
        methodExpressionArgument=
            "org.icefaces.ace.component.fileentry.FileEntryEvent")
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

    @Property(tlddoc="When constructing the path in which to save the " +
        "files, whether it be in the directory specified by the " +
        "absolutePath property, or inside of the web application deployment " +
        "root directory, or inside a sub-directory of that, as specified by " +
        "the relativePath property, when useSessionSubdir is true, then an " +
        "additional sub-directory, will be used, that is the session id, to " +
        "separate file uploads from different sessions, from each other.",
        defaultValue="true")
    private boolean useSessionSubdir;

    @Property(tlddoc="Uploaded files' names, as they were on the user's " +
        "file-system, are always provided to the application, via the " +
        "FileEntryResults.FileInfo.fileName property. By default, the " +
        "fileEntry component will store the uploaded files on the server's " +
        "file-system using a unique naming convention, to ensure that new " +
        "files do not over-write older files, and that the names do not " +
        "create security issues. The application may then implement its own " +
        "policy of maintaining old files or over-writing them, as well as " +
        "vetting file names based on any particular rules specific to their " +
        "deployment operating system. Alternatively, they application may " +
        "simply set this property to true, so that uploaded files will be " +
        "saved using the user's file name, which will cause any " +
        "pre-existing file using that name to be over-written.",
        defaultValue="false")
    private boolean useOriginalFilename;

    @Property(tlddoc="Maintains the results of the most recent file upload " +
        "operation. From this, applications can retrieve the uploaded " +
        "files' information, such as the file name, MIME content type, " +
        "size, location where the file has been stored, and status of the " +
        "success of the upload. If saving a FileEntryResults object or " +
        "FileEntryResults.FileInfo objects, in your application, then save " +
        "a clone of the objects, instead.")
    private FileEntryResults results;

    //TODO Only generate getter that accesses ValueExpression
    // Right now, we'll use EXISTS_IN_SUPERCLASS to keep from generating
    // methods, even though this is not from any super class
    @Property(tlddoc="Specify a reference to a FileEntryCallback instance " +
        "using a ValueExpression. Using this will result in the uploaded " +
        "files not being written to the file system, and so precludes the " +
        "absolutePath, relativePath, useSessionSubdir, and " +
        "useOriginalFilename properties. This property supports scenarios " +
        "where the uploaded files will be stored in a database, or streamed " +
        "through to elsewhere, or virus scanned, or otherwise processed " +
        "before being saved.",
        expression= Expression.VALUE_EXPRESSION,
        implementation= Implementation.EXISTS_IN_SUPERCLASS)
    private FileEntryCallback callback;

    @Property(tlddoc="The maximum amount of bytes allowed, in total, for " +
        "all of the files uploaded, together. If, for example, three files " +
        "are uploaded, and the second one exceeds maxTotalSize, then the " +
        "second and third files will be discarded.",
        defaultValue="Long.MAX_VALUE")
    private long maxTotalSize;

    @Property(tlddoc="The maximum amount of bytes allowed, that each " +
        "individual file may have. If a file exceeds both maxFileSize and " +
        "maxTotalSize, then maxFileSize will be the reported error. If a " +
        "file size exceeds maxFileSize, it is completely discarded.",
        defaultValue="Long.MAX_VALUE")
    private long maxFileSize;

    @Property(tlddoc="The maximum number of files that may be uploaded, per " +
        "form submit upload operation, by this one component. Any files " +
        "uploaded, beyond this count, will be discarded. Any subsequent " +
        "form submit which uploads files will restart the counting at zero.",
        defaultValue="10")
    private int maxFileCount;

    @Property(tlddoc="Similar to required property on input components, " +
        "when true, this states that at least one file must be selected, " +
        "and uploaded, by this component, when the form is submitted.",
        defaultValue="false")
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
