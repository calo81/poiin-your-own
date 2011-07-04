package com.poiin.yourown.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.poiin.yourown.people.Person;

public class PeopleItemizedOverlay extends ItemizedOverlay<PersonOverlayItem> {

	private List<PersonOverlayItem> people;
	private Context context;

	public PeopleItemizedOverlay(final List<Person> people,
			final Drawable defaultMarker, Context context) {
		super(PeopleItemizedOverlay.boundCenterBottom(defaultMarker));
		this.context = context;
		this.people = new ArrayList<PersonOverlayItem>();
		for (Person person : people) {
			PersonOverlayItem item = new PersonOverlayItem(person);
			this.people.add(item);
		}
		populate();
	}

	public PeopleItemizedOverlay(final List<PersonOverlayItem> people,
			final Drawable defaultMarker) {
		super(defaultMarker);
		this.people = people;
		populate();
	}

	@Override
	protected PersonOverlayItem createItem(int i) {
		return this.people.get(i);
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
		Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("poiin://poiin.yourown/"+item.getPerson().getId()+"?name="+item.getPerson().getName()+"&poiinText="+item.getPerson().getPoiinText()));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);		
		return true;
	}

}
