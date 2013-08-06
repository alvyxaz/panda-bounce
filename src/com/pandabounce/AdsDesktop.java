package com.pandabounce;

public class AdsDesktop implements AdsInterface {

	@Override
	public boolean adsVisible() {
		return false;
	}

	@Override
	public void showAds() {
		System.out.println("SHOW ADS");
	}

	@Override
	public void hideAds() {
		System.out.println("HIDE ADS");
	}

	@Override
	public void showInterstitial() {
		System.out.println("FULL SCREEN AD");
	}

}
