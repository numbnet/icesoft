package org.icemobile.model;

import java.util.List;

/**
 * Copyright 2010-2013 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 */
@SuppressWarnings("rawtypes")
public class DataViewListDataModel extends DataViewDataModel {
    
    List list;

    public DataViewListDataModel(List list) {
        this.list = list;
    }

    public IndexedIterator<Object> iterator() {
        return new IndexedIterator<Object>(
                list.iterator(),
                getPageStartIndex()
            );
    }

    public Object getDataByIndex(int index) {
        Object retVal = null;
        if( list != null && index > -1 
            && list.size() > index && index < list.size() ){
                retVal = list.get(index);
        }
        return retVal;
    }

    public int length() {
        return list.size();
    }
}
