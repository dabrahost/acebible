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
package com.gyc.ace.bible.db;

//android.text.method.DateTimeKeyListener;

public class Mark {

	private long id;

	private String volume;
	private int chapter;
	private int subchapter;
	private String tags;
	private long createdate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Mark))
			return false;

		Mark right = (Mark) o;

		return this.volume.equals(right.volume)
				&& this.chapter == right.chapter
				&& this.subchapter == right.chapter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.volume + "  " + this.chapter + ":" + this.subchapter + "  "
				+ this.tags;
	}

	public Mark() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the chapter
	 */
	public int getChapter() {
		return chapter;
	}

	/**
	 * @return the createdate
	 */
	public long getCreatedate() {
		return createdate;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the subchapter
	 */
	public int getSubchapter() {
		return subchapter;
	}

	/**
	 * @return the tags
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * @return the volume
	 */
	public String getVolume() {
		return volume;
	}

	/**
	 * @param chapter
	 *            the chapter to set
	 */
	public void setChapter(int chapter) {
		this.chapter = chapter;
	}

	/**
	 * @param createdate
	 *            the createdate to set
	 */
	public void setCreatedate(long createDate) {
		this.createdate = createDate;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param subchapter
	 *            the subchapter to set
	 */
	public void setSubchapter(int subChapter) {
		this.subchapter = subChapter;
	}

	/**
	 * @param tags
	 *            the tags to set
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	/**
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(String volume) {
		this.volume = volume;
	}

}
