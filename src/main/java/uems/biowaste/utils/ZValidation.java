package uems.biowaste.utils;

import android.graphics.Color;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

import uems.biowaste.R;


public class ZValidation {
	private static final String CLASS_TAG = "Validate";

	public static final int VALID_TEXT_COLOR = Color.BLACK;
	public static final int INVALID_TEXT_COLOR = Color.RED;
	public static final String NOT_ALLOWED_CHR = "~!@#$%&*-=+}{][:;><?";
	public static final String NOT_ALLOWED_AS_FIRST_CHR = " ,.//\\-\'\"()";
	private String errMsgName = "Enter your name.";
	private String errMsgMobile = "Enter your Mobile No.";
	//private String errMsgEmail = "Enter your email id.";
	//private String errMsgPassword = "Enter your password.";
	boolean required = true;

	public static boolean isEmailAddress(EditText editText, boolean required) {
		Log.e(CLASS_TAG, "isEmailAddress()");

		String regex = "";
				//editText.getResources().getString(R.string.regex_email);

		return isValid(editText, regex, required);
	}
	public static boolean isValidPassword(EditText editText, boolean required) {
		Log.e(CLASS_TAG, "isEmailAddress()");

		//String regex = editText.getResources().getString(R.string.regex_password);

		String pass = Utils.getText(editText);
		if(pass==null||pass.length()<6||pass.contains(" ")){
			return false;
		}else
			return true;
		//return isValid(editText, regex, required);
	}


	public final static boolean isValidEmail(EditText etEmail) {
		CharSequence target = etEmail.getText();
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}
	public static boolean isPhoneNumber(EditText editText, boolean required) {
		Log.e(CLASS_TAG, "isPhoneNumber()");

		String regex = editText.getResources().getString(R.string.regex_phone);

		return isValid(editText, regex, required);
	}

	public static boolean isPhoneNumberValid(EditText editText) {
		// ZLog.log(CLASS_TAG, "isPhoneNumber()");
		boolean retVal = false;

		if (editText.getText().toString().length() > 9
				&& editText.getText().toString().length() < 15) {
			if (PhoneNumberUtils.isGlobalPhoneNumber(editText.getText()
					.toString())) {
				editText.setTextColor(VALID_TEXT_COLOR);
				return true;
			}
		}

		editText.setTextColor(INVALID_TEXT_COLOR);
		return retVal;
	}
	
	public static boolean compareText(EditText editText, EditText editText2){
		
		try{
			if(editText.getText().toString().compareTo(editText2.getText().toString())==0) {
				return true;
			}
		}catch(Exception e) {
			
		}
		editText.setError("");
		editText2.setError("");
		return false;
	}

	public static boolean isPasswordValid(EditText editText) {

		boolean retVal = false;

		if (editText.getText().toString().length() > 5) {
			editText.setTextColor(VALID_TEXT_COLOR);
			return true;

		}
		/*
		 * else if (!editText.getText().toString().trim().contains(" ")) {
		 * editText.setTextColor(VALID_TEXT_COLOR); return true; }
		 */

		editText.setTextColor(INVALID_TEXT_COLOR);
		return retVal;
	}

	public static boolean isLandlineNumberValid(EditText editText) {
		// ZLog.log(CLASS_TAG, "isPhoneNumber()");
		boolean retVal = false;

		if (editText.getText().toString().length() > 5
				&& editText.getText().toString().length() < 9) {
			if (PhoneNumberUtils.isGlobalPhoneNumber(editText.getText()
					.toString())) {
				editText.setTextColor(VALID_TEXT_COLOR);
				return true;
			}
		}

		editText.setTextColor(INVALID_TEXT_COLOR);
		return retVal;
	}

	// End Here

	public static boolean checkFirstletterValidation(EditText editText) {
		Log.e(CLASS_TAG, "check1stletterValidation()");
		editText.setTextColor(VALID_TEXT_COLOR);
		String str = editText.getText().toString();
		char element = '\0';
		char[] array = { '.', '-', '(', ')', '"', '\'', '/', ',', '`', '|',
				'\\' };
		boolean flag = true;
		for (int i = 0; i < array.length; i++) {
			element = array[i];
			if (str != null && !str.isEmpty()) {
				if (str.charAt(0) == element) {
					flag = false;
					break;
				}
			}
		}
		if (!flag) {
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			editText.setTextColor(INVALID_TEXT_COLOR);
			String errMsg = "'" + element + "' not allowed.";
			editText.setError(errMsg);
		}
		return flag;
	}

