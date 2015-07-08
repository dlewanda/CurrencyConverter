package com.davelewanda.currencyconverter;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.widget.EditText;

public class CurrencyConverterActivityTest extends ActivityInstrumentationTestCase2<CurrencyConverterActivity> {
    private CurrencyConverterActivity activity;

    public CurrencyConverterActivityTest() {
        super(CurrencyConverterActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        activity = getActivity();
    }

    @SmallTest
    public void testActivityLayoutExists() throws Exception {
        assertNotNull(activity.findViewById(R.id.activity_title));
        assertNotNull(activity.findViewById(R.id.usDollarsEditText));
        assertNotNull(activity.findViewById(R.id.list_view_title));
        assertNotNull(activity.findViewById(R.id.currencyListView));
    }

    @SmallTest
    public void testListViewLayoutExists() throws Exception {
        assertNotNull(activity.findViewById(R.id.countryTextView));
        assertNotNull(activity.findViewById(R.id.currencyTextView));
    }

    @SmallTest
    public void testUsDollarTextFieldInput() throws Exception {
        try {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EditText usDollarsEditText = (EditText) activity.findViewById(R.id.usDollarsEditText);
                    sendKeys("3 ENTER"); //user enters "3<DONE>"
                    assertEquals("3", usDollarsEditText.getText().toString());
                    usDollarsEditText.setText("");
                    sendKeys("3 3 ENTER"); //user enters "33<DONE>"
                    assertEquals("33", usDollarsEditText.getText().toString());
                    usDollarsEditText.setText("");
                    sendKeys("1 2 . 3 ENTER"); //user enters "12.3<DONE>"
                    assertEquals("123", usDollarsEditText.getText().toString()); //no decimal point accepted
                }
            });
        } catch (Throwable t) {
            Log.d(CurrencyConverterActivityTest.this.getClass().toString(), "Throwable exception: " + t.getStackTrace().toString());
        }
    }

}