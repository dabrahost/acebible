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
package com.gyc.ace.bible.alarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.gyc.ace.bible.R;

public class GenerateAlarm extends Activity {

	private final int YOURAPP_NOTIFICATION_ID = 9982;

	private NotificationManager nm;
	Toast mToast;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

		this.nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// FIXME
		Button button = (Button) findViewById(R.id.btn_search);

		button.setOnClickListener(this.mOneShotListener);

	}

	private void showNotification(int statusBarIconID, int statusBarTextID,
			int detailedTextID, boolean showIconOnly) {

		Intent contentIntent = new Intent(this, GenerateAlarm.class);
		PendingIntent theappIntent = PendingIntent.getBroadcast(
				GenerateAlarm.this, 0, contentIntent, 0);

		CharSequence from = "Alarm Manager";
		CharSequence message = "The Alarm was fired";

		String tickerText = showIconOnly ? null : this
				.getString(statusBarTextID);

		Notification notif = new Notification(statusBarIconID, tickerText,
				System.currentTimeMillis());

		notif.setLatestEventInfo(this, from, message, theappIntent);

		this.nm.notify(this.YOURAPP_NOTIFICATION_ID, notif);
	}

	private OnClickListener mOneShotListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intent = new Intent(GenerateAlarm.this, AlarmReceiver.class);

			PendingIntent appIntent = PendingIntent.getBroadcast(
					GenerateAlarm.this, 0, intent, 0);

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.SECOND, 30);

			AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

			am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
					appIntent);

			showNotification(R.drawable.home, R.string.ac_name,
					R.string.app_name, false);

		}

	};

}
