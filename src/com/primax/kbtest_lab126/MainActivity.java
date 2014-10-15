package com.primax.kbtest_lab126;


import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.bluetooth.BluetoothInputDevice;

public class MainActivity extends Activity{
	
//	static final boolean isDebug = true;
	static final boolean isDebug = false;
	static final int PASSWORD = 7890;

	static final int FIRST_ROW_BASE = 0;
	static final int SECOND_ROW_BASE = 20;
	static final int THIRD_ROW_BASE = 40;
	static final int FORTH_ROW_BASE = 60;
	static final int FIFTH_ROW_BASE = 80;
	static final int SIXTH_ROW_BASE = 100;
	static final int TEST_TRACKPAD_BASE = 120;
	static final int TEST_COMPLETE = 200;
	
	static final int MSG_DISMISS_PROGRESS_DIALOG = 0;
	static final int MSG_RESET_MOTION_EVENT_COUNT = 1;
	static final int MSG_TEST_NEXT_STEP = 2;
	static final int MSG_TEST_COMPLETE = 3;
	static final int MSG_DETECT_ACTION_DOWN = 4;
	static final int MSG_DETECT_ACTION_SCROLL = 5;
	static final int MSG_DRAW_LINE = 6;

	static final int color_red = 1;
	static final int color_green = 2;
	static final int color_blue = 3;
	static final int color_black = 4;
	static final int color_orange = 5;
	
	final int REQUEST_ENABLE_BT = 1;
	
    static final int BOND_NONE = 10;
    static final int BOND_BONDING = 11;
    static final int BOND_BONDED = 12;
    
    static final int STAGE_PAIRING_TEST = 1;
    static final int STAGE_KEY_TEST = 2;
    static final int STAGE_TRACKPAD_TEST = 3;

    static final int ORANGE = 0xFFFF3300;
	
	BluetoothAdapter btAdapter;
	public static BluetoothDevice btDevice;
	public static boolean isTryDisconnect = false;
	public boolean isReportRateRunning = false;
	
	// instance of BluetoothInputDevice
	// need this to use "setReport"
	public static BluetoothInputDevice btInputDevice;
	
	int testStep;
	int wrongPressedkeyNum;
	boolean isCompleteTest;
	boolean isTestNextRow;
	boolean isCapsLEDOn;
	
	// SharedPreferences instance
	public static SharedPreferences sharedPrefs;
	public static SharedPreferences.Editor sharedPrefsEditor;
	public static String btDeviceMACAddress;
	public static String layoutLanguage;
	public static int testStage; 
	
	// ProgressDialog, wait bluetooth behave finish
	public ProgressDialog mProgressDialog;
	
	static SimpleView drawView = null;
	
	public int motionEventCount;
	Message m;
	
	public int setReportRate;
	public int setReportRateRange;
	public int setReportRateHoldSeconds;
	public int HoldCounter;
	public int clickCounter;
	
	public static Path new_path;
	public static float mCalibration_dx = (float) 0.0;
	public static float mCalibration_dy = (float) 0.0;
	public static int mSelectedPaintColor = Color.rgb(0, 0, 0);
	public static int mSelectedStrokeWidth = 1;
	public static float mOldX;
	public static float mOldY;
	public static float mNewX;
	public static float mNewY;
	
	// TextView vars =========================
	// first row
	TextView tv_firstRow01;
	TextView tv_firstRow02;
	TextView tv_firstRow03;
	TextView tv_firstRow04;
	TextView tv_firstRow05;
	TextView tv_firstRow06;
	TextView tv_firstRow07;
	TextView tv_firstRow08;
	TextView tv_firstRow09;
	TextView tv_firstRow10;
	TextView tv_firstRow11;
	TextView tv_firstRow12;
	TextView tv_firstRow13;
	TextView tv_firstRow14;

	// second row
	TextView tv_1;
	TextView tv_2;
	TextView tv_3;
	TextView tv_4;
	TextView tv_5;
	TextView tv_6;
	TextView tv_7;
	TextView tv_8;
	TextView tv_9;
	TextView tv_0;
	TextView tv_minus;
	TextView tv_bs;

	// third row
	TextView tv_tab;
	TextView tv_q;
	TextView tv_w;
	TextView tv_e;
	TextView tv_r;
	TextView tv_t;
	TextView tv_y;
	TextView tv_u;
	TextView tv_i;
	TextView tv_o;
	TextView tv_p;
	TextView tv_equal;
	TextView tv_backslash;
	
	// forth row 
	TextView tv_caps;
	TextView tv_a;
	TextView tv_s;
	TextView tv_d;
	TextView tv_f;
	TextView tv_g;
	TextView tv_h;
	TextView tv_j;
	TextView tv_k;
	TextView tv_l;
	TextView tv_semicolon;
	TextView tv_enter;
	
	// fifth row
	TextView tv_lshift;
	TextView tv_z;
	TextView tv_x;
	TextView tv_c;
	TextView tv_v;
	TextView tv_b;
	TextView tv_n;
	TextView tv_m;
	TextView tv_comma;
	TextView tv_period;
	TextView tv_slash;
	TextView tv_rshift;
	
	// sixth row 
	TextView tv_ctl;
	TextView tv_fn;
	TextView tv_alt;
	TextView tv_space;
	TextView tv_apostrophe;
	TextView tv_lbracket;
	TextView tv_rbracket;
	TextView tv_arrow_up;
	TextView tv_arrow_down;
	TextView tv_arrow_left;
	TextView tv_arrow_right;
	
	// hint & result
	TextView tv_test_hint;
	TextView tv_pairing_test_status;
	TextView tv_key_test_status;
	TextView tv_caps_test_status;
	TextView tv_trackpad_test_status;
	TextView tv_trackpad_reportrate;
	TextView tv_trackpad_reportrate_status;
	TextView tv_trackpad_click;
	TextView tv_trackpad_click_status;
	TextView tv_trackpad_scroll;
	TextView tv_trackpad_scroll_status;
	TextView tv_test_result0;
	TextView tv_test_result;
	
	// TextView vars =========================
	
	View v;
	RelativeLayout rl;

	public BroadcastReceiver searchDevices = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (isDebug) {
				Log.e("gray", "MainActivity.java:onReceive, action:" + action);
			}
			MainActivity.btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			
			if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
				
