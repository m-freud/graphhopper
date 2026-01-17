/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.routing.util.parsers;

import com.graphhopper.reader.ReaderWay;
import com.graphhopper.routing.ev.ArrayEdgeIntAccess;
import com.graphhopper.routing.ev.BooleanEncodedValue;
import com.graphhopper.routing.ev.DummyOwner;
import com.graphhopper.routing.ev.EncodedValue;
import com.graphhopper.storage.IntsRef;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OSMDummyOwnerParserTest {

    @Test
    void testBasic() {
        BooleanEncodedValue dummyEnc = DummyOwner.create();
        dummyEnc.init(new EncodedValue.InitializerConfig());
        OSMDummyOwnerParser parser = new OSMDummyOwnerParser(dummyEnc);
        
        ReaderWay way = new ReaderWay(1);
        way.setTag("highway", "primary");
        
        ArrayEdgeIntAccess edgeIntAccess = new ArrayEdgeIntAccess(1);
        int edgeId = 0;
        parser.handleWayTags(edgeId, edgeIntAccess, way, new IntsRef(2));
        
        assertTrue(dummyEnc.getBool(false, edgeId, edgeIntAccess));
    }
}
