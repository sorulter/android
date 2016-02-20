package com.iproxier.ipx4andorid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.iproxier.ipx4andorid.base.helpers;
import com.iproxier.ipx4andorid.vpn.core.Constant;
import com.iproxier.ipx4andorid.vpn.core.LocalVpnService;
import java.util.Calendar;

public class DashboardActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener,
        LocalVpnService.onStatusChangedListener {

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private static final int START_VPN_SERVICE_REQUEST_CODE = 10086;
    private static String GL_HISTORY_LOGS;
    private Switch switchProxy;
    private TextView textViewLog;
    private ScrollView scrollViewLog;
    private Calendar mCalendar;
    private static String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scrollViewLog = (ScrollView) findViewById(R.id.scrollViewLog);
        textViewLog = (TextView) findViewById(R.id.textViewLog);

        textViewLog.setText(GL_HISTORY_LOGS);
        scrollViewLog.fullScroll(ScrollView.FOCUS_DOWN);

        mCalendar = Calendar.getInstance();
        LocalVpnService.addOnStatusChangedListener(this);

        token = getIpxApiToken();
        if (token.equals("")) {
            showLogin();
        } else {
            onLogReceived("Welcome!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onLogReceived(String logString) {
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        logString = String.format("[%1$02d:%2$02d:%3$02d] %4$s\n",
                mCalendar.get(Calendar.HOUR_OF_DAY),
                mCalendar.get(Calendar.MINUTE),
                mCalendar.get(Calendar.SECOND),
                logString);

        Log.d(Constant.TAG, logString);

        if (textViewLog.getLineCount() > 200) {
            textViewLog.setText("");
        }
        textViewLog.append(logString);
        scrollViewLog.fullScroll(ScrollView.FOCUS_DOWN);
        GL_HISTORY_LOGS = textViewLog.getText() == null ? "" : textViewLog.getText().toString();
    }

    @Override
    public void onStatusChanged(String status, Boolean isRunning) {
       switchProxy.setEnabled(true);
       switchProxy.setChecked(isRunning);
        Toast.makeText(this, status, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (LocalVpnService.IsRunning != isChecked) {
            switchProxy.setEnabled(false);

            if (isChecked) {
                Intent intent = LocalVpnService.prepare(this);
                if (intent == null) {
                    startVPNService();
                } else {
                    startActivityForResult(intent, START_VPN_SERVICE_REQUEST_CODE);
                }
            } else {
                LocalVpnService.IsRunning = false;
            }
        }
    }

    private void startVPNService() {
        textViewLog.setText("");
        GL_HISTORY_LOGS = null;
        onLogReceived("starting...");
        startService(new Intent(this, LocalVpnService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == START_VPN_SERVICE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                startVPNService();
            } else {
                switchProxy.setChecked(false);
                switchProxy.setEnabled(true);
                onLogReceived("canceled.");
            }
            return;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_switch);
        if (menuItem == null) {
            return false;
        }

        switchProxy = (Switch) menuItem.getActionView();
        if (switchProxy == null) {
            return false;
        }

        switchProxy.setChecked(LocalVpnService.IsRunning);
        switchProxy.setOnCheckedChangeListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_about:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.app_name) + " " + helpers.getVersionName(getApplicationContext()))
                        .setMessage(R.string.about_info)
                        .setPositiveButton(R.string.btn_ok, null)
                        .show();
                return true;
            case R.id.menu_item_logout:
                logout();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        LocalVpnService.removeOnStatusChangedListener(this);
        super.onDestroy();
    }

    private void logout() {
        delIpxApiToken();
        LocalVpnService.removeOnStatusChangedListener(this);
        showLogin();
    }

    private void showLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private String getIpxApiToken() {
        SharedPreferences sharedPref = this.getSharedPreferences("IPX_API_TOKEN", Context.MODE_PRIVATE);
        String token = sharedPref.getString("com.iproxier.ipx.IPX_API_TOKEN", "");
        Log.d(Constant.TAG, "token: "+token);
        return token;
    }

    private void delIpxApiToken() {
        SharedPreferences tokenSharedPref = DashboardActivity.this.getSharedPreferences("IPX_API_TOKEN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tokenSharedPref.edit();
        editor.putString("com.iproxier.ipx.IPX_API_TOKEN", "");
        editor.apply();
    }

}
