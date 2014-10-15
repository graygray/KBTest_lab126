package com.primax.kbtest_lab126;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothAdapter;
import android.util.Log;

public class ClsUtils {
	static public boolean createBond(Class btClass, BluetoothDevice btDevice) throws Exception {
		Method createBondMethod = btClass.getMethod("createBond");
		Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}
	
	// not work (2014/08/19)
	static public boolean removeBond(Class btClass, BluetoothDevice btDevice) throws Exception {
		Method removeBondMethod = btClass.getMethod("removeBond");
		Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}
	
	static public void printAllInform(Class clsShow) {
		try {
			int i = 0;
//			Method[] hideMethod = clsShow.getMethods();
//			for (i= 0; i< hideMethod.length; i++)
//			{
//				Log.e("method name", hideMethod[i].getName());
//			}
			
			Field[] allFields = clsShow.getFields();
			
			for (i = 0; i < allFields.length; i++) {
				Log.e("Field name", allFields[i].getName());
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static public boolean setPin(Class btClass, BluetoothDevice btDevice, String str) throws Exception {
		try {
			Method removeBondMethod = btClass.getDeclaredMethod("setPin", new Class[]{byte[].class});
			removeBondMethod.invoke(btDevice, new Object[]{str.getBytes()});
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean pair(String address, String psw, BluetoothAdapter adapter) {
		boolean result = false;
		BluetoothDevice device = adapter.getRemoteDevice(address);
		if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
			try {
				setPin(device.getClass(), device, psw);
				boolean flag = createBond(device.getClass(), device);
				if (flag) {
					result = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			result = true;
		}
		return result;
	}
	
	static public boolean cancelPairingUserInput(Class btClass, BluetoothDevice device) throws Exception {
		Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
		// cancelBondProcess()
		Boolean returnValue = (Boolean) createBondMethod.invoke(device);
		return returnValue.booleanValue();
	}
	
	static public boolean cancelBondProcess(Class btClass, BluetoothDevice device) throws Exception {
		Method createBondMethod = btClass.getMethod("cancelBondProcess");
		Boolean returnValue = (Boolean) createBondMethod.invoke(device);
		return returnValue.booleanValue();
	}
	
//	private void pairDevice(BluetoothDevice device) {
//		try {
//		    if (D)
//		    Log.d(TAG, "Start Pairing...");
//
//		    waitingForBonding = true;
//
//		    Method m = device.getClass()
//		        .getMethod("createBond", (Class[]) null);
//		    m.invoke(device, (Object[]) null);
//
//		    if (D)
//		    Log.d(TAG, "Pairing finished.");
//		} catch (Exception e) {
//		    Log.e(TAG, e.getMessage());
//		}
//	}
	
	public static void unpairDevice(BluetoothDevice device) {
		try {
		    Method m = device.getClass()
		        .getMethod("removeBond", (Class[]) null);
		    m.invoke(device, (Object[]) null);
		} catch (Exception e) {
		    Log.e("gray", e.getMessage());
		}
		}
	
}
