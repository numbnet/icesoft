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
package org.icefaces.mobi.component.viewselector;

/**
 * Model data for View Selector component which currently defines two view types,
 * small and large.  Which ever view type the component detects the respective
 * child elements are rendered.  .
 */
@Deprecated
public class ViewSelector extends ViewSelectorBase {

    // facet names that define the three parts of a page
    public static final String SMALL_FACET = "small";
    public static final String LARGE_FACET = "large";

    public static final String VIEW_TYPE_KEY = "mobile_view_type";

}