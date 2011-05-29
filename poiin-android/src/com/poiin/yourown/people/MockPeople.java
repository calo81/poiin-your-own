package com.poiin.yourown.people;

import java.util.ArrayList;
import java.util.List;

public class MockPeople implements People {

	public void retrieve() {
		// TODO Auto-generated method stub
		
	}

	public List<Person> iterate() {
		List<Person> list = new ArrayList<Person>();
		Person person = new Person();
		person.setLongitude(-0.187683);
		person.setLatitude(51.571266);
		list.add(person);
		person = new Person();
		person.setLongitude(0.065002);
		person.setLatitude(51.578094);
		list.add(person);
		return list;
	}


}
