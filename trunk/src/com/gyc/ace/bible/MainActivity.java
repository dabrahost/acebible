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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

//import com.google.tts.TTS;
//import com.google.tts.TextToSpeechBeta;

import com.gyc.ace.bible.alarm.AlarmReceiver;
import com.gyc.lit.LitUtils;

public class MainActivity extends Activity {

	// private TTS myTts;

	private static final String PREF_PAGE_CHAPTERS_NUM = "pageChaptersNum";

	private static final String PREF_SHOW_BTN_PRE_NEXT = "showBtnPreNext";

	private static final String PREF_TRADITIONAL = "traditional";

	private static final String PREF_AUTO_START_TOMORROW = "autoStartTomorrow";

	private static final String PREF_AUTO_START_MINUTE = "auto_start_minute";

	private static final String PREF_AUTO_START_HOUR = "auto_start_hour";

	private static final String PREF_CHAPTER_INDEX = "chapterIndex";

	private static final String PREF_LIST_ITEM_TEXT_SIZE = "listTextSize";

	private static final String PREF_KEY = "key";

	public static final String encoding = "gb2312";

	private static final int MENU_ID_SELECT_OLD = 0;

	private static final int MENU_ID_SELECT_NEW = 1;

	private static final int MENU_ID_SELECT_GOTO = 2;

	private static final int MENU_ID_SELECT_QUERY = 3;

	private static final int MENU_ID_SELECT_HELP = 4;

	private static final int MENU_ID_SELECT_PREF = 5;

	private static final int CCM_BookMark_ADD = 100;

	private static final int CCM_SMS = 101;
	private static final int CCM_CONTENT_COPY = 102;

	private static final int CCM_CONTENT_COPY_ALL = 103;

	private static final int MENU_ID_MORE = 6;

	private static final int MENU_ID_SELECT_BOOKMARK = 7;

	private static final int MENU_ID_ADD_SHOW_BUTTON = 8;

	private static final int MENU_ID_AUTO_STRAT = 9;

	private static final int MENU_ID_SET_AUTO_START_TIME = 10;

	private static final int CCM_EMAIL = 104;

	private static final int CCM_SMS_ALL = 105;

	public static final String PREFS_NAME = "com.gyc.ace.bible";

	private static final int MENU_ID_SELECT_ENCODING = 11;

	private static final int MENU_ID_FONT_SIZE = 15;

	private static final int MENU_ID_READ_TTS = 16;

	/**
	 * 模拟器上不好用 真机可以
	 * 
	 * @param ctx
	 * @return
	 */
	private static boolean isNetworkAvailable(Context ctx) {
		connectivityManager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = connectivityManager.getActiveNetworkInfo();

		return (info != null && info.isConnected());
	}

	private String[] old_volumes = { "创世纪", "出埃及记", "利未记", "民数记", "申命记",
			"约书亚记", "士师记", "路得记", "撒母耳记上 ", "撒母耳记下", "列王记上", "列王记下", "历代志上",
			"历代志下", "以斯拉记", "尼希米记 ", "以斯帖记", "约伯记", "诗篇", "箴言", "传道书", "雅歌",
			"以赛亚书", "耶利米书 ", "耶利米哀歌", "以西结书", "但以理书", "何西阿书", "约珥书", "阿摩司书",
			"俄巴底亚书", "约拿书", "弥迦书", "那鸿书", "哈巴谷书", "西番雅书", "哈该书", "撒加利亚书",
			"玛拉基书" };

	private int[] old_volumes_chapterCnt = { 50, 40, 27, 36, 34, 24, 21, 4, 31,
			24, 22, 25, 29, 36, 10, 13, 10, 42, 150, 31, 12, 8, 66, 52, 5, 48,
			12, 14, 3, 9, 1, 4, 7, 3, 3, 3, 2, 14, 4 };

	private String[] new_volumes = { "马太福音", "马可福音", "路加福音", "约翰福音", "使徒行传",
			"罗马书", "哥林多前书", "哥林多后书", "加拉太书", "以弗所书", "腓立比书 ", "哥罗西书",
			"帖撒罗尼迦前书", "帖撒罗尼迦后书", "提摩太前书", "提摩太后书", "提多书", "腓利门书 ", "希伯来书",
			"雅各布书", "彼得前书", "彼得后书", "约翰一书", "约翰二书", "约翰三书", "犹大书", "启示录" };

	private int[] new_volumes_chapterCnt = { 28, 16, 24, 21, 28, 16, 16, 13, 6,
			6, 4, 4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5, 1, 1, 1, 22 };

	List<CharSequence> ot_list;
	List<CharSequence> ot_list_traditional;
	List<CharSequence> nt_list;
	List<CharSequence> nt_list_traditional;

	private int pageChaptersNum = 5;

	int listTextSize = 20;

	private int chapterIndex = -1;

	String[] xs;

