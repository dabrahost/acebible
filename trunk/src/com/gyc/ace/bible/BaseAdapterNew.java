/* 
 * Copyright (C) 2009 Guan YC yc.guanATgmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gyc.ace.bible;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

class BaseAdapterNew extends BaseAdapter {

	protected List<CharSequence> contentList;
	protected int textsize;

	protected static Pattern chapterPattern = Pattern.compile("\\d+:\\d+");

	/**
	 * 
	 */
	protected final Activity mainActivity;

	// private int resourceId = R.layout.select_check_text;
	protected int resourceId;

	/**
	 * @param mainActivity
	 */
	public BaseAdapterNew(Activity mainActivity) {
		this.mainActivity = mainActivity;
		// resourceId = R.layout.select_check_text;
		resourceId = R.layout.select_text;
		textsize = 20;
	}

	/**
	 * @return the textsize
	 */
	public int getTextsize() {
		return textsize;
	}

	/**
	 * @param textsize
	 *            the textsize to set
	 */
	public void setTextsize(int textsize) {
		this.textsize = textsize;
	}

	@Override
	public int getCount() {
		return contentList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return this.contentList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final CharSequence charSequence = (CharSequence) this.contentList
				.get(position);

		if (resourceId == R.layout.select_check_text) {
			CheckBox tv;
			if (convertView == null) {
				tv = (CheckBox) LayoutInflater.from(this.mainActivity).inflate(
						resourceId, parent, false);
			} else {
				tv = (CheckBox) convertView;
			}

			setListItemTextWithStyle(charSequence, tv);
			tv.setTextSize(textsize);
			return tv;
		} else {
			TextView tv;
			if (convertView == null) {
				tv = (TextView) LayoutInflater.from(this.mainActivity).inflate(
						resourceId, parent, false);
			} else {
				tv = (TextView) convertView;
			}

			tv.setTextSize(textsize);

			setListItemTextWithStyle(charSequence, tv);

			// tv.setText(charSequence);
			return tv;

		}
	}

	protected void setListItemTextWithStyle(final CharSequence charSequence,
			TextView tv) {

		Matcher mat = chapterPattern.matcher(charSequence);

		if (mat.find()) {
			int start = mat.start();
			int end = mat.end();

			final String xx = charSequence.toString();

			// String pre = xx.substring(0, start).trim();
			String ci = xx.substring(start, end);
			String post = xx.substring(end);

			Spanned xx2 = Html.fromHtml("<font color='#00BB00' >" + ci
					+ "</FONT>" + post);

			tv.setText(xx2);

		} else {
			tv.setText(charSequence);
		}

	}

	public void setContentList(List<CharSequence> contentList) {
		this.contentList = contentList;
	}

	public List<CharSequence> getContentList() {
		return contentList;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public int getResourceId() {
		return resourceId;
	}
}