	public static boolean isValid(EditText editText, String regex,
                                  boolean required) {
		Log.e(CLASS_TAG, "isValid()");

		boolean validated = true;
		String text = editText.getText().toString().trim();
		boolean hasText = hasText(editText, "required", 0);

		// editText.setTextColor(VALID_TEXT_COLOR);

		if (required && !hasText)
			validated = false;

		if (validated && hasText) {
			if (!Pattern.matches(regex, text)) {

				validated = false;
			}
		}
		if (!validated) {
			setError(editText);
		}
		return validated;
	}

	public static boolean hasText(EditText editText, String errMsg,
                                  int minLength) {
		Log.e(CLASS_TAG, "hasText()");
		boolean validated = true;
		String text = editText.getText().toString().trim();
		editText.setError(null);

		// length 0 means there is no text
		if (text.length() < minLength || text.length() == 0) {
			if (errMsg == null)
				errMsg = "required";
			// editText.setError(errMsg);
			validated = false;
			return false;
		}

		return validated;
	}

	public static boolean isValidAddress(String address) {

		if (address.length() >= 3) {
			return true;
		}
		return false;
	}

	public static boolean isValidDescription(String description) {
		String notAllowedAsFirstChar = " ,'-.()[]/\\:;\"*+{}&=!$%?\n";
		if (description.length() != 0) {
			for (int i = 0; i < notAllowedAsFirstChar.length(); ++i) {
				if (description.startsWith(notAllowedAsFirstChar.substring(i,
						i + 1))) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isValidPrice(String bookingAmountStr) {
		long amount = Long.parseLong(bookingAmountStr);
		if (amount == 0) {
			return false;
		} // few more conditions go here
		return true;
	}

	public static boolean isValidArea(double value) {
		if (value > 0) {
			return true;
		}
		return false;
	}

	public boolean validateUserInput(final EditText name, final EditText email,
                                     final EditText mobile) {

		boolean retVal = true;

		name.setError(null);
		email.setError(null);
		mobile.setError(null);

		/*
		 * name.addTextChangedListener(new TextWatcher() { public void
		 * afterTextChanged(Editable s) { }
		 * 
		 * public void beforeTextChanged(CharSequence s, int start, int count,
		 * int after) { }
		 * 
		 * public void onTextChanged(CharSequence s, int start, int before, int
		 * count) { if (s != null && s.length() > 0 && name.getError() != null)
		 * { name.setError(null); } } });
		 * 
		 * email.addTextChangedListener(new TextWatcher() { public void
		 * afterTextChanged(Editable s) { }
		 * 
		 * public void beforeTextChanged(CharSequence s, int start, int count,
		 * int after) { }
		 * 
		 * public void onTextChanged(CharSequence s, int start, int before, int
		 * count) { if (s != null && s.length() > 0 && email.getError() != null)
		 * { email.setError(null); } } });
		 */
		if (!ZValidation.hasText(name, errMsgName, 3)) {
			retVal = false;
			// AlertUtil.messageAlert(getActivity(), "Error", errMsgName);
		}
		if (!ZValidation.isEmailAddress(email, required)) {
			retVal = false;
			// AlertUtil.messageAlert(getActivity(), "Error", errMsgEmail);
		}
		if (!ZValidation.hasText(mobile, errMsgMobile, 10)) {
			retVal = false;
			// AlertUtil.messageAlert(getActivity(), "Error", errMsgMobile);
		}

		if (!ZValidation.checkFirstletterValidation(name)) {
			retVal = false;
			// AlertUtil.messageAlert(getActivity(), "Error", errMsgName);
		}
		if (!ZValidation.checkFirstletterValidation(email)) {
			retVal = false;
			// AlertUtil.messageAlert(getActivity(), "Error", errMsgEmail);
		}
		return retVal;
	}

	public static boolean checkEmpty(EditText myEdit) {
		if (myEdit.getText().toString() == null
				|| myEdit.getText().toString().length() < 1) {
			setError(myEdit);
			return true;
		}
		return false;
	}
	public static boolean isEmpty(EditText myEdit) {
		if (myEdit.getText().toString() == null
				|| myEdit.getText().toString().length() < 1) {
			//setError(myEdit);
			return true;
		}
		return false;
	}

	public static boolean isEmpty(TextView myEdit) {
		if (myEdit.getText().toString() == null
				|| myEdit.getText().toString().length() < 1) {
			//setError(myEdit);
			return true;
		}
		return false;
	}

	public static boolean isValidFacingRoad(double width) {
		if ((width >= 0) && (width <= 999)) {
			return true;
		}
		return false;
	}

	public static void setError(EditText myEdit) {
		final EditText et = myEdit;
		myEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				android.R.drawable.stat_notify_error, 0);
		myEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
				et.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		
		// remove the error icon on key press
		myEdit.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				EditText eText = (EditText) v;

				// remove error sign
				eText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

				// remove this key handler
				eText.setOnKeyListener(null);

				return false;
			}
		});
	}

}
