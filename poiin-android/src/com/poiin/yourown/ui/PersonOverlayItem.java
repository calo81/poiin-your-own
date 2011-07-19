package com.poiin.yourown.ui;

import com.google.android.maps.OverlayItem;
import com.poiin.yourown.people.Person;

public class PersonOverlayItem extends OverlayItem {

	private Person person;

	public PersonOverlayItem(Person person) {
		super(
				new ExtendedGeoPoint(person.getLatitude(),
						person.getLongitude()), person.getName(), person
						.getName());
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}
}
