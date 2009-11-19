package com.gyc.ace.bible;

import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends Activity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.help);
		
		TextView tv = (TextView) this.findViewById(R.id.textview4help);
		tv.setAutoLinkMask(Linkify.ALL);
		tv.setLinksClickable(true);

		Button rt = (Button) this.findViewById(R.id.btnHelpRtn);
		rt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HelpActivity.this.finish();
			}

		});
	}

}
