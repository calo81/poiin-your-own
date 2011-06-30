package com.poiin.yourown;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.poiin.yourown.location.LocationHelper;
import com.poiin.yourown.people.Person;
import com.poiin.yourown.poiin.Poiin;
import com.poiin.yourown.poiin.PoiinService;
import com.poiin.yourown.poiin.PoiinServiceImpl;
import com.poiin.yourown.ui.ExtendedGeoPoint;

public class Main extends MapActivity {

	private static final int PROFILE = 1;
	private static final int POIIN_MENU_ID = 0;
	private static final int MY_LOCATION_MENU_ID = 3;
	private MapController mapController;
	private LocationManager locationManager;
	private LocationProvider locationProvider;
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	private PoiinService poiinService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initMap();	
		startService(new Intent(this, PoiinBackgroundService.class));
		poiinService = new PoiinServiceImpl();
	}

	@Override
	public void onStart() {
		super.onStart();
		locateMap();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	

	@Override
	public void onNewIntent(Intent newIntent) {
		centerMapInLastPoiin(newIntent);
		
	}

	private void centerMapInLastPoiin(Intent newIntent) {
		super.onNewIntent(newIntent);
		Person lastPoiin = ((ApplicationState)getApplication()).getLastPoiinPerson();
		GeoPoint point = new ExtendedGeoPoint(lastPoiin.getLatitude(), lastPoiin.getLongitude());
		mapController.animateTo(point);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, POIIN_MENU_ID, 0, "Poiin").setIcon(android.R.drawable.ic_menu_more);
		menu.add(0, PROFILE, 0, "Profile").setIcon(android.R.drawable.ic_menu_more);
		menu.add(0, 2, 0, "Friends").setIcon(android.R.drawable.ic_menu_more);
		menu.add(0, MY_LOCATION_MENU_ID, 0, "My Location").setIcon(android.R.drawable.ic_menu_more);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
		switch (item.getItemId()) {
		case MY_LOCATION_MENU_ID:
			this.mapController.animateTo(getLastKnownPoint());
			break;
		case POIIN_MENU_ID:
			poiin();
			break;
		case PROFILE:
			startActivity(new Intent(this, ProfileActivity.class));
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public void onPause(){
	    super.onPause();
	    this.locationManager.removeUpdates(locationListenerRecenterMap);
	    myLocationOverlay.disableMyLocation();
	}
	    

	private void locateMap() {
		this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		this.locationProvider = this.locationManager.getProvider(LocationManager.GPS_PROVIDER);
		this.mapController = this.mapView.getController();
		this.mapController.setZoom(10);
		establishMyOwnLocationAndMarker();
		listenOnLocationChange();
	}


	private void establishMyOwnLocationAndMarker() {
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				mapController.animateTo(myLocationOverlay.getMyLocation());
			}
		});
	}

	private void listenOnLocationChange() {
		if (this.locationProvider != null) {
			this.locationManager.requestLocationUpdates(this.locationProvider.getName(), 3000, 1000, this.locationListenerRecenterMap);
		} else {
			Toast.makeText(this, "Location Provider Not available.", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private void initMap() {
		mapView = (MapView) findViewById(R.id.map_view);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapView.displayZoomControls(true);
		((ApplicationState)getApplication()).setMapView(mapView);
	}

	private void poiin() {
		ApplicationState application = (ApplicationState)getApplication();
		Poiin poiin = new Poiin(getLastKnownPoint(),application.getMe());
		poiinService.sendPoiin(poiin);
	}

	private GeoPoint getLastKnownPoint() {
		if (myLocationOverlay.getMyLocation() != null) {
			return myLocationOverlay.getMyLocation();
		}
		Location lastKnownLocation = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastKnownLocation != null) {
			return LocationHelper.getGeoPoint(lastKnownLocation);
		}
		return null;
	}

	private final LocationListener locationListenerRecenterMap = new LocationListener() {

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}

		public void onLocationChanged(final Location loc) {
			int lat = (int) (loc.getLatitude() * LocationHelper.MILLION);
			int lon = (int) (loc.getLongitude() * LocationHelper.MILLION);
			GeoPoint geoPoint = new GeoPoint(lat, lon);
			mapController.animateTo(geoPoint);
		}
	};

}