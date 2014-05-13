package bjtu.group4.mealplanner.activity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import bjtu.group4.mealplanner.R;
import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class StartPageFragment extends Fragment 
implements
ConnectionCallbacks,
OnConnectionFailedListener,
LocationListener,
OnMyLocationButtonClickListener{

	private MapView mMapView;
	private GoogleMap mMap;
	private LocationClient mLocationClient;
	private TextView myTextView;
	private final static String TAG = StartPageFragment.class.getName();

	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000)         // 5 seconds
			.setFastestInterval(16)    // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.fragment_startpage,
				container, false);
		mMapView = (MapView) messageLayout.findViewById(R.id.map);
		myTextView = (TextView)messageLayout.findViewById(R.id.location);
		mMapView.onCreate(savedInstanceState);
		setUpMapIfNeeded();
		return messageLayout;
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		mMapView.onResume();
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			if(mMapView != null)
				mMap = mMapView.getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	/**
	 * This is where we can add markers or lines, add listeners or move the camera. In this case, we
	 * just add a marker near Africa.
	 * <p>
	 * This should only be called once and when we are sure that {@link #mMap} is not null.
	 */
	private void setUpMap() {
		mMap.setMyLocationEnabled(true);
		mMap.setOnMyLocationButtonClickListener(this);
		//mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(
					getActivity().getApplicationContext(),
					this,  // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	public void showMyLocation(View view) {
		if (mLocationClient != null && mLocationClient.isConnected()) {
			String msg = "Location = " + mLocationClient.getLastLocation();
			Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mMapView.onLowMemory();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	/**
	 * Implementation of {@link LocationListener}.
	 */
	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG,"onLocationChanged");
	}

	/**
	 * Callback called when connected to GCore. Implementation of {@link ConnectionCallbacks}.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		mLocationClient.requestLocationUpdates(REQUEST, this);  // LocationListener
	}

	/**
	 * Callback called when disconnected from GCore. Implementation of {@link ConnectionCallbacks}.
	 */
	@Override
	public void onDisconnected() {
		Toast.makeText(getActivity(), "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * Implementation of {@link OnConnectionFailedListener}.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Toast.makeText(getActivity(), "Connection Failure : " + 
				result.getErrorCode(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onMyLocationButtonClick() {
		// Get the current location's latitude & longitude
		if (mLocationClient != null && mLocationClient.isConnected()) {
			Location currentLocation = mLocationClient.getLastLocation();
			if(currentLocation != null) {
				String msg = "Current Location: " +
						Double.toString(currentLocation.getLatitude()) + "," +
						Double.toString(currentLocation.getLongitude());

				// Display the current location in the UI
				myTextView.setText(msg);
				GetAddressTask task = new GetAddressTask((Context)getActivity());
				task.execute(currentLocation);
			}
			else {
				Toast.makeText(getActivity(), "GetLastLocation Failure : ",
						Toast.LENGTH_SHORT).show();
			}
		}
		// Return false so that we don't consume the event and the default behavior still occurs
		// (the camera animates to the user's current position).
		return false;
	}

	/*
	 * Following is a subclass of AsyncTask which has been used to get
	 * address corresponding to the given latitude & longitude.
	 */
	private class GetAddressTask extends AsyncTask<Location, Void, String>{
		Context mContext;
		public GetAddressTask(Context context) {
			super();
			mContext = context;
		}

		/*
		 * When the task finishes, onPostExecute() displays the address. 
		 */
		@Override
		protected void onPostExecute(String address) {
			// Display the current address in the UI
			myTextView.setText(address);
		}
		@Override
		protected String doInBackground(Location... params) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
			// Get the current location from the input parameter list
			Location loc = params[0];
			// Create a list to contain the result address
			List<Address> addresses = null;
			try {
				addresses = geocoder.getFromLocation(loc.getLatitude(),
						loc.getLongitude(), 1);
			} catch (IOException e1) {
				Log.e(TAG, "IO Exception in getFromLocation()");
				e1.printStackTrace();
				return ("IO Exception trying to get address");
			} catch (IllegalArgumentException e2) {
				// Error message to post in the log
				String errorString = "Illegal arguments " +
						Double.toString(loc.getLatitude()) +
						" , " +
						Double.toString(loc.getLongitude()) +
						" passed to address service";
				Log.e(TAG, errorString);
				e2.printStackTrace();
				return errorString;
			}
			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {
				// Get the first address
				Address address = addresses.get(0);
				/*
				 * Format the first line of address (if available),
				 * city, and country name.
				 */
				String addressText = String.format("%s, %s, %s, %s, %s",
						// If there's a street address, add it
						address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
								address.getLocality(), address.getSubAdminArea(),
								address.getThoroughfare(), address.getFeatureName());
				// Return the text
				return addressText;
			} else {
				return "No address found";
			}
		}
	}// AsyncTask class
}
