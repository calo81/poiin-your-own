package com.poiin.yourown.ui;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.poiin.yourown.people.People;
import com.poiin.yourown.people.Person;

public class PeopleItemizedOverlay extends ItemizedOverlay<PersonOverlayItem> {

	private List<PersonOverlayItem> people;
	
	public PeopleItemizedOverlay(final People people, final Drawable defaultMarker) {
		super(defaultMarker);
		this.people=new ArrayList<PersonOverlayItem>();
		for(Person person:people.iterate()){
			PersonOverlayItem item = new PersonOverlayItem(person);
			this.people.add(item);
		}
		populate();
	}

	public PeopleItemizedOverlay(final List<PersonOverlayItem> people, final Drawable defaultMarker) {
		super(defaultMarker);
		this.people=people;
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

}
