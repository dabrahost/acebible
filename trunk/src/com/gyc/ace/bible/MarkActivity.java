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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.gyc.ace.bible.db.DataHelper;
import com.gyc.ace.bible.db.Mark;
import com.gyc.lit.LitUtils;

public class MarkActivity extends Activity {

	private static final String SPACE_THREE = "   ";
	public static final String PR_TEXT = "text";
	public static final String PR_VOLUME = "volume";
	public static final String PR_VOLUME_KEY = "volumekey";

	private static final int CCM_LOCATE = 0;
	private static final int CCM_DELETE = 1;
	private static final int CCM_EDIT = 2;
	private static final int CCM_SEND = 3;

	private com.gyc.ace.bible.db.DataHelper dbHelper;
	private List<CharSequence> marksString;
	private List<Mark> marksList;
	private ListView listview;
	private String add_volume_key;

	private boolean listmodified = false;
	private boolean traditional = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		SharedPreferences settings = getSharedPreferences(
				MainActivity.PREFS_NAME, 0);

		this.traditional = settings.getBoolean("traditional", false);

		dbHelper = new DataHelper(this);

		Intent it = this.getIntent();
		if (it != null) {
			Bundle bundle = it.getExtras();
			if (bundle != null && bundle.get(MarkActivity.PR_VOLUME) != null) {
				this.setContentView(R.layout.mark_add);

				final String volume = bundle.getString(MarkActivity.PR_VOLUME);
				String marktext = (String) bundle.get(MarkActivity.PR_TEXT);
				add_volume_key = bundle.getString(MarkActivity.PR_VOLUME_KEY);

				// System.out.println("add_volume_key :" + add_volume_key);

				int splitIndex = marktext.indexOf(" ");
				final String chapter;
				final String chaptercontent;

				if (splitIndex > -1) {
					chapter = marktext.substring(0, splitIndex).trim();
					chaptercontent = marktext.substring(splitIndex).trim();
				} else {
					chapter = "";
					chaptercontent = marktext;
				}

				TextView index = (TextView) this.findViewById(R.id.markIndex);
				index.setText(volume + "  " + chapter);

				TextView content = (TextView) this
						.findViewById(R.id.markContent);

				content.setText(chaptercontent);

				Button btnreturn = (Button) this
						.findViewById(R.id.btnSaveMarkRtn);

				btnreturn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MarkActivity.this.finish();
					}
				});

				Button btnshowlist = (Button) this
						.findViewById(R.id.btnSaveMark2List);

				btnshowlist.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showList();
					}
				});

				final Button btnsaveMark = (Button) this
						.findViewById(R.id.btnSaveMark);

				String string = get18n("增加书签");
				String string2 = get18n("增加书签-保存");

				this.setTitle(string);
				btnsaveMark.setText(string2);

				btnsaveMark.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						Mark mark2add = new Mark();

						mark2add.setVolume(add_volume_key + " " + volume);

						mark2add.setChapter(Integer
								.parseInt(chapter.split(":")[0]));

						mark2add.setSubchapter(Integer.parseInt(chapter
								.split(":")[1]));

						final EditText tags = (EditText) findViewById(R.id.mark_tags);

						mark2add.setTags(chaptercontent.substring(0,
								chaptercontent.length() > 20 ? 20
										: chaptercontent.length())
								+ MarkActivity.SPACE_THREE
								+ tags.getText().toString().trim().replaceAll(
										"\r", "").replaceAll("\n", "").replace(
										MarkActivity.SPACE_THREE, " ").replace(
										"  ", " "));

						mark2add.setCreatedate(System.currentTimeMillis());

						String string = get18n("保存");
						String string2 = get18n("确定 ");
						try {

							dbHelper.insertTask(mark2add);

							MarkActivity.this.add(mark2add);

							btnsaveMark.setEnabled(false);

							final AlertDialog.Builder builder = new AlertDialog.Builder(
									MarkActivity.this);

							String string3 = get18n("保存信息完毕");
							builder.setTitle(string).setMessage(string3)
									.setCancelable(true).setNeutralButton(
											string2, null).show();

						} catch (Exception e) {

							// e.printStackTrace();

							final AlertDialog.Builder builder = new AlertDialog.Builder(
									MarkActivity.this);
							String string3 = get18n("保存信息失败");
							builder.setTitle(string).setMessage(string3)
									.setCancelable(true).setNeutralButton(
											string2, null).show();
						} finally {

						}
					}
				});

				// System.out.println(volume + ":" + marktext);
			} else {

				showList();
			}
		}
	}

	private String get18n(String string) {

		if (this.traditional) {
			return LitUtils.toTraditional(string);
		}
		return string;
	}

	/**
	 * @param object
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(Mark object) {
		if (marksList != null) {
			listmodified = true;
			// marksString.add(object.toString());
			return marksList.add(object);
		}
		return true;
	}

	/**
	 * @param object
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object object) {
		marksString.remove(object.toString());
		return marksList.remove(object);
	}

	private void showList() {

		this.setContentView(R.layout.mark_list);

		this.setTitle("所有书签");

		if (marksList == null || listmodified) {
			marksList = dbHelper.getAllMarks();
			listmodified = false;
		}

		marksString = new ArrayList<CharSequence>(marksList.size());

		for (Iterator<Mark> iterator = marksList.iterator(); iterator.hasNext();) {
			Mark mark = iterator.next();

			marksString.add(mark.toString());
		}

		listview = (ListView) findViewById(R.id.list_marks);
		listview.setAdapter(new BaseAdapterNull(this));

		final Adapter4MarkList baseAdapterNew = new Adapter4MarkList(this);

		baseAdapterNew.setContentList(marksString);

		baseAdapterNew.setTextsize(16);

		listview.setAdapter(baseAdapterNew);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listview.setSelection(0);
		listview.requestFocus();

		Button btn = (Button) this.findViewById(R.id.btnRtnFromMarks);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MarkActivity.this.finish();
			}
		});

		this.registerForContextMenu(listview);

	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.add(0, CCM_SEND, 1, get18n("发送给朋友"));
		menu.add(0, CCM_LOCATE, 0, get18n("打开"));
		menu.add(0, CCM_EDIT, 2, get18n("编辑"));
		menu.add(0, CCM_DELETE, 3, get18n("删除"));
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		// long itemid = info.id;
		long itemposition = info.position;

		// System.out.println(itemid + ":" + itemposition);

		// TextView tv = (TextView) info.targetView;

		final Mark mark = marksList.get((int) itemposition);

		// System.out.println(mark);

		TextView content2;
		EditText tags2;

		Button btnreturn2;
		Button btnshowlist2;
		switch (item.getItemId()) {
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
			key = volume.substring(0, hindex).trim();
			chapter = mark.getChapter();
			subchapter = mark.getSubchapter();

			Intent it = new Intent(Intent.ACTION_VIEW);
			it.putExtra("sms_body", mark.toString());
			it.setType("vnd.android-dir/mms-sms");
			startActivityForResult(it, 1);

			break;
		case CCM_EDIT:

			volume = mark.getVolume();
			hindex = volume.indexOf(" ");
			key = volume.substring(0, hindex).trim();
			chapter = mark.getChapter();
			subchapter = mark.getSubchapter();

			this.setContentView(R.layout.mark_add);

			TextView index = (TextView) this.findViewById(R.id.markIndex);
			index.setText(volume + "  " + chapter);

			final TextView content = (TextView) this
					.findViewById(R.id.markContent);

			content.setText(mark.getTags().split(MarkActivity.SPACE_THREE)[0]);

			final EditText tags = (EditText) this.findViewById(R.id.mark_tags);

			tags.setText(mark.getTags().split(MarkActivity.SPACE_THREE)[1]);

			Button btnreturn = (Button) this.findViewById(R.id.btnSaveMarkRtn);

			btnreturn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MarkActivity.this.finish();
				}
			});

			Button btnshowlist = (Button) this
					.findViewById(R.id.btnSaveMark2List);

			btnshowlist.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showList();
				}
			});

			final Button btnsaveMark = (Button) this
					.findViewById(R.id.btnSaveMark);

			String string = get18n("修改书签");
			this.setTitle(string);
			String string2 = get18n("修改书签-确定");
			btnsaveMark.setText(string2);

			btnsaveMark.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					EditText xtags = (EditText) findViewById(R.id.mark_tags);

					mark.setTags(content.getText().toString()
							+ MarkActivity.SPACE_THREE
							+ xtags.getText().toString().replaceAll("\r", "")
									.replaceAll("\n", "").replaceAll(
											SPACE_THREE, " ").replaceAll("  ",
											" "));
					// System.out.println(mark.getTags());

					String string3 = get18n("保存");
					String string5 = get18n("确定 ");
					try {
						dbHelper.updateMark(mark);

						listmodified = true;

						btnsaveMark.setEnabled(false);

						final AlertDialog.Builder builder = new AlertDialog.Builder(
								MarkActivity.this);

						String string4 = get18n("修改完毕");
						builder.setTitle(string3).setMessage(string4)
								.setCancelable(true).setNeutralButton(string5,
										null).show();

					} catch (Exception e) {

						final AlertDialog.Builder builder = new AlertDialog.Builder(
								MarkActivity.this);
						String string4 = get18n("修改失败");
						builder.setTitle(string3).setMessage(string4)
								.setCancelable(true).setNeutralButton(string5,
										null).show();
					} finally {

					}
				}
			});

			break;
		case CCM_DELETE:
			volume = mark.getVolume();
			hindex = volume.indexOf(" ");
			key = volume.substring(0, hindex).trim();
			chapter = mark.getChapter();
			subchapter = mark.getSubchapter();

			this.setContentView(R.layout.mark_add);

			index = (TextView) this.findViewById(R.id.markIndex);
			index.setText(volume + "  " + chapter);

			content2 = (TextView) this.findViewById(R.id.markContent);

			content2.setText(mark.getTags().split(MarkActivity.SPACE_THREE)[0]);

			tags2 = (EditText) this.findViewById(R.id.mark_tags);

			tags2.setText(mark.getTags().split(MarkActivity.SPACE_THREE)[1]);

			btnreturn2 = (Button) this.findViewById(R.id.btnSaveMarkRtn);

			btnreturn2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MarkActivity.this.finish();
				}
			});

			btnshowlist2 = (Button) this.findViewById(R.id.btnSaveMark2List);

			btnshowlist2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showList();
				}
			});

			final Button btnsaveMark2 = (Button) this
					.findViewById(R.id.btnSaveMark);

			this.setTitle(get18n("删除书签"));
			btnsaveMark2.setText(get18n("删除书签-确定"));

			btnsaveMark2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					String get18n = get18n("确定 ");
					String get18n2 = get18n("删除书签");
					try {
						dbHelper.delete(mark.getId());
						listmodified = true;

						btnsaveMark2.setEnabled(false);

						String get18n3 = get18n("删除完毕!");
						new AlertDialog.Builder(MarkActivity.this).setTitle(
								get18n2).setMessage(get18n3)
								.setCancelable(true).setNeutralButton(get18n,
										null).show();

					} catch (Exception e) {
						// e.printStackTrace();

						final AlertDialog.Builder builder = new AlertDialog.Builder(
								MarkActivity.this);
						String get18n3 = get18n("删除失败!");
						builder.setTitle(get18n2).setMessage(get18n3)
								.setCancelable(true).setNeutralButton(get18n,
										null).show();
					} finally {

					}
				}
			});

			break;
		default:
			break;

		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// System.out.println("ondestroy");
		dbHelper.close();
		super.onDestroy();
	}
}
