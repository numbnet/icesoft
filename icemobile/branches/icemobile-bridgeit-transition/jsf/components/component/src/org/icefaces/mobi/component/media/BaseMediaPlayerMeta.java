package org.icefaces.mobi.component.media;

import org.icefaces.ace.meta.annotation.Field;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.mobi.component.BaseLayoutMeta;

public class BaseMediaPlayerMeta extends BaseLayoutMeta{

        @Property(defaultValue = "auto", tlddoc = "Options for preloading the resource. Accepted values are " +
            "\"auto\" (allow the browser to decide), \"none\" or \"metadata\" (preload only the metadata). ")
    private String preload;

    @Property(defaultValue = "true", tlddoc = "If \"true\", will render the media controls. ")
    private boolean controls;

    @Property(tlddoc = "The media source. The value may resolve to a byte array, the String of a file name, " +
            "or a JSF Resource, if used in JSF project ")
    private Object value;

    @Property(defaultValue = "false", tlddoc = "If \"true\", will play the the audio file repeatedly. ")
    private boolean loop;

    @Property (defaultValue="false",
               tlddoc="Will cause the media to automatically play when the page is load. This may be required " +
                    "for some older devices to play. ")
    private boolean autoplay;

    @Property(defaultValue = "session", tlddoc="The JSF Resource scope of the object resolving from the \"value\" " +
            "attribute. Possible values are \"flash\", \"request\", \"view\", \"session\", and \"application\". ")
    private String scope;

    @Property(tlddoc = "The name is used for JSF Resource registration. ")
    private String name;

    @Property(tlddoc = "The library used for JSF Resource registration. ")
    private String library;

    @Property(tlddoc = "The URL or src of the media file. If the \"value\" attribute is empty, the \"url\" attribute will" +
            " be used. ")
    private String url;

    @Property(tlddoc = "The mime type of media file. ")
    private String type;

    @Property( tlddoc = "For certain devices, this adds the ability for a link to launch a system media player. If " +
            "this attribute is present thelink will appear with this text as the label.")
    private String linkLabel;

    @Property(defaultValue = "false", tlddoc=" audio will be muted if true. Default is false")
    private boolean muted;

    @Field
    private String srcAttribute;


}
