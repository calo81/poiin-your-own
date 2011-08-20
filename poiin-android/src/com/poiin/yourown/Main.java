package com.poiin.yourown;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.facebook.android.Facebook;
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
import com.poiin.yourown.social.facebook.FacebookAuthenticator;
import com.poiin.yourown.social.twitter.TwitterAuthenticatorHelper;
import com.poiin.yourown.storage.Data;
import com.poiin.yourown.storage.PreferencesBackedData;
import com.poiin.yourown.ui.ExtendedGeoPoint;

public class Main extends MapActivity {

	private static final int FRIENDS_MENU_ID = 2;
	private static final int PROFILE_MENU_ID = 1;
	private static final int MY_LOCATION_MENU_ID = 3;
	private MapController mapController;
	private LocationManager locationManager;
	private LocationProvider locationProvider;
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	private PoiinService poiinService;
	private EditText popupText;
	private Button popupButton;
	private PopupWindow popUp;
	private Button poiinButton;
	private ApplicationState appState;
	private boolean comingFromNewIntent;
	private Data data;
	private Facebook facebook = new Facebook("149212958485869");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		appState = ((ApplicationState) getApplication());
		appState.setApplicationFullyStarted(true);
		configurePoiinButton();
		initMap();
		startService(new Intent(this, PoiinBackgroundService.class));
		poiinService = new PoiinServiceImpl();
		data = new PreferencesBackedData(this);
		initTwitterIfExistentButLoggedWithFacebook();
		initFacebookIfExistentButLoggedWithTwitter();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		listenOnLocationChange();
		establishMyLocation();
		if (!comingFromNewIntent) {
			locateMap();
		}
		comingFromNewIntent = false;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onNewIntent(Intent newIntent) {
		centerMapInLastPoiin(newIntent);
		comingFromNewIntent = true;
	}

	private void centerMapInLastPoiin(Intent newIntent) {
		super.onNewIntent(newIntent);
		Person lastPoiin = appState.getLastPoiinPerson();
		if (lastPoiin != null) {
			GeoPoint point = new ExtendedGeoPoint(lastPoiin.getLatitude(), lastPoiin.getLongitude());
			mapController.animateTo(point);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, PROFILE_MENU_ID, 0, null).setIcon(R.drawable.boton_me);
		menu.add(0, FRIENDS_MENU_ID, 0, "Friends").setIcon(android.R.drawable.ic_menu_more);
		menu.add(0, MY_LOCATION_MENU_ID, 0, "My Location").setIcon(android.R.drawable.ic_menu_directions);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
		switch (item.getItemId()) {
		case MY_LOCATION_MENU_ID:
			this.mapController.animateTo(getLastKnownPoint());
			break;
		case PROFILE_MENU_ID:
			startActivity(new Intent(this, ProfileActivity.class));
			break;
		case FRIENDS_MENU_ID:
			startActivity(new Intent(this, MyFriendsActivity.class));
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onPause() {
		super.onPause();
		this.locationManager.removeUpdates(locationListenerRecenterMap);
		myLocationOverlay.disableMyLocation();
	}

	private void configurePoiinButton() {
		poiinButton = (Button) findViewById(R.id.buttonPoiin);
		poiinButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (establishedLocation) {
					poiin();
				} else {
					Toast msg =  Toast.makeText(Main.this.getApplicationContext(), "Still locating your device, wait a couple of seconds..", Toast.LENGTH_SHORT);
					msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
					msg.show();
				}
			}
		});
	}

	private void locateMap() {
		this.mapController = this.mapView.getController();
		this.mapController.setZoom(16);
		goToMyLocation();
	}

	private void goToMyLocation() {
		myLocationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				if (myLocationOverlay.getMyLocation() != null) {
					mapController.animateTo(myLocationOverlay.getMyLocation());
				}
			}
		});
	}

	private void establishMyLocation() {
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();
	}

	private void listenOnLocationChange() {
		this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		this.locationProvider = this.locationManager.getProvider(LocationManager.GPS_PROVIDER);
		if (this.locationProvider != null) {
			this.locationManager.requestLocationUpdates(this.locationProvider.getName(), 3000, 1000, this.locationListenerRecenterMap);
		} else {
			Toast.makeText(this, "Location Provider Not available.", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private void initMap() {
		mapView = (MapView) findViewById(R.id.map_view);
		mapView.setBuiltInZoomControls(false);
		mapController = mapView.getController();
		mapView.displayZoomControls(false);
		setNotZoomable();
		appState.setMapView(mapView);
	}

	private void setNotZoomable() {
		mapView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getPointerCount() > 1) {
					return true;
				}
				return false;
			}
		});
	}

	private void poiin() {
		configureAndShowPopUp();
		popupButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ApplicationState application = appState;
				Poiin poiin = new Poiin(getLastKnownPoint(), application.getMe());
				String poiinText = popupText.getText().toString() != null ? popupText.getText().toString() : "";
				poiin.setText(poiinText);
				poiinService.sendPoiin(poiin);
				popUp.dismiss();
			}
		});
	}

	private void configureAndShowPopUp() {
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popUp = new PopupWindow(inflater.inflate(R.layout.send_poiin_popup, null, false), 450, 450, true);
		popUp.showAtLocation(this.findViewById(R.id.map_view), Gravity.TOP | Gravity.LEFT, 20, 100);

		popupButton = (Button) popUp.getContentView().findViewById(R.id.sendPoiinButton);
		popupText = (EditText) popUp.getContentView().findViewById(R.id.poiinText);
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

	private void initTwitterIfExistentButLoggedWithFacebook() {
		if (data.getLoginOption() == Data.LoginOption.FACEBOOK) {
			try {
				if (appState.getMe().get("twitter_id") != null) {
					new TwitterAuthenticatorHelper(this, appState).configureApplicationTwitter();
				}
			} catch (JSONException e) {
				// TODO Nothing to do here
				e.printStackTrace();
			}
		}
	}

	private void initFacebookIfExistentButLoggedWithTwitter() {
		if (data.getLoginOption() == Data.LoginOption.TWITTER) {
			try {
				if (appState.getMe().get("facebook_id") != null) {
					new FacebookAuthenticator(this, facebook).authenticate();
				}
			} catch (JSONException e) {
				// TODO Nothing to do here
				e.printStackTrace();
			}
		}
	}

	/**
	 * Implemented for facebook requirements
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	private boolean establishedLocation = false;
	private final LocationListener locationListenerRecenterMap = new LocationListener() {

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
			establishedLocation = false;
		}

		public void onLocationChanged(final Location loc) {
			int lat = (int) (loc.getLatitude() * LocationHelper.MILLION);
			int lon = (int) (loc.getLongitude() * LocationHelper.MILLION);
			GeoPoint geoPoint = new GeoPoint(lat, lon);
			mapController.animateTo(geoPoint);
			establishedLocation = true;
		}
	};

}