				if (isDebug) {
					Log.e("gray", "MainActivity.java:ACTION_DISCOVERY_FINISHED, " + "");
				}
				
			} else if (action.equals(BluetoothDevice.ACTION_FOUND)) {
				
				if (isDebug) {
					Log.e("gray", "MainActivity.java:ACTION_FOUND, btDevice.getAddress():" + btDevice.getAddress());
				}

				if ( btDeviceMACAddress.equalsIgnoreCase( btDevice.getAddress()) ) {
					
					if (isDebug) {
						Log.e("gray", "MainActivity.java:MAC address match , " + "");
					}
					btAdapter.cancelDiscovery();
					ClsUtils.pair(btDevice.getAddress(), "0000", btAdapter);
				}
				
			} else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
				
				if (isDebug) {
					Log.e("gray", "MainActivity.java:ACTION_BOND_STATE_CHANGED, " + "");
				}
				switch (btDevice.getBondState()) {
					case BOND_BONDING:
						
						if (isDebug) {
							Log.e("gray", "MainActivity.java:BOND_BONDING, " + "");
						}
						break;
					case BOND_BONDED:
						if (isDebug) {
							Log.e("gray", "MainActivity.java:BOND_BONDED, " + "");
						}
						btAdapter.getProfileProxy(context, new InputDeviceServiceListener(), BluetoothProfile.INPUT_DEVICE);
						break;
					case BluetoothDevice.BOND_NONE:

						if (isDebug) {
							Log.e("gray", "MainActivity.java:BOND_NONE, " + "");
						}
						break;
				}
			} else if (action.equals(BluetoothInputDevice.ACTION_CONNECTION_STATE_CHANGED)) {
				if (isDebug) {
					Log.e("gray", "MainActivity.java:ACTION_CONNECTION_STATE_CHANGED, " + "");
				}
				if (isDebug) {
					Log.e("gray", "btDevice.getAddress():" + btDevice.getAddress());
				}
				
			}
		}
	};
	
	private final class InputDeviceServiceListener implements BluetoothProfile.ServiceListener {
		
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			
			if (isDebug) {
				Log.e("gray", "MainActivity.java:onServiceConnected, " + "");
			}
			MainActivity.btInputDevice = (BluetoothInputDevice) proxy;
			
			if (!isTryDisconnect) {
				
				if (MainActivity.btInputDevice != null) {
					
					if (testStage == STAGE_PAIRING_TEST) {
						MainActivity.btInputDevice.connect(MainActivity.btDevice);
						testStage = STAGE_KEY_TEST;	// pairing ok, to next stage (test key)
						
						sharedPrefsEditor.putString("pref_btDeviceMACAddress", btDeviceMACAddress);
						sharedPrefsEditor.putInt("pref_testStage", testStage);
						sharedPrefsEditor.commit();
						
						handler.sendEmptyMessage(MSG_DISMISS_PROGRESS_DIALOG);
					}
				}
				
			} else {
				
				if (isDebug) {
					Log.e("gray", "MainActivity.java:onServiceConnected, " + MainActivity.btDevice.getAddress());
					Log.e("gray", "MainActivity.java:onServiceConnected, " + btDevice.getAddress());
				}
				
				if (MainActivity.btInputDevice != null) {
					MainActivity.btInputDevice.disconnect(MainActivity.btDevice);
				}
				
			}
		}
		
		public void onServiceDisconnected(int profile) {
			
			if (isDebug) {
				Log.e("gray", "MainActivity.java:onServiceDisconnected, " + "");
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (isDebug) {
			Log.e("gray", "MainActivity.java:onCreate, " + "");
		}
		
		// get SharedPreferences instance
		if (sharedPrefs == null) {
			sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		}
		if (sharedPrefsEditor == null) {
			sharedPrefsEditor = sharedPrefs.edit();  
		}
		
//		btDeviceMACAddress = "F0:65:DD:63:C9:CE"; // amazon
//		btDeviceMACAddress = "99:28:27:26:25:24"; // mozart
//		btDeviceMACAddress = "00:02:76:4C:E8:7B"; // belkin
		btDeviceMACAddress = sharedPrefs.getString("pref_btDeviceMACAddress", "F0:65:DD:63:C9:CE");
		testStage = sharedPrefs.getInt("pref_testStage", STAGE_PAIRING_TEST);

		try {
			setReportRate = Integer.valueOf(sharedPrefs.getString("pref_set_reportrate", "80"));
		} catch (Exception e) {
			Log.e("gray", "MainActivity.java: onCreate, Exception : " + e.toString() );
		}
		try {
			setReportRateRange = Integer.valueOf(sharedPrefs.getString("pref_set_reportrate_range", "30"));
		} catch (Exception e) {
			Log.e("gray", "MainActivity.java: onCreate, Exception : " + e.toString() );
		}
		try {
			setReportRateHoldSeconds = Integer.valueOf(sharedPrefs.getString("pref_set_reportrate_hold_seconds", "5"));
		} catch (Exception e) {
			Log.e("gray", "MainActivity.java: onCreate, Exception : " + e.toString() );
		}
		layoutLanguage = sharedPrefs.getString("pref_layout_language", "US");
		
		if (isDebug) {
			Log.e("gray", "MainActivity.java:onCreate, btDeviceMACAddress:" + btDeviceMACAddress);
			Log.e("gray", "MainActivity.java:onCreate, testStage:" + testStage);
			Log.e("gray", "MainActivity.java:onActivityResult, setReportRate:" + setReportRate);
			Log.e("gray", "MainActivity.java:onActivityResult, setReportRateRange:" + setReportRateRange);
			Log.e("gray", "MainActivity.java:onActivityResult, setReportRateHoldSeconds:" + setReportRateHoldSeconds);
			Log.e("gray", "MainActivity.java:onActivityResult, layoutLanguage:" + layoutLanguage);
		}
		
		// initial variables
		init();
		
		// set broadcastreveiver
		IntentFilter intent = new IntentFilter();
		intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		intent.addAction(BluetoothDevice.ACTION_FOUND);
		intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intent.addAction(BluetoothInputDevice.ACTION_CONNECTION_STATE_CHANGED);
		registerReceiver(searchDevices, intent);
		
		// get BT adapter
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null) {
			Log.e("gray", "MainActivity.java:onCreate, " + "Device does not support Bluetooth.");
		}
		
		// if BT close, open it
		if (!btAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}

		EditText et = (EditText) findViewById(R.id.et_bda);
		et.setText(btDeviceMACAddress);
		et.setSelection(et.length());

		btDevice = btAdapter.getRemoteDevice(btDeviceMACAddress);
		
		// pair button
		final Button button_pair = (Button) findViewById(R.id.button_pair);
		button_pair.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				if (isDebug) {
					Log.e("gray", "MainActivity.java:onClick, " + "pair button");
				}
				
				if (testStage == STAGE_PAIRING_TEST) {
					
					EditText et = (EditText) findViewById(R.id.et_bda);
					et.setFocusable(false);
					btDeviceMACAddress = et.getText().toString().toUpperCase();
					btDevice = btAdapter.getRemoteDevice(btDeviceMACAddress);
					
					// start to discovery & pair device
					showProcessDialog("Info", "Bluetooth connecting...");
					btAdapter.startDiscovery();
				} else {
					if (isDebug) {
						Log.e("gray", "MainActivity.java:onClick, testStep:" + testStep);
						Log.e("gray", "MainActivity.java:onClick, HoldCounter:" + HoldCounter);
					}
				}
			}
		});
		
		// handle "enter" key event
		button_pair.setOnKeyListener(new OnKeyListener()
		{
		    public boolean onKey(View v, int keyCode, KeyEvent event)
		    {
		        if (isDebug) {
					Log.e("gray", "MainActivity.java:onKey, " + event.toString());
				}
		        
		        if (event.getAction() == KeyEvent.ACTION_DOWN) {
		        	
		        	 if (keyCode == KeyEvent.KEYCODE_ENTER) {
		     			setKeyColor(tv_enter, color_red);
		        	 }
					
				} else if (event.getAction() == KeyEvent.ACTION_UP) {
					
					if (keyCode == KeyEvent.KEYCODE_ENTER) {
						keyUpCheck(tv_enter, FORTH_ROW_BASE+11);
					}
				}
		        
		        return false;
		    }
		});
		
		// test button
		final Button button_unpair = (Button) findViewById(R.id.button_unpair);
		button_unpair.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				if (isDebug) {
					Log.e("gray", "MainActivity.java:onClick, " + "test button");
				}
				
				showProcessDialog("Info", "Bluetooth disconnecting...");
				
				EditText et = (EditText) findViewById(R.id.et_bda);
				btDeviceMACAddress = et.getText().toString().toUpperCase();
				btDevice = btAdapter.getRemoteDevice(btDeviceMACAddress);
				et.requestFocus();
				et.setFocusable(true);
				et.setSelection(et.length());
				
				ClsUtils.unpairDevice(btDevice);
				
				// restart test
				sharedPrefsEditor.putString("pref_btDeviceMACAddress", btDeviceMACAddress);
				sharedPrefsEditor.putInt("pref_testStage", STAGE_PAIRING_TEST);
				sharedPrefsEditor.commit();
				reStartTest();
				
				startTest();
				
				// show soft keyboard
