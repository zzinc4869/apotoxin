package com.imooc.flashsale.enums;

public enum OrderInfoState {
	CREATE_AND_UNPAID(0, "新建未支付"), PAID_DONE(1, "已经支付"), SHIPPED(2, "已发货"), RECEIVED(3, "已收货");

	private int state;

	private String stateInfo;

	private OrderInfoState(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static OrderInfoState stateOf(int index) {
		for (OrderInfoState state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}

}
