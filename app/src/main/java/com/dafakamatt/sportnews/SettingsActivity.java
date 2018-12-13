package com.dafakamatt.sportnews;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Loading fragment into the content view:
        setContentView(R.layout.activity_settings);
    }

    public static class ArticlePreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Inflating our settings_main.xml, which contains our preferences layout
            addPreferencesFromResource(R.xml.settings_main);
            // Finding our existing preferences in the system using the strings we setup:
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            // When the preference is changed, set the summary (underneath the preference label) to
            // show the value so when the user clicks it the grammar is better and is not specifically
            // the value in the API query.
            String stringValue = value.toString();

            Log.i("SettingsActivity","StringValue == " + stringValue);
            // Changing the value the user sees to be a little user friendly
            String summary;
            if (value.toString().equals("newest")) {
                summary = getString(R.string.settings_order_by_date_label);
            }
            else {
                summary = getString(R.string.settings_order_by_relevance_label);
            }

            preference.setSummary(summary);
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(),"");
            onPreferenceChange(preference,preferenceString);
        }
    }
}
