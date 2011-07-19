package com.poiin.yourown;

import java.util.ArrayList;
import java.util.List;

import com.poiin.yourown.people.PeopleService;
import com.poiin.yourown.people.PeopleServiceImpl;
import com.poiin.yourown.people.Person;
import com.poiin.yourown.social.facebook.FacebookAuthentication;
import com.poiin.yourown.ui.CategoriesDeleteListViewAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FirstTimeProfileActivity extends Activity {

	private ExpandableListView listView;
	private ExpandableListAdapter listAdapter;
	private ProgressDialog progressDialog;
	private PeopleService peopleService;
	private Button sendProfileButton;
	private ListView categoriesAddedList;
	private BaseAdapter addedCategoriesListAdapter;
	private List<String> selectedCategories = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		peopleService = new PeopleServiceImpl(
				(ApplicationState) this.getApplication());
		listAdapter = new MyExpandableListAdapter();
		setContentView(R.layout.initial_profile);
		listView = (ExpandableListView) findViewById(R.id.categoryList);
		listView.setOnChildClickListener((OnChildClickListener) listAdapter);
		listView.setAdapter(listAdapter);
		categoriesAddedList = (ListView) findViewById(R.id.categoriesSelected);
		configureButtonListener();
		addedCategoriesListAdapter = new CategoriesDeleteListViewAdapter(
				FirstTimeProfileActivity.this, selectedCategories,
				deleteSelectedCategoryHandler);
		categoriesAddedList.setAdapter(addedCategoriesListAdapter);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void configureButtonListener() {
		sendProfileButton = (Button) findViewById(R.id.saveCategoriesButton);
		sendProfileButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				progressDialog = ProgressDialog.show(
						FirstTimeProfileActivity.this, "Processing . . .",
						"Saving User Information ...", true, false);
				Person person = new Person();
				person.setSelectedCategories(selectedCategories);
				peopleService.registerUser(person, registrationHandler);
			}
		});
	}

	private final Handler registrationHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			progressDialog.dismiss();
			startActivity(new Intent(
					FirstTimeProfileActivity.this.getApplicationContext(),
					Main.class));
			finish();
		}
	};

	private Handler deleteSelectedCategoryHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String category = msg.getData().getString("category");
			selectedCategories.remove(category);
			addedCategoriesListAdapter.notifyDataSetChanged();
		}
	};

	public class MyExpandableListAdapter extends BaseExpandableListAdapter
			implements OnChildClickListener {
		private String[] groups = { "Country", "Profesion", "Misc" };
		private String[][] children = { { "Venezuela", "England", "Spain" },
				{ "Programmer", "CEO" }, { "Gay", "Looking for couple" } };

		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return children[groupPosition].length;
		}

		public TextView getGenericView() {
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 64);
			TextView textView = new TextView(FirstTimeProfileActivity.this);
			textView.setLayoutParams(lp);
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			textView.setPadding(36, 0, 0, 0);
			return textView;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			textView.setText(getChild(groupPosition, childPosition).toString());
			return textView;
		}

		public Object getGroup(int groupPosition) {
			return groups[groupPosition];
		}

		public int getGroupCount() {
			return groups.length;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			textView.setText(getGroup(groupPosition).toString());
			return textView;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			selectedCategories.add(((TextView) v).getText().toString());
			addedCategoriesListAdapter.notifyDataSetChanged();
			return true;
		}

	}

}
