package com.bit.strength.card;

public class GetCardData extends Thread {
	public boolean exit;
	private CardViewUpdater cv_updater = null;
	private int card_type_index = 0;

	private final static int CARD_6208 = 0;
	private final static int CARD_341 = 2;
	private final static int CARD_BST23208 = 1;
	private final static int CARD_1553 = 3;

	private boolean[] finiRec = { false };
	private boolean[] resetP = { false };
	private boolean stopRec;

	private double[] dataVals_card6208;
	private byte[] dataVals_bst23208;
	private int[] dataLength;

	public GetCardData(CardViewUpdater cv_updater, int card_type_index) {
		// TODO Auto-generated constructor stub
		this.cv_updater = cv_updater;
		this.exit = false;
		this.card_type_index = card_type_index;
		this.stopRec = false;
		this.dataLength = new int[1];
	}

	@Override
	public void run() {
		super.run();
		int data_index = 0;
		int byte_index = 0;
		while (!exit) {
			this.finiRec[0] = false;
			this.dataLength[0] = 0;

			switch (this.card_type_index) {
			case CARD_6208:
				// this.dataVals_card6208 = new double[1024 * 1024 * 4];
				// Card6208_dll.INSTANCE.card6208_cardDataExchange(this.dataVals_card6208,
				// this.dataLength, this.finiRec, this.stopRec);
				int ret_index = cv_updater.update(
						/* inputData[byte_index], */sin[data_index],
						byteData[byte_index], randomNum(300, 5),
						randomNum(2, 3), randomNum(100, 5), data_index);
				if (ret_index != data_index) {
					byte_index++;
					if (byte_index == byteData.length) {
						byte_index = 0;
					}
					data_index = ret_index;
				}
				if (data_index == sin.length) {
					data_index = 0;
				}
				break;
			case CARD_BST23208:
				this.dataVals_bst23208 = new byte[1024 * 1024 * 4];
				this.resetP[0] = true;
				CardBST23208_dll.INSTANCE.bst23208_cardDataExchange(
						this.dataVals_bst23208, this.dataLength, this.finiRec,
						this.stopRec, this.resetP);
				break;
			case CARD_341:

				break;
			case CARD_1553:

				break;
			}
		}
		this.stopRec = true;
		switch (this.card_type_index) {
		case CARD_6208:
			Card6208_dll.INSTANCE.card6208_cardDataExchange(
					this.dataVals_card6208, this.dataLength, this.finiRec,
					this.stopRec);
			break;
		case CARD_BST23208:
			CardBST23208_dll.INSTANCE.bst23208_cardDataExchange(
					this.dataVals_bst23208, this.dataLength, this.finiRec,
					this.stopRec, this.resetP);
			break;
		case CARD_341:

			break;
		case CARD_1553:

			break;
		}
	}

	int[] byteData = { 0, 1 };
	int[] inputData = { 0, 1 };
	double[] sin = { 0.000000, 0.871557, 1.736482, 2.588190, 3.420201,
			4.226183, 5.000000, 5.735764, 6.427876, 7.071068, 7.660444,
			8.191520, 8.660254, 9.063078, 9.396926, 9.659258, 9.848078,
			9.961947, 10.000000, 9.961947, 9.848078, 9.659258, 9.396926,
			9.063078, 8.660254, 8.191520, 7.660444, 7.071068, 6.427876,
			5.735764, 5.000000, 4.226183, 3.420201, 2.588190, 1.736482,
			0.871557, 0.000000, -0.871557, -1.736482, -2.588190, -3.420201,
			-4.226183, -5.000000, -5.735764, -6.427876, -7.071068, -7.660444,
			-8.191520, -8.660254, -9.063078, -9.396926, -9.659258, -9.848078,
			-9.961947, -10.000000, -9.961947, -9.848078, -9.659258, -9.396926,
			-9.063078, -8.660254, -8.191520, -7.660444, -7.071068, -6.427876,
			-5.735764, -5.000000, -4.226183, -3.420201, -2.588190, -1.736482,
			-0.871557 };

	private int randomNum(int max, int min) {
		// System.out.println((Math.random() * 20 + 80));
		int val = 0;
		val = (int) (max + Math.random() * min);
		return val;
	}
}