	protected String key = "01";

	private static ConnectivityManager connectivityManager;

	private Button btnPre;

	private Button btnNext;

	private boolean showBtnPreNext = false;

	private boolean autoStartTomorrow = true;

	private int auto_start_hour = 21;

	private int auto_start_minute = 0;

	boolean traditional = false;

	private Menu menu;

	private ContextMenu contextMenu;

	private void bindChapterlistview() {

		// android.R.attr.textAppearance

		if (this.traditional) {
			this.setTitle(LitUtils.toTraditional(xs[0]));
		} else {
			this.setTitle(xs[0]);
		}

		ListView listview = (ListView) findViewById(R.id.listcontents);
		listview.setAdapter(new BaseAdapterNull(MainActivity.this));

		final Adapter4MainList adapter = new Adapter4MainList(MainActivity.this);

		adapter.setContentList(fillListContent());

		adapter.setTextsize(this.listTextSize);

		listview.setAdapter(adapter);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listview.setSelection(0);
		listview.requestFocus();

		/*
		 * listview.setOnScrollListener(new OnScrollListener() {
		 * 
		 * @Override public void onScroll(AbsListView view, int
		 * firstVisibleItem, int visibleItemCount, int totalItemCount) { }
		 * 
		 * @Override public void onScrollStateChanged(AbsListView view, int
		 * scrollState) { // TODO Auto-generated method stub
		 * 
		 * } });
		 */

		this.registerForContextMenu(listview);

	}

	private void bindChapterlistviewOrEmpty() {

		if (xs != null && xs.length > 0) {

			bindChapterlistview();

		} else {

			if (btnPre != null) {
				btnPre.setVisibility(View.GONE);
				btnNext.setVisibility(View.GONE);
			}

			List<CharSequence> listcontent = new ArrayList<CharSequence>();

			String ac = this.getResources().getString(R.string.ac_name);

			String[] xx = ac.split("\\s");
			for (int i = 0; i < xx.length; i++) {
				listcontent.add(" ");
				if (this.traditional) {
					listcontent.add(LitUtils.toTraditional(xx[i]));
				} else {
					listcontent.add(xx[i]);
				}
			}

			ListView listview = (ListView) findViewById(R.id.listcontents);
			listview.setAdapter(new BaseAdapterNull(this));

			final BaseAdapterNew baseAdapterNew = new BaseAdapterNew(this);
			baseAdapterNew.setContentList(listcontent);
			listview.setAdapter(baseAdapterNew);
			listview.setSelection(0);
			listview.requestFocus();
			listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			if (this.traditional) {
				this.setTitle(LitUtils.toTraditional("圣经"));
			} else {
				this.setTitle("圣经");
			}

		}
	}

	private List<CharSequence> fillListContent() {

		List<CharSequence> listcontent = new ArrayList<CharSequence>();

		int ii = chapterIndex;

		for (; ii < xs.length; ii++) {
			if (ii >= chapterIndex + getPageChaptersNum()) {
				break;
			} else {
				listcontent.add(xs[ii]);
			}
		}
		return listcontent;

	}

	private int getPageChaptersNum() {
		return pageChaptersNum;
	}

