package com.graphhopper.routing;

import com.graphhopper.routing.ev.StringEncodedValueImpl;
import com.graphhopper.routing.ev.StringEncodedValue;

public class DummyOwner {
    public static final String KEY = "dummy_owner";

    public static StringEncodedValue create() {
        return new StringEncodedValueImpl(KEY, 10); // max 10 chars
    }
}
