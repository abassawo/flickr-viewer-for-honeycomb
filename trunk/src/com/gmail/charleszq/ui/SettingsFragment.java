/**
 * 
 */

package com.gmail.charleszq.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.gmail.charleszq.FlickrViewerActivity;
import com.gmail.charleszq.FlickrViewerApplication;
import com.gmail.charleszq.R;
import com.gmail.charleszq.utils.Constants;
import com.gmail.charleszq.utils.ImageCache;

/**
 * Represents the fragment for the setting page of whole application.
 * 
 * @author charles
 */
public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {

	private static final String TAG = SettingsFragment.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceManager pm = this.getPreferenceManager();
		pm.setSharedPreferencesName(Constants.DEF_PREF_NAME);
		pm.setSharedPreferencesMode(Context.MODE_PRIVATE);

		this.addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	public void onStart() {
		super.onStart();
		PreferenceManager pm = getPreferenceManager();
		SharedPreferences sp = pm.getSharedPreferences();
		Log.d(TAG, "Preference name: " + pm.getSharedPreferencesName()); //$NON-NLS-1$
		sp.registerOnSharedPreferenceChangeListener(this);

		FlickrViewerActivity act = (FlickrViewerActivity) getActivity();
		act.changeActionBarTitle(null);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (Constants.PHOTO_LIST_CACHE_SIZE.equals(key)) {
			String size = sharedPreferences.getString(key, String
					.valueOf(Constants.DEF_CACHE_SIZE));
			ImageCache.CACHE_SIZE = Integer.parseInt(size);
			Log.d(TAG, "Cache size changed: " + size); //$NON-NLS-1$
			return;
		}

		FlickrViewerActivity act = (FlickrViewerActivity) getActivity();
		FlickrViewerApplication app = (FlickrViewerApplication) act.getApplication();
		if (Constants.ENABLE_CONTACT_UPLOAD_NOTIF.equals(key)
				|| Constants.NOTIF_CONTACT_UPLOAD_INTERVAL.equals(key)) {
			app.handleContactUploadService();
			return;
		}

		if (Constants.ENABLE_PHOTO_ACT_NOTIF.equals(key)
				|| Constants.NOTIF_PHOTO_ACT_INTERVAL.equals(key)) {
			app.handlePhotoActivityService();
			return;
		}
	}

	@Override
	public void onStop() {
		SharedPreferences sp = this.getPreferenceManager()
				.getSharedPreferences();
		sp.unregisterOnSharedPreferenceChangeListener(this);
		super.onStop();
	}

}