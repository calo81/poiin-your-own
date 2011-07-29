package com.poiin.yourown.ui;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poiin.yourown.people.Person;

public class FriendsListAdapter extends BaseAdapter{
	private List<Person> friendList;
	private Context context;
	public FriendsListAdapter(Context context,List<Person> retrievedFriends) {
		this.context=context;
		this.friendList=retrievedFriends;
	}

	@Override
	public int getCount() {
		return friendList.size();
	}

	@Override
	public Object getItem(int position) {
		return friendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return new FriendsListView(context, this.friendList.get(position));
	}
	
	private final class FriendsListView extends LinearLayout {
		private TextView name;
		public FriendsListView(Context context, Person person) {
			super(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 3, 5, 0);
			this.name = new TextView(context);
			this.name.setText(person.getName());
			this.name.setTextSize(16f);
			this.name.setTextColor(Color.WHITE);
			this.addView(this.name, params);			
		}
	}
}
