package com.poiin.yourown;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import com.poiin.yourown.people.PeopleService;
import com.poiin.yourown.people.PeopleServiceImpl;
import com.poiin.yourown.people.Person;
import com.poiin.yourown.social.FriendRetriever;
import com.poiin.yourown.social.GenericFriendRetriever;
import com.poiin.yourown.ui.FriendsListAdapter;
import com.poiin.yourown.ui.FriendsListAdapter.FriendsListView;

public class MyFriendsActivity extends ListActivity {

	private FriendsListAdapter friendsListAdapter;
	private ProgressDialog progressDialog;
	private FriendRetriever friendRetriever;
	private PeopleService peopleService;
	private ApplicationState appState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appState=(ApplicationState)getApplication();
		this.setContentView(R.layout.my_friends);
		ListView listView = getListView();
		listView.setItemsCanFocus(true);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		friendRetriever = new GenericFriendRetriever((ApplicationState) getApplication());
		peopleService = new PeopleServiceImpl(appState);
		registerForContextMenu(getListView());
	}

	@Override
	protected void onResume() {
		super.onResume();
		progressDialog = ProgressDialog.show(this, "Processing", "Retrieving Friends ...");
		friendRetriever.retrieveFriends(handler);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    FriendsListView view = (FriendsListView)info.targetView;
	    Person person = view.getPerson();
	    switch (item.getItemId()) {
	    case R.id.context_menu_send_invitation:
	    	peopleService.inviteToPoiin(person);
	     return true;
	    case R.id.context_menu_already_registered:
	    	peopleService.getUserInfo(person);
	       return true;
	    default:
	      return super.onContextItemSelected(item);
	    }
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.friend_context_menu, menu);

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			@SuppressWarnings("unchecked")
			List<Person> retrievedFriends = (List<Person>) msg.getData().getSerializable("friends");
			friendsListAdapter = new FriendsListAdapter(MyFriendsActivity.this.getApplication(), retrievedFriends);
			MyFriendsActivity.this.setListAdapter(friendsListAdapter);
			progressDialog.dismiss();
		}
	};
}
