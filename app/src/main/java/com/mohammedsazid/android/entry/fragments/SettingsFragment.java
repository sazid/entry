package com.mohammedsazid.android.entry.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.mohammedsazid.android.entry.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
