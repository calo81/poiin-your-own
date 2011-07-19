package com.poiin.yourown.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
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

public class CategoriesDeleteListViewAdapter extends BaseAdapter {
	private final Context context;
	private final List<String> categories;
	private Handler deleteSelectedCategoryHandler;

	public CategoriesDeleteListViewAdapter(Context context,
			List<String> categories, Handler deleteSelectedCategoryHandler) {
		this.context = context;
		this.categories = categories;
		this.deleteSelectedCategoryHandler = deleteSelectedCategoryHandler;
	}

	@Override
	public int getCount() {
		return this.categories.size();
	}

	@Override
	public Object getItem(int position) {
		return this.categories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String category = this.categories.get(position);
		return new CategoryListView(this.context, category);
	}

	private final class CategoryListView extends LinearLayout {
		private TextView name;
		private ImageView deleteIcon;

		public CategoryListView(Context context, String name) {
			super(context);
			setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 3, 5, 0);
			this.name = new TextView(context);
			this.name.setText(name);
			this.name.setTextSize(16f);
			this.name.setTextColor(Color.WHITE);
			this.addView(this.name, params);
			this.deleteIcon = new ImageView(context);
			this.deleteIcon
					.setImageResource(android.R.drawable.ic_input_delete);
			configureDeleteButton();
			this.addView(this.deleteIcon);
		}

		private void configureDeleteButton() {
			this.deleteIcon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putString("category", CategoryListView.this.name
							.getText().toString());
					msg.setData(data);
					CategoriesDeleteListViewAdapter.this.deleteSelectedCategoryHandler
							.sendMessage(msg);
				}
			});
		}
	}
}