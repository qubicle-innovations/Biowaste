package uems.biowaste.utils;

import android.content.Context;
import android.graphics.Typeface;

public final class ZTypeface {
	public static final Typeface robotoBold(Context ctx) {
		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/robotobold.ttf");
		return typeface;
	}

	public static final Typeface robotoLight(Context ctx) {
		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/robotolight.ttf");
		return typeface;
	}

	public static final Typeface robotoRegular(Context ctx) {
		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/robotoregular.ttf");
		return typeface;
	}
	public static final Typeface robotoMedium(Context ctx) {
		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/robotomedium.ttf");
		return typeface;
	}

}
