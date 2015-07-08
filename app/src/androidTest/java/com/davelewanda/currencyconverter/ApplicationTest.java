package com.davelewanda.currencyconverter;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
//@RunWith(RobolectricGradleTestRunner.class)
//@Config(constants = BuildConfig.class, emulateSdk = 22)
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    //    @SmallTest
    public void testPreconditions() {
    }

    /**
     * Test basic startup/shutdown of Application
     */
//    @MediumTest
    public void testSimpleCreate() {
        createApplication();
//        assertNotNull(getApplication().getA);
    }
}