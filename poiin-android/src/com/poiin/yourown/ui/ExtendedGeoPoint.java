package com.poiin.yourown.ui;

import com.google.android.maps.GeoPoint;

public class ExtendedGeoPoint extends GeoPoint {
	public ExtendedGeoPoint(double latitude, double longitude) {
		super((int) (latitude * 1E6), (int) (longitude * 1E6));
	}

}
