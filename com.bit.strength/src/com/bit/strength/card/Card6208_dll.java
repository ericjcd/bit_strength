package com.bit.strength.card;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface Card6208_dll extends StdCallLibrary {
	public abstract void card6208_cardDataExchange(double[] dataVals_card6208, int[] dataLength, boolean[] finiRec, boolean stopRec);
	
	Card6208_dll INSTANCE = (Card6208_dll) Native.loadLibrary("CardDatadll_6280", Card6208_dll.class);
}
