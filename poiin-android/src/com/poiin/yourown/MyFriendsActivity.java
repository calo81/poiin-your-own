package com.poiin.yourown;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.poiin.yourown.people.Person;
import com.poiin.yourown.social.FriendRetriever;
import com.poiin.yourown.social.GenericFriendRetriever;
import com.poiin.yourown.ui.FriendsListAdapter;

public class MyFriendsActivity extends  ListActivity{

	private FriendsListAdapter friendsListAdapter;
	private ProgressDialog progressDialog;
	private FriendRetriever friendRetriever;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			this.setContentView(R.layout.my_friends);
			ListView listView = getListView();
			listView.setItemsCanFocus(false);
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			friendRetriever = new GenericFriendRetriever((ApplicationState)getApplication());
			
	}

	@Override
	protected void onResume() {
		super.onResume();
		progressDialog = ProgressDialog.show(this, "Processing", "Retrieving Friends ...");
		friendRetriever.retrieveFriends(handler);
		
	}

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			@SuppressWarnings("unchecked")
			List<Person> retrievedFriends = (List<Person>)msg.getData().getSerializable("friends");
			friendsListAdapter = new FriendsListAdapter(MyFriendsActivity.this,retrievedFriends);
			MyFriendsActivity.this.setListAdapter(friendsListAdapter);
			progressDialog.dismiss();
		}
	};
}
