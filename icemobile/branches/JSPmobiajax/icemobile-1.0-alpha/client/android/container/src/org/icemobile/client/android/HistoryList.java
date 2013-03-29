/*
* Copyright 2004-2011 ICEsoft Technologies Canada Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions an
* limitations under the License.
*/ 

package org.icemobile.client.android;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.content.Intent;
import android.util.Log;

public class HistoryList extends ListActivity {

    private String[] history;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	history = getIntent().getStringArrayExtra("history");
	setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,history));
    }

    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	Object o = this.getListAdapter().getItem(position);
	String url = o.toString();
	Intent result = new Intent();
	result.putExtra("url",url);
	setResult(RESULT_OK,result);
	finish();
    }
}

