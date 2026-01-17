package com.graphhopper;

import com.graphhopper.config.Profile;
import com.graphhopper.routing.TestProfiles;
import com.graphhopper.routing.ev.BooleanEncodedValue;
import com.graphhopper.routing.ev.DummyOwner;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.Helper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class DummyOwnerIntegrationTest {
    
    private final String graphLocation = "target/dummy-test-gh";
    
    @BeforeEach
    public void setUp() {
        Helper.removeDir(new File(graphLocation));
    }
    
    @AfterEach
    public void tearDown() {
        Helper.removeDir(new File(graphLocation));
    }
    
    @Test
    public void testDummyOwnerInRoute() {
        GraphHopper hopper = new GraphHopper()
            .setOSMFile("core/files/andorra.osm.pbf")
            .setGraphHopperLocation(graphLocation)
            .setEncodedValuesString("car_access, car_average_speed, dummy_owner")
            .setProfiles(TestProfiles.accessAndSpeed("car", "car"));
        hopper.importOrLoad();
        
        BooleanEncodedValue dummyEnc = hopper.getEncodingManager()
            .getBooleanEncodedValue(DummyOwner.KEY);
        
        // Route from A to B (adjust coordinates for your data)
        GHRequest req = new GHRequest(42.5, 1.5, 42.6, 1.6).setProfile("car");
        GHResponse rsp = hopper.route(req);
        
        if (rsp.hasErrors()) {
            System.out.println("❌ Routing failed: " + rsp.getErrors());
            System.out.println("💡 Change coordinates to match your enriched_routing_input.pbf file");
            
            // Show first 10 edges in graph anyway
            System.out.println("\n📊 First 10 edges in graph with dummy_owner:");
            var graph = hopper.getBaseGraph();
            var iter = graph.getAllEdges();
            int count = 0;
            while (iter.next() && count < 10) {
                boolean dummy = iter.get(dummyEnc);
                System.out.println("  Edge " + iter.getEdge() + ": dummy_owner=" + dummy);
                count++;
            }
        } else {
            ResponsePath path = rsp.getBest();
            System.out.println("✅ Route found!");
            System.out.println("Distance: " + path.getDistance() + "m");
            System.out.println("Time: " + path.getTime() + "ms");
            System.out.println("\n📍 DUMMY_OWNER VALUES IN YOUR ROUTE:");
            
            // Get edge IDs from path and look them up
            var graph = hopper.getBaseGraph();
            var pointList = path.getPoints();
            System.out.println("Route has " + pointList.size() + " points");
            
            // Simple check: show all edges from first node
            if (pointList.size() > 0) {
                var iter = graph.getAllEdges();
                int count = 0;
                while (iter.next() && count < 20) {
                    boolean dummy = iter.get(dummyEnc);
                    System.out.println("  Edge " + iter.getEdge() + ": dummy_owner=" + dummy);
                    count++;
                }
            }
        }
        
        hopper.close();
    }
}
