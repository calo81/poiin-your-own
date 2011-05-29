package com.poiin.yourown.ui;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

public class PersonDrawable extends ShapeDrawable {
	private int radius = 10;
	private GeoPoint point = null;
	private MapView mapView = null;

	public PersonDrawable(GeoPoint point, MapView mapView) {
		this.point = point;
		this.mapView = mapView;
	}

	public void draw(Canvas canvas) {
		Projection projection = mapView.getProjection();
		Point pt = projection.toPixels(point, null);
		canvas.drawCircle(pt.x, pt.y, radius, getPaint());
	}

	public int getIntrinsicHeight() {
		return 2 * radius;
	}

	public int getIntrinsicWidth() {
		return 2 * radius;
	}
}