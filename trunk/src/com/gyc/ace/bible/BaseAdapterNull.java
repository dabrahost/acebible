/**
 * 
 */
package com.gyc.ace.bible;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

final class BaseAdapterNull extends BaseAdapter {

	public BaseAdapterNull(Activity activity) {
	}

	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
}