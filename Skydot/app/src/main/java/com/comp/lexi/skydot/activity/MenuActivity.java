package com.comp.lexi.skydot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.comp.lexi.skydot.R;
import com.comp.lexi.skydot.base.BaseActivity;
import com.comp.lexi.skydot.base.BaseFragment;
import com.comp.lexi.skydot.database.UserDataStore;
import com.comp.lexi.skydot.fragment.BillsFragment;
import com.comp.lexi.skydot.fragment.ProfileFragment;
import com.comp.lexi.skydot.fragment.SettingsFragment;
import com.comp.lexi.skydot.fragment.TransfersFragment;
import com.comp.lexi.skydot.utils.RequestUtil;
import com.comp.lexi.skydot.utils.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BaseFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = new ProfileFragment();
        setTitle(getString(R.string.title_profile));
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragment = null;

        if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
            setTitle(getString(R.string.title_profile));
        } else if (id == R.id.nav_transfers) {
            fragment = new TransfersFragment();
            setTitle(getString(R.string.title_transfers));
        } else if (id == R.id.nav_bill_payment) {
            fragment = new BillsFragment();
            setTitle(getString(R.string.title_bill_payment));
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
            setTitle(getString(R.string.title_settings));

        } else if (id == R.id.nav_logout) {
            logout();
            return false;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setProfileInfo(final String p) {
        UserDataStore dataStore = UserDataStore.getDataStore();
        dataStore.setUser(p);
        dataStore.saveState(getApplicationContext());
    }

    private void logout() {
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        RequestUtil.getInstance(this).logout(new VolleyCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.has(getString(R.string.ERROR))) {
                    try {
                        showError(result.getString(getString(R.string.MESSAGE)), result.getString(getString(R.string.ERROR)));
                    } catch (JSONException je) {
                        showError("Json parse failed.");
                    }
                }
            }

            @Override
            public void onSuccess(JSONArray result) {
                // do nothing
            }

            @Override
            public void onSuccess(String result) {
                // do nothing
            }

            @Override
            public void onFailure(String result) {
                showError(result);
            }
        });
    }

}