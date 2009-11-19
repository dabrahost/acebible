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
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;

import com.gyc.ace.bible.db.Mark;
import com.gyc.lit.LitUtils;

public class SearchActivity extends Activity {

	private List<Mark> marksList = new ArrayList<Mark>();
	private List<CharSequence> marksString = new ArrayList<CharSequence>();

	public static final int message_start = 1;
	public static final int message_inter = 2;
	public static final int message_end = 9;

	private final int maxnum = 100;

	int search_start = 1;
	int search_end_inclusive = 66;

	private Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == message_start) {
				Log.i("message", "message_start");

				ProgressBar pb = (ProgressBar) SearchActivity.this
						.findViewById(R.id.progress_search);
				pb.setMax(67);
				pb.setProgress(0);

			} else if (msg.what == message_inter) {
				ProgressBar pb = (ProgressBar) SearchActivity.this
						.findViewById(R.id.progress_search);

				pb.setProgress(pb.getProgress() + 1);

			} else if (msg.what == message_end) {
				ProgressBar pb = (ProgressBar) SearchActivity.this
						.findViewById(R.id.progress_search);
				pb.setProgress(66);

				if (marksList.size() > 0) {
					SearchActivity.this.showList();
				} else {
					String noresult = "查询无结果";
					if (traditional) {
						noresult = "查詢無結果";
					}
					Toast.makeText(SearchActivity.this, noresult,
							Toast.LENGTH_LONG).show();

					final Button search = (Button) findViewById(R.id.btn_search);
					search.setEnabled(true);
				}
			}

			super.handleMessage(msg);
		}

	};
	private boolean traditional;

	private void showList() {

		this.setContentView(R.layout.search_list);

		this.setTitle("查询结果");

		marksString.clear();

		for (Iterator<Mark> iterator = marksList.iterator(); iterator.hasNext();) {
			Mark mark = iterator.next();
			marksString.add(mark.toString());
		}

		ListView listview = (ListView) findViewById(R.id.list_search);
		listview.setAdapter(new BaseAdapterNull(this));

		final Adapter4SearchList baseAdapterNew = new Adapter4SearchList(this,
				this.searchTarget, traditional);

		baseAdapterNew.setContentList(marksString);
		baseAdapterNew.setTextsize(16);

		listview.setAdapter(baseAdapterNew);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listview.setSelection(0);
		listview.requestFocus();

		Button btn = (Button) this.findViewById(R.id.btnRtnFromSearch);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchActivity.this.finish();
			}
		});

		this.registerForContextMenu(listview);

	}

	private static final int CCM_LOCATE = 1;
	private static final int CCM_SEND = 3;
	private static final int CCM_EMAIL = 5;
	private static final int CCM_CONTENT_COPY = 7;
	private static final int CCM_CONTENT_COPY_ALL = 9;

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (this.traditional) {
			menu.add(0, CCM_LOCATE, 0, "打開");
			menu.add(0, CCM_SEND, 1, "發短信給朋友");
			menu.add(0, CCM_CONTENT_COPY, 2, "複製本節");
			menu.add(0, CCM_CONTENT_COPY_ALL, 3, "複製本頁");
			menu.add(0, CCM_EMAIL, 4, LitUtils.toTraditional("发邮件发送"));
		} else {
			menu.add(0, CCM_LOCATE, 0, "打开");
			menu.add(0, CCM_SEND, 1, "发短信给朋友");
			menu.add(0, CCM_CONTENT_COPY, 2, "复制本节");
			menu.add(0, CCM_CONTENT_COPY_ALL, 3, "复制本页");
			menu.add(0, CCM_EMAIL, 4, "用邮件发送本节");
		}

	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		// long itemid = info.id;
		long itemposition = info.position;

		// System.out.println(itemid + ":" + itemposition);

		// TextView tv = (TextView) info.targetView;

		final Mark mark = marksList.get((int) itemposition);

		switch (item.getItemId()) {

		case CCM_CONTENT_COPY:
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(mark.toString());
			break;
		case CCM_CONTENT_COPY_ALL:
			ClipboardManager clipboardall = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			StringBuilder sb = new StringBuilder(1024);
			for (Iterator<Mark> iterator = marksList.iterator(); iterator
					.hasNext();) {
				Mark mark2 = iterator.next();
				sb.append(mark2.toString());
				sb.append("\n");
			}
			clipboardall.setText(sb.toString());
			sb.setLength(0);
			break;

		case CCM_LOCATE:
			String volume = mark.getVolume();
			int hindex = volume.indexOf(" ");
			String key = volume.substring(0, hindex).trim();

			int chapter = mark.getChapter();
			int subchapter = mark.getSubchapter();

			Intent mIntent = new Intent();
			Bundle bundle = new Bundle();

			bundle.putString("key", key);
			bundle.putInt("chapter", chapter);
			bundle.putInt("subchapter", subchapter);

			mIntent.putExtras(bundle);

			setResult(RESULT_OK, mIntent);

			finish();
			break;

		case CCM_SEND:

			volume = mark.getVolume();
			hindex = volume.indexOf(" ");

			Intent it = new Intent(Intent.ACTION_VIEW);
			it.putExtra("sms_body", mark.toString());
			it.setType("vnd.android-dir/mms-sms");
			startActivityForResult(it, 1);
			break;

		case CCM_EMAIL:
			// FIXME TODO

			Intent it2 = new Intent(Intent.ACTION_SEND);
			String[] tos = { "to_who@gmail.com" };
			// String[] ccs = { "you@abc.com" };
			it2.putExtra(Intent.EXTRA_EMAIL, tos);
			// it2.putExtra(Intent.EXTRA_CC, ccs);
			it2.putExtra(Intent.EXTRA_TEXT, mark.toString());
			String markstr = mark.getVolume() + " " + mark.getChapter() + "-"
					+ mark.getSubchapter();
			System.out.println(markstr);
			it2.putExtra(Intent.EXTRA_SUBJECT, markstr);
			it2.setType("message/rfc822");
			startActivity(Intent.createChooser(it2, "选择邮件客户端"));

			break;

		default:
			break;

		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences settings = getSharedPreferences(
				MainActivity.PREFS_NAME, 0);

		this.traditional = settings.getBoolean("traditional", false);

		setSearchWordView();

	}

	private String searchTarget;

	private String get18n(String string) {

		if (this.traditional) {
			return LitUtils.toTraditional(string);
		}
		return string;
	}

	private String getSimple(String string) {

		if (this.traditional) {
			return LitUtils.toSimplified(string);
		}
		return string;
	}

	private void setSearchWordView() {
		this.setContentView(R.layout.searchword);

		final Button search = (Button) this.findViewById(R.id.btn_search);
		search.setEnabled(true);
		if (this.traditional) {
			search.setText(LitUtils.toTraditional("查询"));
		}

		Button btnreturn = (Button) this.findViewById(R.id.btn_search_return);
		final EditText et = (EditText) this.findViewById(R.id.search_word);

		final Spinner spinner = (Spinner) this
				.findViewById(R.id.search_spinner);

		List<String> spinnerc = new ArrayList<String>();

		if (!this.traditional) {
			// 0 1-66
			spinnerc.add("选择查询范围-全书");
			// 1 1-39
			spinnerc.add("旧约");
			// 2 40-66
			spinnerc.add("新约");
			// 3 1-5
			spinnerc.add("旧约-律法书(创-申)");
			// 4 6-17
			spinnerc.add("旧约-历史书(约书亚记-以斯帖记)");
			// 5 18-22
			spinnerc.add("旧约-智慧书(约伯记、诗篇、箴言、传道书、雅歌)");
			// 6 23-27
			spinnerc.add("旧约-大先知书(以赛亚书-但以理书)");
			// 7 28-39
			spinnerc.add("旧约-小先知书(何西阿书-玛拉基书)");

			// 8 40-43
			spinnerc.add("新约-福音(马太福音-约翰福音)");
			// 9 44
			spinnerc.add("新约-使徒行传");

			// 10 45-53
			spinnerc.add("新约-保罗书信(罗马书-帖撒罗尼迦后书)");//

			// 11 54-57
			spinnerc.add("新约-保罗书信-教牧(提摩太前书-腓利门书)");//

			// 12 58-65
			spinnerc.add("新约-普通书信(希伯来书-犹大书)");

			// 13 66
			spinnerc.add("新约-启示录");

		} else {
			// 0 1-66
			spinnerc.add("選擇查詢範圍-全書");
			// 1 1-39
			spinnerc.add("舊約");
			// 2 40-66
			spinnerc.add("新約");
			// 3 1-5
			spinnerc.add("舊約-律法書(創-申)");
			// 4 6-17
			spinnerc.add("舊約-歷史書(約書亞記-以斯帖記)");
			// 5 18-22
			spinnerc.add("舊約-智慧書(約伯記、詩篇、箴言、傳道書、雅歌)");
			// 6 23-27
			spinnerc.add("舊約-大先知書(以賽亞書-但以理書)");
			// 7 28-39
			spinnerc.add("舊約-小先知書(何西阿書-瑪拉基書)");

			// 8 40-43
			spinnerc.add("新約-福音(馬太福音-約翰福音)");
			// 9 44
			spinnerc.add("新約-使徒行傳");

			// 10 45-53
			spinnerc.add("新約-保羅書信(羅馬書-帖撒羅尼迦後書)");//

			// 11 54-57
			spinnerc.add("新約-保羅書信-教牧(提摩太前書-腓利門書)");//

			// 12 58-65
			spinnerc.add("新約-普通書信(希伯來書-猶大書)");

			// 13 66
			spinnerc.add("新約-啟示錄");
		}

		ArrayAdapter<String> spinnerAdpater = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinnerc);
		spinnerAdpater
				.setDropDownViewResource(android.R.layout.simple_spinner_item);

		spinner.setAdapter(spinnerAdpater);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {

				// String step_node = spinner.getSelectedItem().toString();
				// System.out.println(position + " : " + step_node);
				switch (position) {

				case 0:
					search_start = 1;
					search_end_inclusive = 66;
					break;
				case 1:
					search_start = 1;
					search_end_inclusive = 39;
					break;
				case 2:
					search_start = 40;
					search_end_inclusive = 66;
					break;
				case 3:
					search_start = 1;
					search_end_inclusive = 5;
					break;
				case 4:
					search_start = 6;
					search_end_inclusive = 17;
					break;
				case 5:
					search_start = 18;
					search_end_inclusive = 22;
					break;
				case 6:
					search_start = 23;
					search_end_inclusive = 27;
					break;
				case 7:
					search_start = 28;
					search_end_inclusive = 39;
					break;
				case 8:
					search_start = 40;
					search_end_inclusive = 43;
					break;
				case 9:
					search_start = 44;
					search_end_inclusive = 44;
					break;
				case 10:
					search_start = 45;
					search_end_inclusive = 53;
					break;
				case 11:
					search_start = 54;
					search_end_inclusive = 57;
					break;
				case 12:
					search_start = 58;
					search_end_inclusive = 65;
					break;
				case 13:
					search_start = 66;
					search_end_inclusive = 66;
					break;

				default:
					break;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		final String get18n = get18n("输入查询词条");
		et.setText(get18n);

		et.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (et.getText().toString().equals(get18n)) {
					et.setText("");
				}
				return false;
			}
		});

		btnreturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchActivity.this.finish();
			}
		});
		/*
		 * if(this.traditional){ btnreturn.setText("返回"); }else{
		 * btnreturn.setText("返回"); }
		 */

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ProgressBar pb = (ProgressBar) SearchActivity.this
						.findViewById(R.id.progress_search);
				pb.setMax(66);
				pb.setProgress(0);

				searchTarget = et.getText().toString();

				if (searchTarget.length() < 2) {
					String string = get18n("请输入至少2个字的词语");
					Toast.makeText(SearchActivity.this, string,
							Toast.LENGTH_LONG).show();

					return;
				}

				new Thread() {

					@Override
					public void run() {

						Message message = new Message();
						message.what = message_start;
						uiHandler.sendMessage(message);

						marksList.clear();
						InputStream is = null;
						InputStreamReader isr = null;
						BufferedReader br = null;

						String simple = getSimple(searchTarget);

						for (int i = search_start; i <= search_end_inclusive; i++) {
							message = new Message();
							message.what = message_inter;
							uiHandler.sendMessage(message);

							if (marksList.size() >= maxnum) {
								break;
							}

							final String key = i < 10 ? ("0" + i) : "" + i;
							String keyFile = "" + key + ".txt";

							try {
								is = getAssets().open(keyFile);
								isr = new InputStreamReader(is,
										MainActivity.encoding);
								br = new BufferedReader(isr, 10240);

								StringBuilder sb = new StringBuilder(10240);
								String line = "";
								while ((line = br.readLine()) != null) {
									sb.append(line);
								}

								if (sb.length() > 2) {
									final String filecontent = sb.toString();
									sb.setLength(0);

									String[] xs = filecontent.split("\\s\\s");

									for (int j = 1; j < xs.length; j++) {

										if (marksList.size() >= maxnum) {
											break;
										}

										String qline = xs[j];

										if (qline.indexOf(simple) > -1) {

											int hindex = qline.indexOf(" ");

											String csub = qline.substring(0,
													hindex);
											final String[] split = csub
													.split(":");

											if (split.length >= 2) {
												String content = qline
														.substring(hindex)
														.trim();

												Mark mark = new Mark();
												mark.setVolume(key + " "
														+ xs[0]);
												mark.setChapter(Integer
														.parseInt(split[0]
																.trim()));
												mark.setSubchapter(Integer
														.parseInt(split[1]
																.trim()));
												mark.setTags(content);

												marksList.add(mark);

											}

										}
									}

								}

							} catch (IOException e) {
								// e.printStackTrace();
							} finally {
								try {
									br.close();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									// e1.printStackTrace();
								}

							}

						}

						message = new Message();
						message.what = message_end;
						uiHandler.sendMessage(message);

					}

				}.start();

				search.setEnabled(false);

			}

		});
	}
}
