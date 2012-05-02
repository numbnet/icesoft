package org.icefaces.mobi.component.contentpane;



public class ContentPane extends ContentPaneBase {
     //  public static final String ACCORDION_FACET = "accordion";
       public static final String ClIENT_TYPE = "client";
       public static final String SERVER_NORMAL_TYPE = "constructed";
       public static final String SERVER_OPT_TYPE = "tobeconstructed";
       public static final String CONTENT_BASE_CLASS = "mobi-contentpane ";
       public static final String CONTENT_HIDDEN_CLASS = "mobi-contentpane-hidden ";

       public enum CacheType {
           client,
           constructed,
           tobeconstructed;
           public static final CacheType DEFAULT = CacheType.constructed;


           public boolean equals(String type){
               return this.name().equals(type);
           }
       }
  }
