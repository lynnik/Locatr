package com.lynnik.locatr;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class LocatrFragment extends Fragment {

  private static final String TAG = "LocatrFragment";

  private ImageView mImageView;
  private GoogleApiClient mClient;

  public static LocatrFragment newInstance() {
    return new LocatrFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    mClient = new GoogleApiClient.Builder(getActivity())
        .addApi(LocationServices.API)
        .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
          @Override
          public void onConnected(@Nullable Bundle bundle) {
            getActivity().invalidateOptionsMenu();
          }

          @Override
          public void onConnectionSuspended(int i) {

          }
        })
        .build();
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_locatr, container, false);

    mImageView = (ImageView) v.findViewById(R.id.image);

    return v;
  }

  @Override
  public void onStart() {
    super.onStart();
    getActivity().invalidateOptionsMenu();
    mClient.connect();
  }

  @Override
  public void onStop() {
    super.onStop();
    mClient.disconnect();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_locatr, menu);

    MenuItem searchItem = menu.findItem(R.id.action_locate);
    searchItem.setEnabled(mClient.isConnected());
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_locate:
        findImage();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void findImage() {
    LocationRequest request = LocationRequest.create();
    request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    request.setNumUpdates(1);
    request.setInterval(0);

    if (ActivityCompat.checkSelfPermission(
        getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(
        getActivity(),
        android.Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    LocationServices.FusedLocationApi
        .requestLocationUpdates(mClient, request, new LocationListener() {
          @Override
          public void onLocationChanged(Location location) {
            Log.i(TAG, "Got a fix: " + location);
          }
        });
  }
}