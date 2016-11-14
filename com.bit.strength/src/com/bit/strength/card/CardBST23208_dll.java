package com.bit.strength.card;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface CardBST23208_dll extends StdCallLibrary{
	public abstract void bst23208_cardDataExchange(byte[] dataVals_bst23208, int[] dataLength, boolean[] finiRec, boolean stopRec, boolean[] resetP);
	
	CardBST23208_dll INSTANCE = (CardBST23208_dll) Native.loadLibrary("CardDatadll_BST23208", CardBST23208_dll.class);
}
