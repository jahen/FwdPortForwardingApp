/*
 * Fwd: the port forwarding app
 * Copyright (C) 2016  Elixsr Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.elixsr.portforwarder.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.elixsr.portforwarder.R;
import com.elixsr.portforwarder.forwarding.ForwardingService;
import com.elixsr.portforwarder.ui.preferences.SettingsFragment;

/**
 * Created by Niall McShane on 28/02/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    private ThemeChangeReceiver themeChangeReceiver;

    @Override
    protected void onCreate(Bundle ofJoy) {

        // Handle intents
        IntentFilter themeChangeIntentFilter = new IntentFilter(
                SettingsFragment.DARK_MODE_BROADCAST);

        themeChangeReceiver =
                new ThemeChangeReceiver();

        // Registers the ForwardingServiceResponseReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                themeChangeReceiver,
                themeChangeIntentFilter);

        applyThemeFromPreference();
        super.onCreate(ofJoy);
    }

    @Override
    protected void onResume() {
        applyThemeFromPreference();
        super.onResume();
    }

    private void applyThemeFromPreference() {
        String mode = getDarkThemeMode();
        boolean useDark = false;
        if ("dark".equals(mode)) {
            useDark = true;
        } else if ("light".equals(mode)) {
            useDark = false;
        } else {
            // follow_system
            int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            useDark = (nightModeFlags == Configuration.UI_MODE_NIGHT_YES);
        }
        setTheme(useDark ? R.style.DarkTheme_NoActionBar : R.style.AppTheme_NoActionBar);
    }

    private String getDarkThemeMode() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            String mode = prefs.getString("pref_dark_theme", "follow_system");
            if (mode.equals("follow_system") || mode.equals("dark") || mode.equals("light")) {
                return mode;
            }
            if (mode.equals("on") || mode.equals("off")) {
                // Migrate old values
                String newMode = mode.equals("on") ? "dark" : "follow_system";
                prefs.edit().putString("pref_dark_theme", newMode).apply();
                return newMode;
            }
        } catch (ClassCastException ignored) {
            // Old CheckBoxPreference stored boolean, migrate to new format
            boolean oldValue = prefs.getBoolean("pref_dark_theme", false);
            String mode = oldValue ? "dark" : "follow_system";
            prefs.edit().remove("pref_dark_theme").putString("pref_dark_theme", mode).apply();
            return mode;
        }
        return "follow_system";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy: CALLED");

        // Ensure that it is no longer looking out for broadcasts
        LocalBroadcastManager.getInstance(this).unregisterReceiver(themeChangeReceiver);
    }

    // Primary toolbar and drawer toggle
    private Toolbar mActionBarToolbar;

    protected Toolbar getActionBarToolbar() {

        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                // Depending on which version of Android you are on the Toolbar or the ActionBar may be
                // active so the a11y description is set here.
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }

    // Broadcast receiver for receiving status updates from the IntentService
    private class ThemeChangeReceiver extends BroadcastReceiver {
        // Prevents instantiation
        private ThemeChangeReceiver() {
        }

        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: style changed");
            recreate();
        }
    }


}
