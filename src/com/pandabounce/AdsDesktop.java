package com.pandabounce;

public class AdsDesktop implements AdsInterface {

	@Override
	public boolean adsVisible() {
		return false;
	}

	@Override
	public void showAds() {
		System.out.println("ADS SHOW");
	}

	@Override
	public void hideAds() {
		System.out.println("ADS HIDE");
	}

}
