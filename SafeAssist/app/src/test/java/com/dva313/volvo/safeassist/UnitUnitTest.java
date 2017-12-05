package com.dva313.volvo.safeassist;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Context;
import android.test.mock.MockContext;

/**
 * Created by Rickard on 2017-12-05.
 */

public class UnitUnitTest {

    private Unit unit = null;
    private Context mContext = null;

    @Before
    public void beforeEachTest() {
        mContext = new MockContext();
        unit = new Unit(mContext);
    }

    @Test
    public void login_isCorrect() throws Exception {
        assertEquals(true, unit.login("u", "p"));
    }

}