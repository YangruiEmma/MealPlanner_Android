package bjtu.group4.mealplanner.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.drive.events.ResourceEvent;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.Restaurant;
import bjtu.group4.mealplanner.utils.ConnectServer;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
	private boolean isServiceOk = true;
	private Location mLocation = new Location("");
	private List<Restaurant> mRestList;
	private HashMap<String, Restaurant> mHMReference = new HashMap<String, Restaurant>();

	private final static String TAG = StartPageFragment.class.getName();
	private static final float UNDEFINED_COLOR = -1;
	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000)         // 5 seconds
			.setFastestInterval(16)    // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.fragment_startpage,
				container, false);

		if(servicesConnected()) {
			mMapView = (MapView) messageLayout.findViewById(R.id.map);
			myTextView = (TextView)messageLayout.findViewById(R.id.location);
			mMapView.onCreate(savedInstanceState);
			setUpMapIfNeeded();
			mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker marker) {
					if(!mHMReference.containsKey(marker.getId()))
						return false;    		
					Restaurant rest = mHMReference.get(marker.getId());
					
					GetRestInfoAsyncTask task = new GetRestInfoAsyncTask();
					task.execute(rest.getName());
					return false;
				}
				
			});    	
		}
		return messageLayout;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(isServiceOk == false)
			return;
		setUpMapIfNeeded();
		mMapView.onResume();
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
	}

	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
		if(ConnectionResult.SUCCESS == resultCode) {
			Log.d("StartPageFragment", "Google Play services is available.");
			isServiceOk = true;
		} else {
			ConnectionResult connectionResult = new ConnectionResult(resultCode, null);
			int errorCode = connectionResult.getErrorCode();
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					errorCode, getActivity(), -1);
			if(errorDialog != null) {
				errorDialog.show();
			}
			isServiceOk = false;
		}
		return isServiceOk;
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			if(mMapView != null)
				mMap = mMapView.getMap();
			MapsInitializer.initialize(getActivity());
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		mMap.setMyLocationEnabled(true);
		mMap.setOnMyLocationButtonClickListener(this);

	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getActivity().getApplicationContext(),this,this);
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
	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG,"onLocationChanged");
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mLocationClient.requestLocationUpdates(REQUEST, this);  // LocationListener
		if (mLocationClient != null && mLocationClient.isConnected()) {
			mLocation = mLocationClient.getLastLocation();
			if(mLocation != null) {
				LatLng latlng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
				mMap.setMyLocationEnabled(true);
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
			}
		}
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(getActivity(), "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Toast.makeText(getActivity(), "Connection Failure : " + 
				result.getErrorCode(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onMyLocationButtonClick() {
		if (mLocationClient != null && mLocationClient.isConnected()) {
			mLocation = mLocationClient.getLastLocation();
			if(mLocation != null) {
				String msg = "Current Location: " +
						Double.toString(mLocation.getLatitude()) + "," +
						Double.toString(mLocation.getLongitude());
				myTextView.setText(msg);
				GetAddressTask task = new GetAddressTask((Context)getActivity());
				task.execute(mLocation);
				SearchAsyncTask taskSearch = new SearchAsyncTask();
				taskSearch.execute(mLocation);

			}
			else {
				Toast.makeText(getActivity(), "GetLastLocation Failure : ",
						Toast.LENGTH_SHORT).show();
			}
		}
		return false;
	}


	private Marker drawMarker(LatLng latLng,float color, String titile){
		MarkerOptions markerOptions = new MarkerOptions();	                  
		markerOptions.position(latLng); 
		markerOptions.title(titile);
		if(color != UNDEFINED_COLOR)
			markerOptions.icon(BitmapDescriptorFactory.defaultMarker(color));
		Marker m = mMap.addMarker(markerOptions);
		return m;     

	}			

	private class GetAddressTask extends AsyncTask<Location, Void, String>{
		Context mContext;
		public GetAddressTask(Context context) {
			super();
			mContext = context;
		}
		@Override
		protected void onPostExecute(String address) {
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
				String addressText = String.format("%s, %s, %s, %s",
						address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
								address.getLocality(),
								address.getThoroughfare(), address.getFeatureName());
				return addressText;
			} else {
				return "No address found";
			}
		}
	}// AsyncTask class
	private class SearchAsyncTask extends AsyncTask<Location, Void, List<Restaurant>>{

		@Override
		protected List<Restaurant> doInBackground(Location... arg0) {
			Location loc = arg0[0];
			try {
				String url = "https://maps.googleapis.com/maps/api/place/search/json?";
				List<Restaurant> list = new ConnectServer().
						getNearbyRest(url, loc.getLatitude(), loc.getLongitude());
				if(list.size() != 0) {
					return list;
				}
			} catch (Exception e) {
				Log.d("SearchAsyncTask ",e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<Restaurant> result) {
			super.onPostExecute(result);
			mRestList = result;
			if(mRestList != null) {
				drawMarker(new LatLng(mLocation.getLatitude(),  mLocation.getLongitude()), 
						BitmapDescriptorFactory.HUE_GREEN, myTextView.getText().toString());
				for(int i = 0; i < mRestList.size(); ++i) {
					Restaurant rest = mRestList.get(i);	           
					LatLng latLng = new LatLng(rest.getLatitude(), rest.getLongtitude());            		            
					Marker m = drawMarker(latLng, UNDEFINED_COLOR, rest.getName());           
					mHMReference.put(m.getId(), rest);
				}
			}
			else {
				Toast.makeText(getActivity(), "ªÒ»°∏ΩΩ¸≤ÕÃ¸ ß∞‹£¨«ÎºÏ≤ÈÕ¯¬Á ",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private class GetRestInfoAsyncTask extends AsyncTask<String, Void, Restaurant>{
		@Override
		protected Restaurant doInBackground(String... arg0) {
			String restName = arg0[0];
			try {
				Restaurant rest = new ConnectServer().getRestaurantDetail(restName);
				
				return rest;
			} catch (Exception e) {
				Log.d("GetRestInfoAsyncTask ",e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Restaurant rest) {
			super.onPostExecute(rest);
			if(rest != null) {
				Intent intent = new Intent(getActivity(), RestInfoActivity.class);
				Bundle mBundle = new Bundle();  
				mBundle.putSerializable("restInfo",rest);  
				intent.putExtras(mBundle);  
				startActivity(intent);
			}
			else {
				Toast.makeText(getActivity(), "ªÒ»°≤ÕÃ¸œÍ«È ß∞‹ +_+ ",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
}
