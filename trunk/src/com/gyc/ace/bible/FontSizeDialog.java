package com.gyc.ace.bible;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class FontSizeDialog extends Dialog implements OnClickListener,
		SeekBar.OnSeekBarChangeListener {
	private TextView tv;
	MainActivity activity;
	private int ts;

	public FontSizeDialog(MainActivity context) {
		super(context);
		this.activity = context;

	}

	protected void onStart() {
		super.onStart();
		setContentView(R.layout.dlg_font_size);
		// ????
		getWindow().setFlags(4, 4);

		setTitle("字体大小");

		Button ok = (Button) this.findViewById(R.id.fs_ok);
		Button cancel = (Button) this.findViewById(R.id.fs_cancel);

		tv = (TextView) this.findViewById(R.id.fs_demo);
		SeekBar sk = (SeekBar) this.findViewById(R.id.fs_seek);

		sk.setProgress(4 * (activity.listTextSize - 10));

		sk.setOnSeekBarChangeListener(this);

		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int vid = v.getId();

		if (vid == R.id.fs_ok) {
			this.activity.listTextSize = ts;
			this.activity.setViewMain();
			this.dismiss();
		} else if (vid == R.id.fs_cancel) {
			this.activity.setViewMain();
			this.dismiss();
		}

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

		ts = 10 + progress / 4;

		tv.setTextSize(ts);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO do nothing
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO do nothing
	}

}