//				InputMethodManager inputManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
//				inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				
			}
		});
		
		final Button button_clear_map = (Button) findViewById(R.id.button_clear_map);
		button_clear_map.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (drawView != null) {
					drawView.ClearPathView();
				}
			}
		});
		
		final Button button_exit_app = (Button) findViewById(R.id.button_exit_app);
		button_exit_app.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		HoldCounter = setReportRateHoldSeconds;
		// report rate thread
		new Thread(new Runnable() 
		{ 
			@Override
			public void run() 
			{ 
				while (true) {
					
					m = new Message();
					m.what = MSG_RESET_MOTION_EVENT_COUNT;
					m.obj = Integer.toString(motionEventCount);
					handler.sendMessage(m);
					
					if (testStep == TEST_TRACKPAD_BASE) {
							
						if (motionEventCount <= setReportRate+setReportRateRange && motionEventCount >= setReportRate-setReportRateRange) {
							HoldCounter--;
						} else {
							HoldCounter = setReportRateHoldSeconds;
						}
						
						if (HoldCounter <= -1) {
							// report rate test pass
							HoldCounter = setReportRateHoldSeconds;
							handler.sendEmptyMessage(MSG_TEST_NEXT_STEP);
						}
					}
					
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			} 
		}).start();
		
		rl = (RelativeLayout) findViewById(R.id.rootlayout);
		rl.setOnGenericMotionListener(new OnGenericMotionListener() {

			@Override
			public boolean onGenericMotion(View v, MotionEvent event) {
				
				if (isDebug) {
					Log.e("gray", "root view:onGenericMotion, " + event.toString());
				}
				
				switch (event.getAction()) {
				
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_HOVER_MOVE:
					motionEventCount++;
					for (int i = 0; i < event.getHistorySize(); i++) {
						motionEventCount++;
					}
					
					break;
					
				case MotionEvent.ACTION_HOVER_ENTER:
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_HOVER_EXIT:
					break;
				case MotionEvent.ACTION_UP:
					break;
				case MotionEvent.ACTION_SCROLL:
					
					handler.sendEmptyMessage(MSG_DETECT_ACTION_SCROLL);
					
					// trackpad click test pass
					if (testStep == TEST_TRACKPAD_BASE+2) {
						handler.sendEmptyMessage(MSG_TEST_COMPLETE);
					}
					break;
					
				}
				return false;
			}
		});
		
		rl.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
//				if (isDebug) {
//					Log.e("gray", "MainA ctivity.java:setOnTouchListener, " + event.toString());
//				}
				
				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN:
					
					if (isDebug) {
						Log.e("gray", "onTouch, MotionEvent.ACTION_DOWN, " + event.toString());
					}
					
					handler.sendEmptyMessage(MSG_DETECT_ACTION_DOWN);
					
					// trackpad click test pass
					if (testStep == TEST_TRACKPAD_BASE+1) {
						clickCounter++;
						if (clickCounter >= 3) {
							handler.sendEmptyMessage(MSG_TEST_NEXT_STEP);
						}
					}
					
					break;
				}
				return false;
			}
		});
		
		new_path = new Path();
		drawView = (SimpleView) findViewById(R.id.mouse_path_view);
		drawView.setBackgroundColor(0xffd9d9d9);
