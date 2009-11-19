package com.gyc.ace.bible;

import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.gyc.lit.LitUtils;

public class Adapter4SearchList extends BaseAdapterNew {

	private String searchString;
	private boolean traditional;
	private String searchStringTraditionl;

	Adapter4SearchList(Activity mainActivity, String target, boolean traditional) {
		super(mainActivity);
		this.traditional = traditional;

		if (this.traditional) {
			searchString = LitUtils.toSimplified(target);
			searchStringTraditionl = LitUtils.toTraditional(target);

		} else {
			searchString = target;
			searchStringTraditionl = target;
		}

		// TODO Auto-generated constructor stub
	}

	private String get18n(String string) {

		if (this.traditional) {
			return LitUtils.toTraditional(string);
		}
		return string;
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

		String ori = charSequence.toString();

		int index = ori.indexOf(searchString);
		int length = this.searchString.length();

		if (index != 0) {
			String pre = ori.substring(0, index);
			String post = ori.substring(index + length);

			Spanned xx = Html.fromHtml(get18n(pre) + "<font color='#00BB00'>"
					+ searchStringTraditionl + "</FONT>" + get18n(post));

			tv.setText(xx);
			tv.setMovementMethod(LinkMovementMethod.getInstance());
		} else {
			String post = ori.substring(index + length);

			Spanned xx = Html.fromHtml("<font color='#00FF00'>" + searchStringTraditionl
					+ "</FONT>" + get18n(post));

			tv.setText(xx);
			tv.setMovementMethod(LinkMovementMethod.getInstance());

		}
	}
}
