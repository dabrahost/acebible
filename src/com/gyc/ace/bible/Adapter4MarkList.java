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

import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

public class Adapter4MarkList extends BaseAdapterNew {

	Adapter4MarkList(Activity mainActivity) {
		super(mainActivity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.gyc.ace.bible.BaseAdapterNew#setListItemTextWithStyle(java.lang.
	 * CharSequence, android.widget.TextView)
	 */
	@Override
	protected void setListItemTextWithStyle(CharSequence markToString,
			TextView tv) {
		// this.volume + "  " + this.chapter + ":" + this.subchapter + "  "
		// this.tags;

		String mark = markToString.toString();
		String[] tokens = mark.split("\\s{2,}");

		String volume = tokens[0];
		String chaptersandsub = tokens[1];
		String content = tokens[2];
		String tags = tokens[3];
		// String tags = contenttags[1];

		Spanned span = Html.fromHtml("<font color='#33CC33'>"
				+ volume.replaceAll("^\\d*?\\s", "") + "</FONT>" + "  "
				+ "<font color='#FFFF00'>" + chaptersandsub + "</FONT>"
				+ "<br/>" + content + "     " + "<font color='#99FFCC'>"
				+ "----" + tags + "</FONT>");
		tv.setText(span);

	}
}
