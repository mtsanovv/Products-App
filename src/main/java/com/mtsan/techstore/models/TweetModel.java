package com.mtsan.techstore.models;

public class TweetModel {
	private String text;

	public TweetModel() {
	}

	public TweetModel(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