//		drawView.setBackgroundColor(Color.argb(255, 255, 255, 255));
		drawView.setOnGenericMotionListener(new OnGenericMotionListener() {

			@Override
			public boolean onGenericMotion(View v, MotionEvent event) {
				
				if (isDebug) {
					Log.e("gray", "drawView:onGenericMotion, " + event.toString());
				}
				
				switch (event.getAction()) {
				
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_HOVER_ENTER:

					mOldX = event.getX();
					mOldY = event.getY();

					if (new_path != null && !new_path.isEmpty()) {
						new_path.reset();
					} else {
						new_path = new Path();
					}
					break;
					
				case MotionEvent.ACTION_HOVER_MOVE:
				case MotionEvent.ACTION_MOVE:
					
					for (int i = 0; i < event.getHistorySize(); i++) {
						mNewX = event.getHistoricalX(i);
						mNewY = event.getHistoricalY(i);
						new_path.moveTo(mOldX - mCalibration_dx, mOldY
								- mCalibration_dy);
						new_path.lineTo(mNewX - mCalibration_dx, mNewY
								- mCalibration_dy);
						drawView.DrawLine(new_path, mSelectedPaintColor,
								mSelectedStrokeWidth);
						new_path.reset();
						mOldX = mNewX;
						mOldY = mNewY;

//						Message msg = handler.obtainMessage();
//						msg.what = MSG_DRAW_LINE;
//						Bundle data = new Bundle();
//						data.putString("event_x", String.valueOf(mNewX));
//						data.putString("event_y", String.valueOf(mNewY));
//						// data.putString("event_z", String.valueOf(event.getZ()));
//						msg.setData(data);
//						msg.sendToTarget();

					}
					
					mNewX = event.getX();
					mNewY = event.getY();
					new_path.moveTo(mOldX - mCalibration_dx, mOldY
							- mCalibration_dy);
					new_path.lineTo(mNewX - mCalibration_dx, mNewY
							- mCalibration_dy);
					drawView.DrawLine(new_path, mSelectedPaintColor,
							mSelectedStrokeWidth);
					new_path.reset();
					mOldX = mNewX;
					mOldY = mNewY;

//					Message msg = handler.obtainMessage();
//					msg.what = MSG_DRAW_LINE;
//					Bundle data = new Bundle();
//					data.putString("event_x", String.valueOf(mNewX));
//					data.putString("event_y", String.valueOf(mNewY));
//					// data.putString("event_z", String.valueOf(event.getZ()));
//					msg.setData(data);
//					msg.sendToTarget();
					
					break;
					
				case MotionEvent.ACTION_HOVER_EXIT:
				case MotionEvent.ACTION_UP:
					
					if (new_path != null && !new_path.isEmpty()) {
						new_path.reset();
					}
					mOldX = (float) 0.0;
					mOldY = (float) 0.0;
					mNewX = (float) 0.0;
					mNewY = (float) 0.0;
					break;
					
				case MotionEvent.ACTION_SCROLL:
					break;
				}
				
				return false;
			}
		});
		
		// start Test
		if (testStage == STAGE_KEY_TEST) {
			
			EditText et2 = (EditText) findViewById(R.id.et_bda);
			et2.setFocusable(false);
			
			tv_pairing_test_status.setText(": PASS");
			tv_pairing_test_status.setTextColor(Color.GREEN);
			
			btAdapter.getProfileProxy(getApplicationContext(), new InputDeviceServiceListener(), BluetoothProfile.INPUT_DEVICE);
			
			startTest();
		}
	}
	
	public void init(){
		
		testStep = 0;
		wrongPressedkeyNum = -1;
		isCompleteTest = false;
		isTestNextRow = false;
		clickCounter = 0;

		// init TextView vars ======================
		// first row
		tv_firstRow01 = (TextView) findViewById(R.id.tv_firstRow01);
		tv_firstRow02 = (TextView) findViewById(R.id.tv_firstRow02);
		tv_firstRow03 = (TextView) findViewById(R.id.tv_firstRow03);
		tv_firstRow04 = (TextView) findViewById(R.id.tv_firstRow04);
		tv_firstRow05 = (TextView) findViewById(R.id.tv_firstRow05);
		tv_firstRow06 = (TextView) findViewById(R.id.tv_firstRow06);
		tv_firstRow07 = (TextView) findViewById(R.id.tv_firstRow07);
		tv_firstRow08 = (TextView) findViewById(R.id.tv_firstRow08);
		tv_firstRow09 = (TextView) findViewById(R.id.tv_firstRow09);
		tv_firstRow10 = (TextView) findViewById(R.id.tv_firstRow10);
		tv_firstRow11 = (TextView) findViewById(R.id.tv_firstRow11);
		tv_firstRow12 = (TextView) findViewById(R.id.tv_firstRow12);
		tv_firstRow13 = (TextView) findViewById(R.id.tv_firstRow13);
		tv_firstRow14 = (TextView) findViewById(R.id.tv_firstRow14);

		// second row
		tv_1 = (TextView) findViewById(R.id.tv_1);
		tv_2 = (TextView) findViewById(R.id.tv_2);
		tv_3 = (TextView) findViewById(R.id.tv_3);
		tv_4 = (TextView) findViewById(R.id.tv_4);
		tv_5 = (TextView) findViewById(R.id.tv_5);
		tv_6 = (TextView) findViewById(R.id.tv_6);
		tv_7 = (TextView) findViewById(R.id.tv_7);
		tv_8 = (TextView) findViewById(R.id.tv_8);
		tv_9 = (TextView) findViewById(R.id.tv_9);
		tv_0 = (TextView) findViewById(R.id.tv_0);
		tv_minus = (TextView) findViewById(R.id.tv_minus);
		tv_bs = (TextView) findViewById(R.id.tv_bs);

		// third row
		tv_tab = (TextView) findViewById(R.id.tv_tab);
		tv_q = (TextView) findViewById(R.id.tv_q);
		tv_w = (TextView) findViewById(R.id.tv_w);
		tv_e = (TextView) findViewById(R.id.tv_e);
		tv_r = (TextView) findViewById(R.id.tv_r);
		tv_t = (TextView) findViewById(R.id.tv_t);
		tv_y = (TextView) findViewById(R.id.tv_y);
		tv_u = (TextView) findViewById(R.id.tv_u);
		tv_i = (TextView) findViewById(R.id.tv_i);
		tv_o = (TextView) findViewById(R.id.tv_o);
		tv_p = (TextView) findViewById(R.id.tv_p);
		tv_equal = (TextView) findViewById(R.id.tv_equal);
		tv_backslash = (TextView) findViewById(R.id.tv_backslash);
		if (layoutLanguage.equalsIgnoreCase("UK")) {
			tv_backslash.setText(" # ");
		} else {
			tv_backslash.setText(" \\ ");
		}
		
		// forth row 
		tv_caps = (TextView) findViewById(R.id.tv_caps);
		tv_a = (TextView) findViewById(R.id.tv_a);
		tv_s = (TextView) findViewById(R.id.tv_s);
		tv_d = (TextView) findViewById(R.id.tv_d);
		tv_f = (TextView) findViewById(R.id.tv_f);
		tv_g = (TextView) findViewById(R.id.tv_g);
		tv_h = (TextView) findViewById(R.id.tv_h);
		tv_j = (TextView) findViewById(R.id.tv_j);
		tv_k = (TextView) findViewById(R.id.tv_k);
		tv_l = (TextView) findViewById(R.id.tv_l);
		tv_apostrophe = (TextView) findViewById(R.id.tv_apostrophe);
		tv_enter = (TextView) findViewById(R.id.tv_enter);
		
		// fifth row
		tv_lshift = (TextView) findViewById(R.id.tv_lshift);
		tv_z = (TextView) findViewById(R.id.tv_z);
		tv_x = (TextView) findViewById(R.id.tv_x);
		tv_c = (TextView) findViewById(R.id.tv_c);
		tv_v = (TextView) findViewById(R.id.tv_v);
		tv_b = (TextView) findViewById(R.id.tv_b);
		tv_n = (TextView) findViewById(R.id.tv_n);
		tv_m = (TextView) findViewById(R.id.tv_m);
		tv_comma = (TextView) findViewById(R.id.tv_comma);
		tv_period = (TextView) findViewById(R.id.tv_period);
		tv_slash = (TextView) findViewById(R.id.tv_slash);
		tv_rshift = (TextView) findViewById(R.id.tv_rshift);
		
		// sixth row 
		tv_ctl = (TextView) findViewById(R.id.tv_ctl);
		tv_fn = (TextView) findViewById(R.id.tv_fn);
		tv_alt = (TextView) findViewById(R.id.tv_alt);
		tv_space = (TextView) findViewById(R.id.tv_space);
		tv_semicolon = (TextView) findViewById(R.id.tv_semicolon);
		tv_lbracket = (TextView) findViewById(R.id.tv_lbracket);
		tv_rbracket = (TextView) findViewById(R.id.tv_rbracket);
		tv_arrow_up = (TextView) findViewById(R.id.tv_arrow_up);
		tv_arrow_down = (TextView) findViewById(R.id.tv_arrow_down);
		tv_arrow_left = (TextView) findViewById(R.id.tv_arrow_left);
		tv_arrow_right = (TextView) findViewById(R.id.tv_arrow_right);
		
		// hint & result
		tv_test_hint = (TextView) findViewById(R.id.tv_test_hint);
		tv_pairing_test_status = (TextView) findViewById(R.id.tv_pairing_test_status);
		tv_key_test_status = (TextView) findViewById(R.id.tv_key_test_status);
		tv_caps_test_status = (TextView) findViewById(R.id.tv_caps_test_status);
		tv_trackpad_test_status = (TextView) findViewById(R.id.tv_trackpad_test_status);
		tv_trackpad_reportrate = (TextView) findViewById(R.id.tv_trackpad_reportrate);
		tv_trackpad_reportrate.setTextColor(Color.BLUE);
		tv_trackpad_reportrate_status = (TextView) findViewById(R.id.tv_trackpad_reportrate_status);
		tv_trackpad_click = (TextView) findViewById(R.id.tv_trackpad_click);
		tv_trackpad_click_status = (TextView) findViewById(R.id.tv_trackpad_click_status);
		tv_trackpad_scroll = (TextView) findViewById(R.id.tv_trackpad_scroll);
		tv_trackpad_scroll_status = (TextView) findViewById(R.id.tv_trackpad_scroll_status);
		tv_test_result0 = (TextView) findViewById(R.id.tv_test_result0);
		if (layoutLanguage.equalsIgnoreCase("UK")) {
			tv_test_result0.setText(" 測試結果（ ＵＫ ）： ");
		} else {
			tv_test_result0.setText(" 測試結果（ ＵＳ ）： ");
		}
		tv_test_result = (TextView) findViewById(R.id.tv_test_result);
		// init TextView vars ======================
		
	}
	
	public void reStartTest(){
		
		testStep = 0;
		wrongPressedkeyNum = -1;
		isCompleteTest = false;
		isTestNextRow = false;
		
	}
	
	public void startTest(){
		
		if (isDebug) {
			Log.e("gray", "MainActivity.java:startTest, testStep:" + testStep);
		}
		
		switch (testStep) {
		
		case FIRST_ROW_BASE+0:
			// start test
			setKeyColor(tv_firstRow01, color_blue);
			tv_test_hint.setText("按下藍色按鍵");
			tv_key_test_status.setText(": 測試中...");
			tv_key_test_status.setTextColor(ORANGE);
			break;
		case FIRST_ROW_BASE+1:
			setKeyColor(tv_firstRow02, color_blue);
			setKeyColor(tv_fn, color_blue);
			tv_test_hint.setText("同時按下 ( fn + 藍色按鍵 ) ");
			break;
		case FIRST_ROW_BASE+2:
			setKeyColor(tv_fn, color_blue);
			setKeyColor(tv_firstRow03, color_blue);
			break;
		case FIRST_ROW_BASE+3:
			setKeyColor(tv_fn, color_blue);
			setKeyColor(tv_firstRow04, color_blue);
			break;
		case FIRST_ROW_BASE+4:
			setKeyColor(tv_fn, color_blue);
			setKeyColor(tv_firstRow05, color_blue);
			break;
		case FIRST_ROW_BASE+5:
			setKeyColor(tv_fn, color_blue);
			setKeyColor(tv_firstRow06, color_blue);
			break;
		case FIRST_ROW_BASE+6:
			setKeyColor(tv_fn, color_blue);
			setKeyColor(tv_firstRow07, color_blue);
			break;
		case FIRST_ROW_BASE+7:
			setKeyColor(tv_fn, color_blue);
			setKeyColor(tv_firstRow08, color_blue);
			break;
		case FIRST_ROW_BASE+8:
			setKeyColor(tv_fn, color_blue);
			setKeyColor(tv_firstRow09, color_blue);
			break;
		case FIRST_ROW_BASE+9:
			setKeyColor(tv_fn, color_blue);
			setKeyColor(tv_firstRow10, color_blue);
			break;
		case FIRST_ROW_BASE+10:
			setKeyColor(tv_fn, color_blue);
			setKeyColor(tv_firstRow11, color_blue);
			break;
		case FIRST_ROW_BASE+11:
			setKeyColor(tv_fn, color_blue);
			setKeyColor(tv_firstRow12, color_blue);
			break;
		case FIRST_ROW_BASE+12:
			setKeyColor(tv_fn, color_blue);
			setKeyColor(tv_firstRow13, color_blue);
			break;
		case FIRST_ROW_BASE+13:
			setKeyColor(tv_fn, color_blue);
			setKeyColor(tv_firstRow14, color_blue);
			break;

		case SECOND_ROW_BASE+0:
			setKeyColor(tv_fn, color_green);
			setKeyColor(tv_1, color_blue);
			tv_test_hint.setText("按下藍色按鍵");
			break;
		case SECOND_ROW_BASE+1:
			setKeyColor(tv_2, color_blue);
			break;
		case SECOND_ROW_BASE+2:
			setKeyColor(tv_3, color_blue);
			break;
		case SECOND_ROW_BASE+3:
			setKeyColor(tv_4, color_blue);
			break;
		case SECOND_ROW_BASE+4:
			setKeyColor(tv_5, color_blue);
			break;
		case SECOND_ROW_BASE+5:
			setKeyColor(tv_6, color_blue);
			break;
		case SECOND_ROW_BASE+6:
			setKeyColor(tv_7, color_blue);
			break;
		case SECOND_ROW_BASE+7:
			setKeyColor(tv_8, color_blue);
			break;
		case SECOND_ROW_BASE+8:
			setKeyColor(tv_9, color_blue);
			break;
		case SECOND_ROW_BASE+9:
			setKeyColor(tv_0, color_blue);
			break;
		case SECOND_ROW_BASE+10:
			setKeyColor(tv_minus, color_blue);
			break;
		case SECOND_ROW_BASE+11:
			setKeyColor(tv_bs, color_blue);
			break;
			
		case THIRD_ROW_BASE+0:
			setKeyColor(tv_tab, color_blue);
			break;
		case THIRD_ROW_BASE+1:
			setKeyColor(tv_q, color_blue);
			break;
		case THIRD_ROW_BASE+2:
			setKeyColor(tv_w, color_blue);
			break;
		case THIRD_ROW_BASE+3:
			setKeyColor(tv_e, color_blue);
			break;
		case THIRD_ROW_BASE+4:
			setKeyColor(tv_r, color_blue);
			break;
		case THIRD_ROW_BASE+5:
			setKeyColor(tv_t, color_blue);
			break;
		case THIRD_ROW_BASE+6:
			setKeyColor(tv_y, color_blue);
			break;
		case THIRD_ROW_BASE+7:
			setKeyColor(tv_u, color_blue);
			break;
		case THIRD_ROW_BASE+8:
			setKeyColor(tv_i, color_blue);
			break;
		case THIRD_ROW_BASE+9:
			setKeyColor(tv_o, color_blue);
			break;
		case THIRD_ROW_BASE+10:
			setKeyColor(tv_p, color_blue);
			break;
		case THIRD_ROW_BASE+11:
			setKeyColor(tv_equal, color_blue);
			break;
		case THIRD_ROW_BASE+12:
			setKeyColor(tv_backslash, color_blue);
			break;
			
		case FORTH_ROW_BASE+0:
			setKeyColor(tv_caps, color_orange);
			tv_test_hint.setText("按下 caps，並確認 LED 燈有亮起");
			tv_test_hint.setTextColor(ORANGE);
			tv_caps_test_status.setText(": 測試中...");
			tv_caps_test_status.setTextColor(ORANGE);
			break;
		case FORTH_ROW_BASE+1:
			setKeyColor(tv_a, color_blue);
			tv_test_hint.setText("按下藍色按鍵");
			tv_test_hint.setTextColor(Color.BLUE);
			tv_caps_test_status.setText(": PASS");
			tv_caps_test_status.setTextColor(Color.GREEN);
			break;
		case FORTH_ROW_BASE+2:
			setKeyColor(tv_s, color_blue);
			break;
		case FORTH_ROW_BASE+3:
			setKeyColor(tv_d, color_blue);
			break;
		case FORTH_ROW_BASE+4:
			setKeyColor(tv_f, color_blue);
			break;
		case FORTH_ROW_BASE+5:
			setKeyColor(tv_g, color_blue);
			break;
		case FORTH_ROW_BASE+6:
			setKeyColor(tv_h, color_blue);
			break;
		case FORTH_ROW_BASE+7:
			setKeyColor(tv_j, color_blue);
			break;
		case FORTH_ROW_BASE+8:
			setKeyColor(tv_k, color_blue);
			break;
		case FORTH_ROW_BASE+9:
			setKeyColor(tv_l, color_blue);
			break;
		case FORTH_ROW_BASE+10:
			setKeyColor(tv_apostrophe, color_blue);
			break;
		case FORTH_ROW_BASE+11:
			setKeyColor(tv_enter, color_blue);
			break;
			
		case FIFTH_ROW_BASE+0:
			setKeyColor(tv_lshift, color_blue);
			break;
		case FIFTH_ROW_BASE+1:
			setKeyColor(tv_z, color_blue);
			break;
		case FIFTH_ROW_BASE+2:
			setKeyColor(tv_x, color_blue);
			break;
		case FIFTH_ROW_BASE+3:
			setKeyColor(tv_c, color_blue);
			break;
		case FIFTH_ROW_BASE+4:
			setKeyColor(tv_v, color_blue);
			break;
		case FIFTH_ROW_BASE+5:
			setKeyColor(tv_b, color_blue);
			break;
		case FIFTH_ROW_BASE+6:
			setKeyColor(tv_n, color_blue);
			break;
		case FIFTH_ROW_BASE+7:
			setKeyColor(tv_m, color_blue);
			break;
		case FIFTH_ROW_BASE+8:
			setKeyColor(tv_comma, color_blue);
			break;
		case FIFTH_ROW_BASE+9:
			setKeyColor(tv_period, color_blue);
			break;
		case FIFTH_ROW_BASE+10:
			setKeyColor(tv_slash, color_blue);
			break;
		case FIFTH_ROW_BASE+11:
			setKeyColor(tv_rshift, color_blue);
			break;
			
		case SIXTH_ROW_BASE+0:
			setKeyColor(tv_ctl, color_blue);
			break;
		case SIXTH_ROW_BASE+1:
			setKeyColor(tv_alt, color_blue);
			break;
		case SIXTH_ROW_BASE+2:
			setKeyColor(tv_space, color_blue);
			break;
		case SIXTH_ROW_BASE+3:
			setKeyColor(tv_semicolon, color_blue);
			break;
		case SIXTH_ROW_BASE+4:
			setKeyColor(tv_lbracket, color_blue);
			break;
		case SIXTH_ROW_BASE+5:
			setKeyColor(tv_arrow_up, color_blue);
			break;
		case SIXTH_ROW_BASE+6:
			setKeyColor(tv_rbracket, color_blue);
			break;
		case SIXTH_ROW_BASE+7:
			setKeyColor(tv_arrow_left, color_blue);
			break;
		case SIXTH_ROW_BASE+8:
			setKeyColor(tv_arrow_down, color_blue);
			break;
		case SIXTH_ROW_BASE+9:
			setKeyColor(tv_arrow_right, color_blue);
			break;
			
		case TEST_TRACKPAD_BASE+0:
			tv_key_test_status.setText(": PASS");
			tv_key_test_status.setTextColor(Color.GREEN);
			tv_test_hint.setText("用單指在 TrackPad 上持續滑動 ( report rate 需在範圍 [ " + setReportRate + " +/- " + setReportRateRange + " ] 內連續 " + setReportRateHoldSeconds + " 秒 )");
			tv_trackpad_test_status.setText(": 測試中...");
			tv_trackpad_test_status.setTextColor(ORANGE);
			tv_trackpad_reportrate_status.setText(": 測試中...");
			tv_trackpad_reportrate_status.setTextColor(ORANGE);
			break;
		case TEST_TRACKPAD_BASE+1:
			tv_trackpad_reportrate_status.setText(": PASS");
			tv_trackpad_reportrate_status.setTextColor(Color.GREEN);
			tv_test_hint.setText("用單指在 TrackPad 左下、中下、右下各點擊一次");
			tv_trackpad_click_status.setText(": 測試中...");
			tv_trackpad_click_status.setTextColor(ORANGE);
			break;
		case TEST_TRACKPAD_BASE+2:
			tv_trackpad_click_status.setText(": PASS");
			tv_trackpad_click_status.setTextColor(Color.GREEN);
			tv_test_hint.setText("用雙指在 TrackPad 上下滑動");
			tv_trackpad_scroll_status.setText(": 測試中...");
			tv_trackpad_scroll_status.setTextColor(ORANGE);
			break;
		case TEST_COMPLETE:
			tv_trackpad_test_status.setText(": PASS");
			tv_trackpad_test_status.setTextColor(Color.GREEN);
			tv_trackpad_scroll_status.setText(": PASS");
			tv_trackpad_scroll_status.setTextColor(Color.GREEN);
			tv_test_hint.setText("測試結束, 按下 \"UnPair\" 按鍵解除配對並重新開始測試");
			tv_test_result.setText("PASS");
			tv_test_result.setTextColor(Color.GREEN);
			tv_test_result.setTextSize(120);
			break;
			
		default:
			break;
		}
	}
	
	public void setKeyColor( TextView tv_key, int color ){
		
		switch (color) {
		case color_red:
			tv_key.setBackgroundColor(Color.RED);
			break;
		case color_green:
			tv_key.setBackgroundColor(Color.GREEN);
			break;
		case color_blue:
			tv_key.setBackgroundColor(Color.BLUE);
			break;
		case color_black:
			tv_key.setBackgroundColor(Color.BLACK);
			break;
		case color_orange:
			tv_key.setBackgroundColor(ORANGE);
			break;

		default:
			break;
		}
	}
	
	// set key background to green
	public void keyAlreadyTested( TextView tv_key ){
		if (isDebug) {
			Log.e("gray", "MainActivity.java:keyAlreadyTested, " + "");
		}
		
		setKeyColor(tv_key, color_green);
		
	}
	
	// set key background to black
	public void keyNotTested( TextView tv_key ){
		if (isDebug) {
			Log.e("gray", "MainActivity.java:keyNotTested, " + "");
		}
		
		setKeyColor(tv_key, color_black);
		
	}
	
	public void keyDectectedCorrect( TextView tv_key ){
		if (isDebug) {
			Log.e("gray", "MainActivity.java:keyDectectedCorrect, " + "");
		}
		
		setKeyColor(tv_key, color_green);
		
		// next test step
		if (isTestNextRow) {
			isTestNextRow = false;
		} else {
			testStep++;
		}
		
		startTest();
	}
	
	public void keyDectectedCorrect( TextView tv_key, TextView tv_key2 ){
		if (isDebug) {
			Log.e("gray", "MainActivity.java:keyDectectedCorrect, " + "");
		}
		
		setKeyColor(tv_key, color_green);
		setKeyColor(tv_key2, color_green);
		
		// next test step
		if (isTestNextRow) {
			isTestNextRow = false;
		} else {
			testStep++;
		}
		
		startTest();
	}
		
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (isDebug) {
			Log.e("gray", "MainActivity.java:onKeyDown, " + event.toString());
		}
		
		if (testStage < STAGE_KEY_TEST) {
			return true;
		}
		
		// first row
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
			setKeyColor(tv_firstRow01, color_red);
		} else if (event.getScanCode() == 194) {  // ****************need chnage
//		} else if (keyCode == KeyEvent.KEYCODE_FORWARD_DEL) {  // ****************need chnage
			setKeyColor(tv_firstRow02, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_F1) {
			setKeyColor(tv_firstRow03, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_F2) {
			setKeyColor(tv_firstRow04, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_F3) {
			setKeyColor(tv_firstRow05, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_F4) {
			setKeyColor(tv_firstRow06, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_F5) {
			setKeyColor(tv_firstRow07, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_F6) {
			setKeyColor(tv_firstRow08, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_F7) {
			setKeyColor(tv_firstRow09, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_F8) {
			setKeyColor(tv_firstRow10, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_F9) {
			setKeyColor(tv_firstRow11, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_F10) {
			setKeyColor(tv_firstRow12, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_F11) {
			setKeyColor(tv_firstRow13, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_F12) {
			setKeyColor(tv_firstRow14, color_red);
			
		// second row
		} else if (keyCode == KeyEvent.KEYCODE_1) {
			setKeyColor(tv_1, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_2) {
			setKeyColor(tv_2, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_3) {
			setKeyColor(tv_3, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_4) {
			setKeyColor(tv_4, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_5) {
			setKeyColor(tv_5, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_6) {
			setKeyColor(tv_6, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_7) {
			setKeyColor(tv_7, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_8) {
			setKeyColor(tv_8, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_9) {
			setKeyColor(tv_9, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_0) {
			setKeyColor(tv_0, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_MINUS) {
			setKeyColor(tv_minus, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_DEL) {
			setKeyColor(tv_bs, color_red);
		
		// third row
		} else if (keyCode == KeyEvent.KEYCODE_TAB) {
			setKeyColor(tv_tab, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_Q) {
			setKeyColor(tv_q, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_W) {
			setKeyColor(tv_w, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_E) {
			setKeyColor(tv_e, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_R) {
			setKeyColor(tv_r, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_T) {
			setKeyColor(tv_t, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_Y) {
			setKeyColor(tv_y, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_U) {
			setKeyColor(tv_u, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_I) {
			setKeyColor(tv_i, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_O) {
			setKeyColor(tv_o, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_P) {
			setKeyColor(tv_p, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_EQUALS) {
			setKeyColor(tv_equal, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_BACKSLASH) {
			setKeyColor(tv_backslash, color_red);
		
		// forth row
		} else if (keyCode == KeyEvent.KEYCODE_CAPS_LOCK) {
			setKeyColor(tv_caps, color_red);
			byte reportType = Byte.decode("0x02");
			
			if (isCapsLEDOn) {
				isCapsLEDOn = false;
				btInputDevice.setReport(btDevice, reportType, "01" + "00");
			} else {
				isCapsLEDOn = true;
				btInputDevice.setReport(btDevice, reportType, "01" + "02");
			}
		} else if (keyCode == KeyEvent.KEYCODE_A) {
			setKeyColor(tv_a, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_S) {
			setKeyColor(tv_s, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_D) {
			setKeyColor(tv_d, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_F) {
			setKeyColor(tv_f, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_G) {
			setKeyColor(tv_g, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_H) {
			setKeyColor(tv_h, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_J) {
			setKeyColor(tv_j, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_K) {
			setKeyColor(tv_k, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_L) {
			setKeyColor(tv_l, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_APOSTROPHE) {
			setKeyColor(tv_apostrophe, color_red);
//		} else if (keyCode == KeyEvent.KEYCODE_ENTER) {
//			setKeyColor(tv_enter, color_red);
		
		// fifth row
		} else if (keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {
			setKeyColor(tv_lshift, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_Z) {
			setKeyColor(tv_z, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_X) {
			setKeyColor(tv_x, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_C) {
			setKeyColor(tv_c, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_V) {
			setKeyColor(tv_v, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_B) {
			setKeyColor(tv_b, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_N) {
			setKeyColor(tv_n, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_M) {
			setKeyColor(tv_m, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_COMMA) {
			setKeyColor(tv_comma, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_PERIOD) {
			setKeyColor(tv_period, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_SLASH) {
			setKeyColor(tv_slash, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT) {
			setKeyColor(tv_rshift, color_red);
			
		// sixth row
		} else if (keyCode == KeyEvent.KEYCODE_CTRL_LEFT) {
			setKeyColor(tv_ctl, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_ALT_LEFT) {
			setKeyColor(tv_alt, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_SPACE) {
			setKeyColor(tv_space, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_SEMICOLON) {
			setKeyColor(tv_semicolon, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_LEFT_BRACKET) {
			setKeyColor(tv_lbracket, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			setKeyColor(tv_arrow_up, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_RIGHT_BRACKET) {
			setKeyColor(tv_rbracket, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			setKeyColor(tv_arrow_left, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			setKeyColor(tv_arrow_down, color_red);
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			setKeyColor(tv_arrow_right, color_red);
		}
		
//		return super.onKeyDown(keyCode, event);
 		return true;
	}
	
	public void keyUpCheck(TextView tv_key, int keyPosition) {
		
		if (testStep > keyPosition) {
			keyAlreadyTested(tv_key);
		} else if (testStep == keyPosition) {
			
			if (keyPosition == FIRST_ROW_BASE+13) {
				isTestNextRow = true;
				testStep = SECOND_ROW_BASE;
			} else if (keyPosition == SECOND_ROW_BASE+11) {
				isTestNextRow = true;
				testStep = THIRD_ROW_BASE;
			} else if (keyPosition == THIRD_ROW_BASE+12) {
				isTestNextRow = true;
				testStep = FORTH_ROW_BASE;
			} else if (keyPosition == FORTH_ROW_BASE+11) {
				isTestNextRow = true;
				testStep = FIFTH_ROW_BASE;
			} else if (keyPosition == FIFTH_ROW_BASE+11) {
				isTestNextRow = true;
				testStep = SIXTH_ROW_BASE;
			} else if (keyPosition == SIXTH_ROW_BASE+9) {
				isTestNextRow = true;
				testStep = TEST_TRACKPAD_BASE;
				testStage = STAGE_TRACKPAD_TEST;
			} 
			
			keyDectectedCorrect(tv_key);
		} else {
			keyNotTested(tv_key);
		}
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		if (isDebug) {
			Log.e("gray", "MainActivity.java:onKeyUp, " + event.toString() );
		}
		
		if (testStage < STAGE_KEY_TEST) {
			return true;
		}
		
		// first row
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
			
			keyUpCheck(tv_firstRow01, FIRST_ROW_BASE+0);
			
		} else if (event.getScanCode() == 194) {  // ****************need chnage
//			} else if (keyCode == KeyEvent.KEYCODE_FORWARD_DEL) {  // ****************need chnage

			keyUpCheck(tv_firstRow02, FIRST_ROW_BASE+1);
			
		} else if (keyCode == KeyEvent.KEYCODE_F1) {

			keyUpCheck(tv_firstRow03, FIRST_ROW_BASE+2);
			
		} else if (keyCode == KeyEvent.KEYCODE_F2) {

			keyUpCheck(tv_firstRow04, FIRST_ROW_BASE+3);
			
		} else if (keyCode == KeyEvent.KEYCODE_F3) {

			keyUpCheck(tv_firstRow05, FIRST_ROW_BASE+4);
			
		} else if (keyCode == KeyEvent.KEYCODE_F4) {

			keyUpCheck(tv_firstRow06, FIRST_ROW_BASE+5);
			
		} else if (keyCode == KeyEvent.KEYCODE_F5) {

			keyUpCheck(tv_firstRow07, FIRST_ROW_BASE+6);
			
		} else if (keyCode == KeyEvent.KEYCODE_F6) {

			keyUpCheck(tv_firstRow08, FIRST_ROW_BASE+7);
			
		} else if (keyCode == KeyEvent.KEYCODE_F7) {

			keyUpCheck(tv_firstRow09, FIRST_ROW_BASE+8);
			
		} else if (keyCode == KeyEvent.KEYCODE_F8) {

			keyUpCheck(tv_firstRow10, FIRST_ROW_BASE+9);
			
		} else if (keyCode == KeyEvent.KEYCODE_F9) {

			keyUpCheck(tv_firstRow11, FIRST_ROW_BASE+10);
			
		} else if (keyCode == KeyEvent.KEYCODE_F10) {

			keyUpCheck(tv_firstRow12, FIRST_ROW_BASE+11);
			
		} else if (keyCode == KeyEvent.KEYCODE_F11) {

			keyUpCheck(tv_firstRow13, FIRST_ROW_BASE+12);
			
		} else if (keyCode == KeyEvent.KEYCODE_F12) {

			keyUpCheck(tv_firstRow14, FIRST_ROW_BASE+13);
			
		// second row
		} else if (keyCode == KeyEvent.KEYCODE_1) {

			keyUpCheck(tv_1, SECOND_ROW_BASE+0);
			
		} else if (keyCode == KeyEvent.KEYCODE_2) {

			keyUpCheck(tv_2, SECOND_ROW_BASE+1);
			
		} else if (keyCode == KeyEvent.KEYCODE_3) {

			keyUpCheck(tv_3, SECOND_ROW_BASE+2);
			
		} else if (keyCode == KeyEvent.KEYCODE_4) {

			keyUpCheck(tv_4, SECOND_ROW_BASE+3);
			
		} else if (keyCode == KeyEvent.KEYCODE_5) {

			keyUpCheck(tv_5, SECOND_ROW_BASE+4);
			
		} else if (keyCode == KeyEvent.KEYCODE_6) {

			keyUpCheck(tv_6, SECOND_ROW_BASE+5);
			
		} else if (keyCode == KeyEvent.KEYCODE_7) {

			keyUpCheck(tv_7, SECOND_ROW_BASE+6);
			
		} else if (keyCode == KeyEvent.KEYCODE_8) {

			keyUpCheck(tv_8, SECOND_ROW_BASE+7);
			
		} else if (keyCode == KeyEvent.KEYCODE_9) {

			keyUpCheck(tv_9, SECOND_ROW_BASE+8);
			
		} else if (keyCode == KeyEvent.KEYCODE_0) {

			keyUpCheck(tv_0, SECOND_ROW_BASE+9);
			
		} else if (keyCode == KeyEvent.KEYCODE_MINUS) {

			keyUpCheck(tv_minus, SECOND_ROW_BASE+10);
			
		} else if (keyCode == KeyEvent.KEYCODE_DEL) {

			keyUpCheck(tv_bs, SECOND_ROW_BASE+11);
			
		// third row
		} else if (keyCode == KeyEvent.KEYCODE_TAB) {

			keyUpCheck(tv_tab, THIRD_ROW_BASE+0);
			
		} else if (keyCode == KeyEvent.KEYCODE_Q) {

			keyUpCheck(tv_q, THIRD_ROW_BASE+1);
			
		} else if (keyCode == KeyEvent.KEYCODE_W) {

			keyUpCheck(tv_w, THIRD_ROW_BASE+2);
			
		} else if (keyCode == KeyEvent.KEYCODE_E) {

			keyUpCheck(tv_e, THIRD_ROW_BASE+3);
			
		} else if (keyCode == KeyEvent.KEYCODE_R) {

			keyUpCheck(tv_r, THIRD_ROW_BASE+4);
			
		} else if (keyCode == KeyEvent.KEYCODE_T) {

			keyUpCheck(tv_t, THIRD_ROW_BASE+5);
			
		} else if (keyCode == KeyEvent.KEYCODE_Y) {

			keyUpCheck(tv_y, THIRD_ROW_BASE+6);
			
		} else if (keyCode == KeyEvent.KEYCODE_U) {
			
			keyUpCheck(tv_u, THIRD_ROW_BASE+7);
			
		} else if (keyCode == KeyEvent.KEYCODE_I) {

			keyUpCheck(tv_i, THIRD_ROW_BASE+8);
			
		} else if (keyCode == KeyEvent.KEYCODE_O) {

			keyUpCheck(tv_o, THIRD_ROW_BASE+9);
			
		} else if (keyCode == KeyEvent.KEYCODE_P) {

			keyUpCheck(tv_p, THIRD_ROW_BASE+10);
			
		} else if (keyCode == KeyEvent.KEYCODE_EQUALS) {

			keyUpCheck(tv_equal, THIRD_ROW_BASE+11);
			
		} else if (keyCode == KeyEvent.KEYCODE_BACKSLASH || 
				   keyCode == KeyEvent.KEYCODE_POUND) {

			keyUpCheck(tv_backslash, THIRD_ROW_BASE+12);
			
		// forth row
		} else if (keyCode == KeyEvent.KEYCODE_CAPS_LOCK) {

			keyUpCheck(tv_caps, FORTH_ROW_BASE+0);
			
		} else if (keyCode == KeyEvent.KEYCODE_A) {

			keyUpCheck(tv_a, FORTH_ROW_BASE+1);
			
		} else if (keyCode == KeyEvent.KEYCODE_S) {

			keyUpCheck(tv_s, FORTH_ROW_BASE+2);
			
		} else if (keyCode == KeyEvent.KEYCODE_D) {

			keyUpCheck(tv_d, FORTH_ROW_BASE+3);
			
		} else if (keyCode == KeyEvent.KEYCODE_F) {

			keyUpCheck(tv_f, FORTH_ROW_BASE+4);
			
		} else if (keyCode == KeyEvent.KEYCODE_G) {

			keyUpCheck(tv_g, FORTH_ROW_BASE+5);
			
		} else if (keyCode == KeyEvent.KEYCODE_H) {

			keyUpCheck(tv_h, FORTH_ROW_BASE+6);
			
		} else if (keyCode == KeyEvent.KEYCODE_J) {

			keyUpCheck(tv_j, FORTH_ROW_BASE+7);
			
		} else if (keyCode == KeyEvent.KEYCODE_K) {

			keyUpCheck(tv_k, FORTH_ROW_BASE+8);
			
		} else if (keyCode == KeyEvent.KEYCODE_L) {

			keyUpCheck(tv_l, FORTH_ROW_BASE+9);
			
		} else if (keyCode == KeyEvent.KEYCODE_APOSTROPHE) {

			keyUpCheck(tv_apostrophe, FORTH_ROW_BASE+10);
			
//		} else if (keyCode == KeyEvent.KEYCODE_ENTER) {
//
//			keyUpCheck(tv_enter, FORTH_ROW_BASE+11);
			
		// fifth row
		} else if (keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {

			keyUpCheck(tv_lshift, FIFTH_ROW_BASE+0);
			
		} else if (keyCode == KeyEvent.KEYCODE_Z) {
			
			keyUpCheck(tv_z, FIFTH_ROW_BASE+1);
			
		} else if (keyCode == KeyEvent.KEYCODE_X) {

			keyUpCheck(tv_x, FIFTH_ROW_BASE+2);
			
		} else if (keyCode == KeyEvent.KEYCODE_C) {

			keyUpCheck(tv_c, FIFTH_ROW_BASE+3);
			
		} else if (keyCode == KeyEvent.KEYCODE_V) {

			keyUpCheck(tv_v, FIFTH_ROW_BASE+4);
			
		} else if (keyCode == KeyEvent.KEYCODE_B) {

			keyUpCheck(tv_b, FIFTH_ROW_BASE+5);
			
		} else if (keyCode == KeyEvent.KEYCODE_N) {

			keyUpCheck(tv_n, FIFTH_ROW_BASE+6);
			
		} else if (keyCode == KeyEvent.KEYCODE_M) {

			keyUpCheck(tv_m, FIFTH_ROW_BASE+7);
			
		} else if (keyCode == KeyEvent.KEYCODE_COMMA) {

			keyUpCheck(tv_comma, FIFTH_ROW_BASE+8);
			
		} else if (keyCode == KeyEvent.KEYCODE_PERIOD) {

			keyUpCheck(tv_period, FIFTH_ROW_BASE+9);
			
		} else if (keyCode == KeyEvent.KEYCODE_SLASH) {

			keyUpCheck(tv_slash, FIFTH_ROW_BASE+10);
			
		} else if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT) {

			keyUpCheck(tv_rshift, FIFTH_ROW_BASE+11);
			
		// sixth row
		} else if (keyCode == KeyEvent.KEYCODE_CTRL_LEFT) {

			keyUpCheck(tv_ctl, SIXTH_ROW_BASE+0);
			
		} else if (keyCode == KeyEvent.KEYCODE_ALT_LEFT) {

			keyUpCheck(tv_alt, SIXTH_ROW_BASE+1);
			
		} else if (keyCode == KeyEvent.KEYCODE_SPACE) {

			keyUpCheck(tv_space, SIXTH_ROW_BASE+2);
			
		} else if (keyCode == KeyEvent.KEYCODE_SEMICOLON) {

			keyUpCheck(tv_semicolon, SIXTH_ROW_BASE+3);
			
		} else if (keyCode == KeyEvent.KEYCODE_LEFT_BRACKET) {

			keyUpCheck(tv_lbracket, SIXTH_ROW_BASE+4);
			
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {

			keyUpCheck(tv_arrow_up, SIXTH_ROW_BASE+5);
			
		} else if (keyCode == KeyEvent.KEYCODE_RIGHT_BRACKET) {

			keyUpCheck(tv_rbracket, SIXTH_ROW_BASE+6);
			
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {

			keyUpCheck(tv_arrow_left, SIXTH_ROW_BASE+7);
			
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

			keyUpCheck(tv_arrow_down, SIXTH_ROW_BASE+8);
			
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

			keyUpCheck(tv_arrow_right, SIXTH_ROW_BASE+9);
			
		}
		
//		return super.onKeyDown(keyCode, event);
 		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (isDebug) {
			Log.e("gray", "MainActivity.java: onActivityResult, requestCode: " + requestCode);
			Log.e("gray", "MainActivity.java: onActivityResult, resultCode: " + resultCode);
		}
		
        switch (requestCode) {
		case 0:
			
			try {
				setReportRate = Integer.valueOf(sharedPrefs.getString("pref_set_reportrate", "60"));
			} catch (Exception e) {
				Log.e("gray", "MainActivity.java: onCreate, Exception : " + e.toString() );
			}
			try {
				setReportRateRange = Integer.valueOf(sharedPrefs.getString("pref_set_reportrate_range", "40"));
			} catch (Exception e) {
				Log.e("gray", "MainActivity.java: onCreate, Exception : " + e.toString() );
			}
			try {
				setReportRateHoldSeconds = Integer.valueOf(sharedPrefs.getString("pref_set_reportrate_hold_seconds", "3"));
			} catch (Exception e) {
				Log.e("gray", "MainActivity.java: onCreate, Exception : " + e.toString() );
			}
			layoutLanguage = sharedPrefs.getString("pref_layout_language", "US");
			if (layoutLanguage.equalsIgnoreCase("UK")) {
				tv_backslash.setText(" # ");
				tv_test_result0.setText(" 測試結果（ ＵＫ ）： ");
			} else {
				tv_backslash.setText(" \\ ");
				tv_test_result0.setText(" 測試結果（ ＵＳ ）： ");
			}
			if (isDebug) {
				Log.e("gray", "MainActivity.java:onActivityResult, setReportRate:" + setReportRate);
				Log.e("gray", "MainActivity.java:onActivityResult, setReportRateRange:" + setReportRateRange);
				Log.e("gray", "MainActivity.java:onActivityResult, setReportRateHoldSeconds:" + setReportRateHoldSeconds);
				Log.e("gray", "MainActivity.java:onActivityResult, layoutLanguage:" + layoutLanguage);
			}
			
			
			
			break;
        }
	}
	
	@Override
	public void onPause() {
		
		if (isDebug) {
			Log.e("gray", "MainActivity.java: onDestroy");	
		}
		
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		
		if (isDebug) {
			Log.e("gray", "MainActivity.java: onDestroy");	
		}
		
		unregisterReceiver(searchDevices);
		btAdapter.closeProfileProxy(BluetoothProfile.INPUT_DEVICE, btInputDevice);
		
		super.onDestroy();
	}
	
    public void showProcessDialog(CharSequence title, CharSequence message){
		mProgressDialog = ProgressDialog.show(MainActivity.this, title, message, true);
		mProgressDialog.setCancelable(true); 
    }
    
    public void showAlertDialog(String title, String message) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
	}
    
	Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {
        	
			switch (msg.what) {
			case MSG_DISMISS_PROGRESS_DIALOG:

				try {
					mProgressDialog.dismiss();
					mProgressDialog = null;
				} catch (Exception e) {
					// nothing
				}

				break;
				
			case MSG_RESET_MOTION_EVENT_COUNT:
				
				tv_trackpad_reportrate.setText(": " + (String)msg.obj);
				motionEventCount = 0;
				
				break;
			
			case MSG_DETECT_ACTION_DOWN:
				
				tv_trackpad_click.setText(": O");
				tv_trackpad_click.setTextColor(Color.BLUE);
				
				tv_trackpad_click.postDelayed(new Runnable() {
					@Override
					public void run() {
						tv_trackpad_click.setText(": X");
						tv_trackpad_click.setTextColor(Color.BLACK);
					}
				}, 200);
				
				break;
				
			case MSG_DETECT_ACTION_SCROLL:
				
				tv_trackpad_scroll.setText(": O");
				tv_trackpad_scroll.setTextColor(Color.BLUE);

				tv_trackpad_scroll.postDelayed(new Runnable() {
					@Override
					public void run() {
						tv_trackpad_scroll.setText(": X");
						tv_trackpad_scroll.setTextColor(Color.BLACK);
					}
				}, 200);
				break;
				
			case MSG_TEST_NEXT_STEP:
				
				testStep++;
				startTest();
				
				break;

			case MSG_TEST_COMPLETE:
				
				testStep = TEST_COMPLETE;
				startTest();
				break;
				
			case MSG_DRAW_LINE:
				
				Bundle data = null;
				String event_x = null;
				String event_y = null;
				
				data = msg.getData();
				event_x = data.getString("event_x");
				event_y = data.getString("event_y");
//				if (event_x != null & !event_x.isEmpty()) {
//					rel_x_value.setText(event_x);
//				}
//
//				if (event_y != null & !event_y.isEmpty()) {
//					rel_y_value.setText(event_y);
//				}
				break;
			}
        }
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		if (isDebug) {
			Log.e("gray", "MainActivity.java: onCreateOptionsMenu");
		}
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		
		if (isDebug) {
			Log.e("gray", "MainActivity.java:onOptionsItemSelected, " + "");
		}
        switch (item.getItemId()) {
        
        case R.id.action_settings:
        	if (isDebug) {
        		Log.e("gray", "MainActivity.java:onOptionsItemSelected, case R.id.action_settings");
        	}
        	
        	final EditText input = new EditText(this);
			input.setText("0");
			new AlertDialog.Builder(this).setTitle("Input Password").setIcon(
				     android.R.drawable.ic_dialog_info).setView(input).setPositiveButton("Confirm",
				    	new DialogInterface.OnClickListener() { 
				    	 	public void onClick(DialogInterface dialog, int whichButton) {
				    	 		
				    	 		int passwd = 0;
				    	 		try {
				    	 			passwd = Integer.valueOf(input.getText().toString());
								} catch (Exception e) {
									// TODO: handle exception
								}
				    	 		
				    	 		if ( passwd == PASSWORD ) {
				    	 			Intent i = new Intent(MainActivity.this, SettingsActivity.class);
				    	 			startActivityForResult(i, 0);
								} else {
									showAlertDialog("Access Deny", "Wrong Password");
								}
				    	 	}
				    	 }
				    ).setNegativeButton("Cancel", null).show();
            break;
            
        }
        
        return true;
	}
	
}
