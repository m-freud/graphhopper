package com.graphhopper;

import com.graphhopper.config.Profile;
import com.graphhopper.routing.TestProfiles;
import com.graphhopper.routing.ev.BooleanEncodedValue;
import com.graphhopper.routing.ev.DummyOwner;
import com.graphhopper.util.*;

/**
 * Simple test: Import your PBF, route, and save the response with dummy_owner to route.json
 */
public class SimpleDummyTest {
    public static void main(String[] args) throws Exception {
        // 1. Start GraphHopper with your file
        System.out.println("🚀 Starting GraphHopper...");
        GraphHopper hopper = new GraphHopper()
            .setOSMFile("./enriched_routing_input.osm")  // Your OSM file at root
            .setGraphHopperLocation("test-gh")
            .setEncodedValuesString("car_access, car_average_speed, dummy_owner")
            .setProfiles(TestProfiles.accessAndSpeed("car", "car"));
        
        hopper.importOrLoad();
        System.out.println("✅ Graph loaded! Total edges: " + hopper.getBaseGraph().getEdges());
        
        BooleanEncodedValue dummyEnc = hopper.getEncodingManager()
            .getBooleanEncodedValue(DummyOwner.KEY);
        
        // 2. Send routing request (CHANGE THESE COORDINATES TO MATCH YOUR FILE!)
        System.out.println("\n📍 Routing...");
        GHRequest req = new GHRequest(42.5, 1.5, 42.6, 1.6)
            .setProfile("car")
            .setPathDetails(java.util.Arrays.asList("dummy_owner"));  // Request dummy_owner in details
        
        GHResponse rsp = hopper.route(req);
        
        if (rsp.hasErrors()) {
            System.out.println("❌ Routing failed: " + rsp.getErrors());
            System.out.println("\n💡 Fix: Open your enriched_routing_input.pbf in JOSM or similar");
            System.out.println("   Find two coordinates and update lines 25-26 in SimpleDummyTest.java");
        } else {
            // 3. Print route with dummy_owner values
            ResponsePath path = rsp.getBest();
            System.out.println("✅ Route found!");
            System.out.println("   Distance: " + path.getDistance() + "m");
            System.out.println("   Time: " + (path.getTime() / 1000) + "s");
            
            // Show dummy_owner from graph edges
            System.out.println("\n📊 Checking first 20 edges for dummy_owner:");
            var iter = hopper.getBaseGraph().getAllEdges();
            int count = 0;
            while (iter.next() && count < 20) {
                boolean dummy = iter.get(dummyEnc);
                System.out.println("   Edge " + iter.getEdge() + ": dummy_owner=" + dummy);
                count++;
            }
            
            // 4. Save to route.json
            String json = new com.fasterxml.jackson.databind.ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(rsp);
            
            java.nio.file.Files.writeString(
                java.nio.file.Paths.get("route.json"), 
                json
            );
            
            System.out.println("\n💾 Saved response to route.json");
            System.out.println("   (Note: dummy_owner is in the graph, but path details need custom serialization)");
        }
        
        hopper.close();
        System.out.println("\n✨ Done!");
    }
}
