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
import com.graphhopper.routing.ev.BooleanEncodedValue;
import com.graphhopper.routing.ev.EdgeIntAccess;
import com.graphhopper.storage.IntsRef;

public class OSMDummyOwnerParser implements TagParser {

    private final BooleanEncodedValue dummyOwnerEnc;

    public OSMDummyOwnerParser(BooleanEncodedValue dummyOwnerEnc) {
        this.dummyOwnerEnc = dummyOwnerEnc;
    }

    @Override
    public void handleWayTags(int edgeId, EdgeIntAccess edgeIntAccess, ReaderWay readerWay, IntsRef relationFlags) {
        // For now, set dummy_owner=true for all edges as a test
        // Later you can add logic like:
        // if (readerWay.hasTag("some_osm_tag", "some_value"))
        //     dummyOwnerEnc.setBool(false, edgeId, edgeIntAccess, true);
        
        dummyOwnerEnc.setBool(false, edgeId, edgeIntAccess, true);
    }
}
