package com.poiin.yourown.ui;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poiin.yourown.R;
import com.poiin.yourown.people.Person;
import com.poiin.yourown.social.GenericProfilePictureRetriever;
import com.poiin.yourown.social.UrlBasedImageRetrieverHelper;

public class FriendsListAdapter extends BaseAdapter {
	private List<Person> friendList;
	private Context context;
	private ExecutorService pictureRetrieverExecutor;

	public FriendsListAdapter(Context context, List<Person> retrievedFriends) {
		this.context = context;
		this.friendList = retrievedFriends;
		pictureRetrieverExecutor = Executors.newSingleThreadExecutor();
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
		private ImageView image;

		public FriendsListView(final Context context, final Person person) {
			super(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 3, 5, 0);
			this.name = new TextView(context);
			this.name.setText(person.getName());
			this.name.setTextSize(16f);
			this.name.setTextColor(Color.WHITE);
			this.image = new ImageView(context);
			this.image.setImageDrawable(context.getResources().getDrawable(R.drawable.image_placeholder));
			this.addView(image, params);
			this.addView(this.name, params);
			loadActualImage(context, person);
		}

		private void loadActualImage(final Context context, final Person person) {
			pictureRetrieverExecutor.execute(new Runnable() {
				@Override
				public void run() {
					Bitmap picture =UrlBasedImageRetrieverHelper.retrieveBitmapFromUrl(person.getProfilePictureUrl());
					if (picture != null) {
						Bundle bundle = new Bundle();
						bundle.putParcelable("image", picture);
						Message msg = new Message();
						msg.setData(bundle);
						actualImageLoadedHandler.sendMessage(msg);
					}
				}
			});
		}

		private Handler actualImageLoadedHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				image.setImageBitmap((Bitmap)msg.getData().getParcelable("image"));
			}

		};
	}
}