	private void handleKeyTyped(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			Button btnPre = (Button) this.findViewById(R.id.btnpre);
			if (btnPre != null) {
				btnPre.performClick();
			}

			// this.showOldChapters();
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			Button btnNext = (Button) this.findViewById(R.id.btnnext);
			if (btnNext != null) {
				btnNext.performClick();
			}
			// this.showNewChapters();
		}
	}

	private void handleSelectedKey(final String key) {
		setViewMain();

		// System.out.println("key" + key);

		MainActivity.this.key = key;

		StringBuilder linesb = new StringBuilder();

		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			is = getAssets().open(key + ".txt");
			isr = new InputStreamReader(is, MainActivity.encoding);
			br = new BufferedReader(isr, 10240);
			String line = "";

			chapterIndex = 0;

			while ((line = br.readLine()) != null) {
				linesb.append(line);
			}

			line = "";

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				// e1.printStackTrace();
			}

			try {
				isr.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				// e1.printStackTrace();
			}

			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}

		if (linesb.length() > 2) {
			final String content = linesb.toString();
			linesb.setLength(0);

			xs = content.split("\\s\\s");
			bindChapterlistview();
		}
	}

	/**
	 * @return the showBtnPreNext
	 */
	public boolean isShowBtnPreNext() {
		return showBtnPreNext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// System.out.println("onActivityResult");

		if (data != null) {
			if (data.getExtras() != null) {
				Bundle bundle = data.getExtras();
				// bundle.putInt("key", Integer.parseInt(key));
				// bundle.putInt("chapter", chapter);
				// bundle.putInt("subchapter", subchapter);
				String rtkey = bundle.getString(PREF_KEY);
				int chapter = bundle.getInt("chapter");
				int subchapter = bundle.getInt("subchapter");

				// System.out.println("rtkey:" + rtkey);
				// System.out.println("chapter:" + chapter);
				// System.out.println("subchapter:" + subchapter);

				if (rtkey != null) {
					if (rtkey.equalsIgnoreCase(this.key) && this.xs != null) {
						this.setChapterIndex(chapter + "");
						this.setChapterSubIndex(chapter, subchapter);

						this.setViewMain();
						// this.bindChapterlistview();

					} else {
						this.key = rtkey;

						this.handleSelectedKey(key);
						this.setChapterIndex(chapter + "");
						this.setChapterSubIndex(chapter, subchapter);
						this.bindChapterlistview();

					}

				}
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreatePanelMenu(int, android.view.Menu)
	 */
	@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreatePanelMenu(featureId, menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreatePanelView(int)
	 */
	@Override
	public View onCreatePanelView(int featureId) {
		// TODO Auto-generated method stub
		return super.onCreatePanelView(featureId);
	}

	/*
	 * 
	 * 
	 * 如果没有在android-manifest中定义需要捕获的事件 本函数不会被调用
	 * 
	 * TODO 查看文档
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.app.Activity#onConfigurationChanged(android.content.res.Configuration
	 * )
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// orientation = newConfig.orientation;

		// System.out.println(orientation);

		super.onConfigurationChanged(newConfig);

		// setViewMain();

		// bindChapterlistviewOrEmpty();

	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		TextView textview = (TextView) info.targetView;

		String text = textview.getText().toString();

		switch (item.getItemId()) {
		case CCM_EMAIL:
			final Intent emailIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { "webmaster@website.com" });

			int splitIndex = text.indexOf(" ");

			final String chapter = text.substring(0, splitIndex).trim();
			final String chaptercontent = text.substring(splitIndex).trim();

			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, xs[0]
					+ chapter);

			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					chaptercontent);

			emailIntent.setType("message/rfc882");

			this.startActivityForResult(Intent.createChooser(emailIntent,
					"发送邮件"), 1);

			break;
		case CCM_CONTENT_COPY:
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(xs[0] + text);
			break;

		case CCM_CONTENT_COPY_ALL:
			clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

			StringBuilder sb = new StringBuilder();
			int ii = chapterIndex;
			for (; ii < xs.length; ii++) {
				if (ii >= chapterIndex + getPageChaptersNum()) {
					break;
				} else {
					sb.append(xs[ii]);
				}
			}

			clipboard.setText((chapterIndex == 0 ? "" : xs[0]) + sb.toString());
			sb.setLength(0);

			break;

		case CCM_BookMark_ADD:
			try {

				if (text.indexOf(" ") < 0) {
					// FIXME no bookmark here TOAST
					if (this.traditional) {
						Toast.makeText(this,
								LitUtils.toTraditional("在别处+书签吧!"),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, "在别处+书签吧!", Toast.LENGTH_SHORT)
								.show();
					}
					return false;
				}

				Intent it = new Intent(this, MarkActivity.class);
				it.putExtra(MarkActivity.PR_TEXT, text);
				it.putExtra(MarkActivity.PR_VOLUME, getTitle());
				it.putExtra(MarkActivity.PR_VOLUME_KEY, MainActivity.this.key);

				startActivityForResult(it, 1);

			} catch (Exception e) {
				// TODO: handle exception
			}

			return true;
		case CCM_SMS:
			try {

				Intent it = new Intent(Intent.ACTION_VIEW);
				it.putExtra("sms_body", xs[0] + text);
				it.setType("vnd.android-dir/mms-sms");
				startActivityForResult(it, 1);
			} catch (Exception e) {
				// TODO: handle exception
			}

			return true;
		case CCM_SMS_ALL:
			try {
				sb = new StringBuilder();
				ii = chapterIndex;
				for (; ii < xs.length; ii++) {
					if (ii >= chapterIndex + getPageChaptersNum()) {
						break;
					} else {
						sb.append(xs[ii]);
					}
				}
				Intent it = new Intent(Intent.ACTION_VIEW);
				it.putExtra("sms_body", (chapterIndex == 0 ? "" : xs[0])
						+ sb.toString());
				it.setType("vnd.android-dir/mms-sms");
				startActivityForResult(it, 1);
			} catch (Exception e) {
				// TODO: handle exception
			}

			return true;

		default:
			return super.onContextItemSelected(item);
		}

		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		final Resources resources = getResources();

		// final Configuration configuration = resources.getConfiguration();

		// orientation = configuration.orientation;

		if (savedInstanceState != null) {
			xs = savedInstanceState.getStringArray("xs");
			this.key = savedInstanceState.getString(PREF_KEY);
			chapterIndex = savedInstanceState.getInt(PREF_CHAPTER_INDEX, -1);
			showBtnPreNext = savedInstanceState.getBoolean(
					PREF_SHOW_BTN_PRE_NEXT, false);
			traditional = savedInstanceState
					.getBoolean(PREF_TRADITIONAL, false);

			this.setPageChaptersNum(savedInstanceState.getInt(
					PREF_PAGE_CHAPTERS_NUM, 5));

			setViewMain();
			// bindChapterlistviewOrEmpty();

		} else {

			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

			this.key = settings.getString(PREF_KEY, "01");
			this.chapterIndex = settings.getInt(PREF_CHAPTER_INDEX, 0);
			this.auto_start_hour = settings.getInt(PREF_AUTO_START_HOUR, 21);
			this.auto_start_minute = settings.getInt(PREF_AUTO_START_MINUTE, 0);

			this.autoStartTomorrow = settings.getBoolean(
					PREF_AUTO_START_TOMORROW, true);

			this.setPageChaptersNum(settings.getInt(PREF_PAGE_CHAPTERS_NUM, 5));

			this.showBtnPreNext = settings.getBoolean(PREF_SHOW_BTN_PRE_NEXT,
					false);

			if (settings.getAll().size() > 0) {
				this.traditional = settings.getBoolean(PREF_TRADITIONAL, false);
			} else {

				this.traditional = false;
				/*
				 * Locale locale = getResources().getConfiguration().locale;
				 * String localetostr = locale.toString().toLowerCase();
				 * 
				 * if (localetostr.indexOf("zh") > -1 &&
				 * localetostr.indexOf("cn") > -1) { this.traditional = false; }
				 * else { this.traditional = true; }
				 */
			}

			setViewMain();

			BufferedReader br = null;
			InputStreamReader isr = null;

			if (this.key != null) {
				StringBuilder linesb = new StringBuilder();

				InputStream is = null;
				try {
					is = getAssets().open(key + ".txt");
					isr = new InputStreamReader(is, MainActivity.encoding);
					br = new BufferedReader(isr, 10240);
					String line = "";

					while ((line = br.readLine()) != null) {
						linesb.append(line);
					}
					line = "";

				} catch (Exception e) {
					// e.printStackTrace();
				} finally {
					try {
						br.close();
					} catch (Exception e1) {
					}

					try {
						isr.close();
					} catch (Exception e1) {
					}

					try {
						is.close();
					} catch (Exception e) {
					}
				}

				if (linesb.length() > 2) {
					final String content = linesb.toString();
					linesb.setLength(0);
					// String[] xs = content.split("\\s(?>\\d+:\\d)");
					xs = content.split("\\s\\s");

					linesb = null;

					bindChapterlistview();

				}

			}

		}

		ot_list = new ArrayList<CharSequence>();
		ot_list.addAll(Arrays.asList(old_volumes));

		ot_list_traditional = new ArrayList<CharSequence>();
		ot_list_traditional.addAll(Arrays.asList(old_volumes));
		for (int i = 0; i < ot_list_traditional.size(); i++) {
			ot_list_traditional.set(i, LitUtils
					.toTraditional(ot_list_traditional.get(i).toString()));
		}

		nt_list = new ArrayList<CharSequence>();
		nt_list.addAll(Arrays.asList(new_volumes));

		nt_list_traditional = new ArrayList<CharSequence>();
		nt_list_traditional.addAll(Arrays.asList(new_volumes));
		for (int i = 0; i < nt_list_traditional.size(); i++) {
			nt_list_traditional.set(i, LitUtils
					.toTraditional(nt_list_traditional.get(i).toString()));
		}
		// XXX 嘿嘿 动画 背景 图片

		Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);

		PendingIntent appIntent = PendingIntent.getBroadcast(MainActivity.this,
				0, intent, 0);

		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		am.cancel(appIntent);

		// myTts = new TTS(this, ttsInitListener, true);

	}

	/*
	 * private TTS.InitListener ttsInitListener = new TTS.InitListener() {
	 * public void onInit(int version) { myTts.setLanguage("zh");
	 * myTts.speak("你好", 0, null); } };
	 */

	public void onCreateContextMenu(ContextMenu contextMenu, View v,
			ContextMenuInfo menuInfo) {

		// System.out.println("onCreateContextMenu  onCreateContextMenu");

		super.onCreateContextMenu(contextMenu, v, menuInfo);

		this.contextMenu = contextMenu;

		updateContextMenu4Traditinal(contextMenu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		this.menu = menu;

		updateMenuForTraditional(menu);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {

		// myTts.shutdown();

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();

		editor.putString(PREF_KEY, key);
		editor.putInt(PREF_CHAPTER_INDEX, chapterIndex);
		editor.putInt(PREF_AUTO_START_HOUR, auto_start_hour);
		editor.putInt(PREF_AUTO_START_MINUTE, auto_start_minute);
		editor.putBoolean(PREF_AUTO_START_TOMORROW, autoStartTomorrow);
		editor.putBoolean(PREF_TRADITIONAL, traditional);
		editor.putBoolean(PREF_SHOW_BTN_PRE_NEXT, showBtnPreNext);
		editor.putInt(PREF_PAGE_CHAPTERS_NUM, pageChaptersNum);

		editor.commit();

		if (autoStartTomorrow) {
			try {

				Intent intent = new Intent(MainActivity.this,
						AlarmReceiver.class);
				PendingIntent appIntent = PendingIntent.getBroadcast(
						MainActivity.this, 0, intent, 0);
				AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				am.cancel(appIntent);

				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());

				calendar.set(Calendar.HOUR_OF_DAY, auto_start_hour);
				calendar.set(Calendar.MINUTE, auto_start_minute);
				calendar.set(Calendar.SECOND, 0);

				calendar.add(Calendar.DAY_OF_YEAR, 1);

				// calendar.add(Calendar.SECOND, 10);

				am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
						appIntent);

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		super.onDestroy();

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		this.handleKeyTyped(keyCode, event);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onLowMemory()
	 */
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == MENU_ID_READ_TTS) {
			// HashMap<String, String> myHashAlarm = new HashMap();
			// myHashAlarm.put(TextToSpeechBeta.Engine.KEY_PARAM_STREAM,
			// String.valueOf(AudioManager.STREAM_MUSIC));

			int ii = chapterIndex;
			for (; ii < xs.length; ii++) {
				if (ii >= chapterIndex + getPageChaptersNum()) {
					break;
				} else {
					// myTts.speak(xs[ii], TextToSpeechBeta.QUEUE_ADD, null);
				}
			}

		} else if (id == MENU_ID_FONT_SIZE) {

			FontSizeDialog fsd = new FontSizeDialog(this);
			fsd.show();

		} else if (id == MENU_ID_SET_AUTO_START_TIME) {

			OnTimeSetListener onTimeSetListener = new OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					auto_start_hour = hourOfDay;
					auto_start_minute = minute;
				}
			};

			TimePickerDialog tpd = new TimePickerDialog(this,
					onTimeSetListener, auto_start_hour, auto_start_minute, true);

			tpd.show();

		} else if (id == MENU_ID_AUTO_STRAT) {

			this.autoStartTomorrow = !this.autoStartTomorrow;

			this.updateMenuForTraditional(menu);

		} else if (id == MENU_ID_ADD_SHOW_BUTTON) {
			this.setShowBtnPreNext(!this.isShowBtnPreNext());

			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(PREF_SHOW_BTN_PRE_NEXT, showBtnPreNext);
			editor.commit();

			this.updateMenuForTraditional(menu);

			setViewMain();
			// bindChapterlistviewOrEmpty();

		} else if (id == MENU_ID_SELECT_BOOKMARK) {
			Intent it = new Intent(this, MarkActivity.class);
			startActivityForResult(it, 1);
		} else if (id == MENU_ID_SELECT_ENCODING) {
			this.traditional = !traditional;

			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(PREF_TRADITIONAL, traditional);
			editor.commit();

			updateMenuForTraditional(menu);
			this.setViewMain();
			// bindChapterlistviewOrEmpty();

		} else {
			String strReturn = "返回";

			if (traditional) {
				strReturn = LitUtils.toTraditional(strReturn);
			}
			if (id == MENU_ID_SELECT_PREF) {

				if (!(xs != null && xs.length > 0)) {
					return false;
				}
				final AlertDialog.Builder builder = new AlertDialog.Builder(
						this);
				final String[] nums = { "5", "8", "10", "12" };

				int pcn = this.getPageChaptersNum();
				int target = 0;
				switch (pcn) {
				case 5:
					target = 0;
					break;
				case 8:
					target = 1;
					break;
				case 10:
					target = 2;
					break;
				case 12:
					target = 3;
					break;
				default:
					break;
				}

				DialogInterface.OnClickListener onClickListenerVersesPage = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String x = nums[which];
						int xx = Integer.parseInt(x);
						switch (xx) {
						case 5:
						case 8:
						case 10:
						case 12:

							MainActivity.this.setPageChaptersNum(xx);

							SharedPreferences settings = getSharedPreferences(
									PREFS_NAME, 0);
							SharedPreferences.Editor editor = settings.edit();
							editor.putInt(PREF_PAGE_CHAPTERS_NUM,
									pageChaptersNum);
							editor.commit();

							setViewMain();

							break;
						default:
							break;
						}
					}
				};

				String linesPerPage = "每页行数";
				if (traditional) {
					linesPerPage = LitUtils.toTraditional(linesPerPage);
				}
				builder.setTitle(linesPerPage).setSingleChoiceItems(nums,
						target, onClickListenerVersesPage).setCancelable(true)
						.setNeutralButton(strReturn, null).show();

			} else if (id == MENU_ID_SELECT_HELP) {
				Intent intent = new Intent(this, HelpActivity.class);
				this.startActivity(intent);

			} else if (id == MENU_ID_SELECT_QUERY) {
				Intent intent = new Intent(this, SearchActivity.class);

				this.startActivityForResult(intent, 1);

			} else if (id == MENU_ID_SELECT_OLD) {
				this.setContentView(R.layout.volumelist);

				Button btnRtnFromVolume = (Button) findViewById(R.id.btnRtnFromVolume);
				btnRtnFromVolume.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// MainActivity.this.setContentView(R.layout.main);
						setViewMain();
					}

				});

				if (!this.traditional) {
					showNtOtList((ListView) findViewById(R.id.chapters),
							this.ot_list);

				} else {
					showNtOtList((ListView) findViewById(R.id.chapters),
							this.ot_list_traditional);

				}

			} else if (id == MENU_ID_SELECT_NEW) {
				this.setContentView(R.layout.volumelist);

				Button btnRtnFromVolume = (Button) findViewById(R.id.btnRtnFromVolume);
				btnRtnFromVolume.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// MainActivity.this.setContentView(R.layout.main);
						setViewMain();
					}

				});

				if (!this.traditional) {

					showNtOtList((ListView) findViewById(R.id.chapters),
							this.nt_list);

				} else {
					showNtOtList((ListView) findViewById(R.id.chapters),
							this.nt_list_traditional);

				}

			} else if (id == MENU_ID_SELECT_GOTO) {

				if (!(xs != null && xs.length > 0)) {
					return true;
				}

				String xx = MainActivity.this.key;

				int xxint = Integer.parseInt(xx);

				// System.out.println(xxint);

				String stringGotoChapter = "转到 ";
				if (traditional) {
					stringGotoChapter = LitUtils
							.toTraditional(stringGotoChapter);
				}

				if (xxint < 40) {
					// ot
					int ntindex = this.old_volumes_chapterCnt[xxint - 1];
					List<String> chapterList = new ArrayList<String>();
					for (int i = 1; i <= ntindex; i++) {
						chapterList.add("" + i);
					}
					final String[] ksa = new String[chapterList.size()];
					chapterList.toArray(ksa);

					final AlertDialog.Builder builder = new AlertDialog.Builder(
							this);

					builder.setItems(ksa,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String chapter = ksa[which];
									// System.out.println("keyx:" + chapter);

									setChapterIndex(chapter);

									setViewMain();

									// bindChapterlistviewOrEmpty();

								}
							}).setTitle(stringGotoChapter + xs[0])
							.setNeutralButton(strReturn, null).show();

				} else {
					// nt
					int ntindex = this.new_volumes_chapterCnt[xxint - 40];
					List<String> chapterList = new ArrayList<String>();

					for (int i = 1; i <= ntindex; i++) {
						chapterList.add("" + i);
					}

					final String[] ksa = new String[chapterList.size()];
					chapterList.toArray(ksa);

					final AlertDialog.Builder builder = new AlertDialog.Builder(
							this);

					builder.setItems(ksa,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String chapter = ksa[which];
									// System.out.println("keyx:" + chapter);

									setChapterIndex(chapter);

									setViewMain();

									// bindChapterlistviewOrEmpty();

								}
							}).setTitle(stringGotoChapter + xs[0])
							.setNeutralButton(strReturn, null).show();

				}

			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle bundle) {

		bundle.putStringArray("xs", xs);
		bundle.putString(PREF_KEY, this.key);
		bundle.putInt(PREF_CHAPTER_INDEX, chapterIndex);
		bundle.putBoolean(PREF_SHOW_BTN_PRE_NEXT, showBtnPreNext);
		bundle.putBoolean(PREF_TRADITIONAL, traditional);
		bundle.putInt(PREF_PAGE_CHAPTERS_NUM, pageChaptersNum);

		// chapterIndex

		super.onSaveInstanceState(bundle);
	}

	private void setChapterIndex(String chapter) {
		// System.out.println("chapter$" + chapter + "$");
		for (int i = 1; i < xs.length; i++) {
			if (xs[i].startsWith(chapter)) {
				// System.out.println("$" + xs[i] + "$");
				chapterIndex = i;
				break;
			}
		}
	}

	private void setChapterSubIndex(int chapter, int subchapter) {
		// System.out.println("subchapter$" + subchapter + "$");
		for (int i = chapterIndex; i < xs.length; i++) {
			if (xs[i].startsWith(chapter + ":" + subchapter)) {
				// System.out.println("$" + xs[i] + "$");
				chapterIndex = i;
				break;
			}
		}
	}

	private void setPageChaptersNum(int npcn) {
		switch (npcn) {
		case 5:
			this.listTextSize = 20;
			break;
		case 8:
			this.listTextSize = 18;
			break;
		case 10:
			this.listTextSize = 15;
			break;
		case 12:
			this.listTextSize = 12;
			break;
		default:
			return;
		}
		pageChaptersNum = npcn;
	}

	/**
	 * @param showBtnPreNext
	 *            the showBtnPreNext to set
	 */
	public void setShowBtnPreNext(boolean showBtnPreNext) {
		this.showBtnPreNext = showBtnPreNext;
	}

	void setViewMain() {
		// setContentView(R.layout.help);
		setContentView(R.layout.main);

		bindChapterlistviewOrEmpty();

		updatePreNextButtons();

		OnClickListener prelstn = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (xs != null) {

					chapterIndex -= getPageChaptersNum();
					if (chapterIndex < 0) {
						chapterIndex = 0;
					}

					bindChapterlistview();

				}

			}
		};
		btnPre.setOnClickListener(prelstn);

		OnClickListener nextlstn = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (xs != null) {

					chapterIndex += getPageChaptersNum();
					if (chapterIndex >= xs.length) {
						chapterIndex -= getPageChaptersNum();
					}

					bindChapterlistview();

				}
			}
		};

		btnNext.setOnClickListener(nextlstn);
	}

	private void showNtOtList(final ListView listview,
			final List<CharSequence> list) {

		listview.setAdapter(new BaseAdapterNull(this));

		final BaseAdapterNew baseAdapterNew = new BaseAdapterNew(this);
		baseAdapterNew.setContentList(list);
		baseAdapterNew.setResourceId(R.layout.select_text);
		listview.setAdapter(baseAdapterNew);
		listview.setSelection(0);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		OnItemClickListener itemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {

				if (list.size() == nt_list.size()) {
					final String key = "" + (position + 40);
					MainActivity.this.key = key;
					handleSelectedKey(key);

				} else if (list.size() == ot_list.size()) {
					int x = position + 1;
					final String key = x > 9 ? "" + x : "0" + x;
					MainActivity.this.key = key;
					handleSelectedKey(key);

				} else {
					setViewMain();
					// bindChapterlistviewOrEmpty();
				}
			}
		};

		listview.setOnItemClickListener(itemClickListener);

	}

	private void updateContextMenu4Traditinal(Menu contextMenu) {
		if (this.contextMenu != null) {
			contextMenu.clear();
			if (this.traditional) {
				MenuItem x = contextMenu.add(0, CCM_BookMark_ADD, 0, LitUtils
						.toTraditional("加为书签"));
				contextMenu
						.add(0, CCM_SMS, 0, LitUtils.toTraditional("短信发送本节"));
				contextMenu.add(0, CCM_SMS_ALL, 0, LitUtils
						.toTraditional("短信发送本页"));

				// menu.add(0, CCM_EMAIL, 0,
				// LitUtils.toTraditional("使用邮件发送本节"));

				contextMenu.add(0, CCM_CONTENT_COPY, 0, LitUtils
						.toTraditional("复制本节"));
				contextMenu.add(0, CCM_CONTENT_COPY_ALL, 0, LitUtils
						.toTraditional("复制本页"));
			} else {
				MenuItem x = contextMenu.add(0, CCM_BookMark_ADD, 0, "加为书签");
				contextMenu.add(0, CCM_SMS, 0, "短信发送本节");
				contextMenu.add(0, CCM_SMS_ALL, 0, "短信发送本页");
				// menu.add(0, CCM_EMAIL, 0, "使用邮件发送本节");
				contextMenu.add(0, CCM_CONTENT_COPY, 0, "复制本节");
				contextMenu.add(0, CCM_CONTENT_COPY_ALL, 0, "复制本页");
			}
		}
	}

	private void updateMenuForTraditional(Menu menu) {
		menu.clear();

		if (this.traditional) {

			menu.add(0, MENU_ID_SELECT_OLD, 0, "舊約").setIcon(R.drawable.ot)
					.setAlphabeticShortcut('O');

			menu.add(1, MENU_ID_SELECT_GOTO, 0, "章")
					.setIcon(R.drawable.chapter).setAlphabeticShortcut('9');

			menu.add(2, MENU_ID_SELECT_NEW, 0, "新約").setIcon(R.drawable.nt)
					.setAlphabeticShortcut('N');

			menu.add(0, MENU_ID_SELECT_BOOKMARK, 1, "書籤").setIcon(
					R.drawable.bookmark).setAlphabeticShortcut('B');

			menu.add(1, MENU_ID_SELECT_QUERY, 1, "查詢").setIcon(
					R.drawable.search).setAlphabeticShortcut('S');

			SubMenu more = menu.addSubMenu(2, MENU_ID_MORE, 1, "設置/幫助")
					.setIcon(android.R.drawable.ic_menu_more);

			more.add(0, MENU_ID_SELECT_PREF, 0, "每頁行數");

			more.add(0, MENU_ID_FONT_SIZE, 0, "字體大小");

			if (this.showBtnPreNext) {
				more.add(0, MENU_ID_ADD_SHOW_BUTTON, 1, "隱藏導航按鈕");
			} else {
				more.add(0, MENU_ID_ADD_SHOW_BUTTON, 1, "顯示導航按鈕");
			}

			more.add(0, MENU_ID_SELECT_ENCODING, 2, "使用繁體中文瀏覽");

			if (this.autoStartTomorrow) {
				more.add(0, MENU_ID_AUTO_STRAT, 3, "取消自動啟動");
			} else {
				more.add(0, MENU_ID_AUTO_STRAT, 3, "恢復自動啟動");
			}
			more.add(0, MENU_ID_SET_AUTO_START_TIME, 4, "設定每日啟動時間");

			// more.add(0, MENU_ID_READ_TTS, 5, "閱讀");

			more.add(0, MENU_ID_SELECT_HELP, 6, "帮助");

		} else {

			menu.add(0, MENU_ID_SELECT_OLD, 0, "旧约").setIcon(R.drawable.ot)
					.setAlphabeticShortcut('O'); // .setIcon(android.R.drawable.o)

			menu.add(1, MENU_ID_SELECT_GOTO, 0, "章")
					.setIcon(R.drawable.chapter).setAlphabeticShortcut('9');

			menu.add(2, MENU_ID_SELECT_NEW, 0, "新约").setIcon(R.drawable.nt)
					.setAlphabeticShortcut('N');

			menu.add(0, MENU_ID_SELECT_BOOKMARK, 1, "书签").setIcon(
					R.drawable.bookmark).setAlphabeticShortcut('B');

			menu.add(1, MENU_ID_SELECT_QUERY, 1, "查询").setIcon(
					R.drawable.search).setAlphabeticShortcut('S');

			SubMenu more = menu.addSubMenu(2, MENU_ID_MORE, 1, "设置/帮助")
					.setIcon(android.R.drawable.ic_menu_more);

			more.add(0, MENU_ID_SELECT_PREF, 0, "每页行数");

			more.add(0, MENU_ID_FONT_SIZE, 0, "字体大小");

			if (this.showBtnPreNext) {
				more.add(0, MENU_ID_ADD_SHOW_BUTTON, 1, "隐藏导航按钮");
			} else {
				more.add(0, MENU_ID_ADD_SHOW_BUTTON, 1, "显示导航按钮");
			}

			more.add(0, MENU_ID_SELECT_ENCODING, 2, "使用繁体中文浏览");

			if (this.autoStartTomorrow) {
				more.add(0, MENU_ID_AUTO_STRAT, 3, "取消自动启动");
			} else {
				more.add(0, MENU_ID_AUTO_STRAT, 3, "恢复自动启动");
			}

			more.add(0, MENU_ID_SET_AUTO_START_TIME, 4, "设定每日启动时间");

			// more.add(0, MENU_ID_READ_TTS, 5, "阅读");

			more.add(0, MENU_ID_SELECT_HELP, 6, "帮助");

		}

		updateContextMenu4Traditinal(this.contextMenu);
	}

	private void updatePreNextButtons() {
		btnPre = (Button) this.findViewById(R.id.btnpre);
		btnNext = (Button) this.findViewById(R.id.btnnext);
		// ListView lv = (ListView) this.findViewById(R.id.listcontents);

		Display display = this.getWindowManager().getDefaultDisplay();

		if (this.showBtnPreNext) {

			btnPre.setVisibility(View.VISIBLE);
			btnNext.setVisibility(View.VISIBLE);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					display.getWidth() / 2, 50);
			params.setMargins(2, 2, 2, 2);

			btnPre.setLayoutParams(params);
			btnNext.setLayoutParams(params);

			// lv.setLayoutParams(params);

			final Drawable drawable_pre = this.getResources().getDrawable(
					R.drawable.go_previous);

			drawable_pre.setBounds(btnPre.getLeft() + 2, btnPre.getTop() + 2,
					btnPre.getLeft() + 34, btnPre.getTop() + 34);

			btnPre.setCompoundDrawables(drawable_pre, null, null, null);

			final Drawable drawable_next = this.getResources().getDrawable(
					R.drawable.go_next);

			drawable_next.setBounds(btnNext.getLeft() + btnNext.getWidth() / 2,
					btnNext.getTop() + 2, btnNext.getLeft()
							+ btnNext.getWidth() / 2 + 34,
					btnNext.getTop() + 34);

			btnNext.setCompoundDrawables(null, null, drawable_next, null);

			if (this.traditional) {
				btnPre.setText("向前");
				btnNext.setText("向後");
			} else {
				btnPre.setText("向前");
				btnNext.setText("向后");
			}

		} else {

			btnPre.setVisibility(View.GONE);
			btnNext.setVisibility(View.GONE);

		}
	}

}