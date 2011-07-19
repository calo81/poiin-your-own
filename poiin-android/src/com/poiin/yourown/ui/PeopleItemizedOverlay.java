package com.poiin.yourown.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.TypedValue;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.poiin.yourown.R;
import com.poiin.yourown.people.Person;
import com.poiin.yourown.social.GenericProfilePictureRetriever;
import com.poiin.yourown.social.facebook.FacebookProfilePictureRetriever;

public class PeopleItemizedOverlay extends ItemizedOverlay<PersonOverlayItem> {

	private List<PersonOverlayItem> people;
	private Context context;

	public PeopleItemizedOverlay(final List<Person> people, final Drawable defaultMarker, Context context) {
		super(PeopleItemizedOverlay.boundCenterBottom(defaultMarker));
		this.context = context;
		this.people = new ArrayList<PersonOverlayItem>();
		for (Person person : people) {
			PersonOverlayItem item = new PersonOverlayItem(person);
			this.people.add(item);
		}
		populate();
	}

	public PeopleItemizedOverlay(final List<PersonOverlayItem> people, final Drawable defaultMarker) {
		super(defaultMarker);
		this.people = people;
		populate();
	}

	@Override
	protected PersonOverlayItem createItem(int i) {
		final PersonOverlayItem result = this.people.get(i);

		final LayerDrawable marker = (LayerDrawable) context.getResources().getDrawable(R.drawable.marker);
		final Drawable photo = new BitmapDrawable(configurePictureImage(result));
		if (photo != null) {
			marker.setDrawableByLayerId(R.id.photo, photo);
			marker.setDrawableByLayerId(R.id.frame, context.getResources().getDrawable(R.drawable.frame));
			//marker.setLayerInset(0, 4, 4, 40, 40);
		}
		result.setMarker(boundCenterBottom(marker));
		return result;
	}

	private Bitmap configurePictureImage(final PersonOverlayItem result) {
		return GenericProfilePictureRetriever.retrieveBitmap(context, result.getPerson().getTwitterId(),result.getPerson().getFacebookId());
	}

	@Override
	public int size() {
		return this.people.size();
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean b) {
		super.draw(canvas, mapView, false);
	}

	@Override
	protected boolean onTap(int index) {
		PersonOverlayItem item = people.get(index);
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("poiin://poiin.yourown/" + item.getPerson().getId() + "?name="
				+ item.getPerson().getName() + "&poiinText=" + item.getPerson().getPoiinText() +"&twitter_id="+ item.getPerson().getTwitterId()+"&facebook_id="+ item.getPerson().getFacebookId()+"&categories="
				+ item.getPerson().getSelectedCategories().toString()));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		return true;
	}

}
