package com.gyc.ace.bible;

import java.util.regex.Matcher;

import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import com.gyc.lit.LitUtils;

public class Adapter4MainList extends BaseAdapterNew {

	private MainActivity ma;

	Adapter4MainList(Activity mainActivity) {
		super(mainActivity);
		this.ma = (MainActivity) mainActivity;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.gyc.ace.bible.BaseAdapterNew#setListItemTextWithStyle(java.lang.
	 * CharSequence, android.widget.TextView)
	 */
	@Override
	protected void setListItemTextWithStyle(CharSequence charSequence,
			TextView tv) {
		Matcher mat = chapterPattern.matcher(charSequence);

		if (mat.find()) {
			int start = mat.start();
			int end = mat.end();

			final String xx = charSequence.toString();

			//String pre = xx.substring(0, start).trim();
			String ci = xx.substring(start, end);
			String post = xx.substring(end);

			if (this.ma.traditional) {
				post = LitUtils.toTraditional(post);
			}

			Spanned xx2 = Html.fromHtml("<font color='#00BB00' >" + ci
					+ "</FONT>" + post);

			tv.setText(xx2);

		} else {
			if (this.ma.traditional) {
				tv.setText(LitUtils.toTraditional(charSequence.toString()));
			} else {
				tv.setText(charSequence);
			}
		}

	}
}
