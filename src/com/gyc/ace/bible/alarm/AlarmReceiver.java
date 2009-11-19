package com.gyc.ace.bible.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gyc.ace.bible.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		//Toast.makeText(context, "圣经alarm", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
