import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.config.Profile;
import com.graphhopper.routing.ev.BooleanEncodedValue;
import com.graphhopper.routing.ev.DummyOwner;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;

public class TestDummyRoute {
    public static void main(String[] args) {
        // Setup GraphHopper
        GraphHopper hopper = new GraphHopper();
        hopper.setOSMFile("enriched_routing_input.pbf");
        hopper.setGraphHopperLocation("test-gh");
        hopper.setProfiles(new Profile("car"));
        
        System.out.println("Importing OSM data...");
        hopper.importOrLoad();
        
        BooleanEncodedValue dummyEnc = hopper.getEncodingManager()
            .getBooleanEncodedValue(DummyOwner.KEY);
        
        // Make a routing request (adjust coordinates for your data)
        System.out.println("\nMaking routing request...");
        GHRequest req = new GHRequest(42.5, 1.5, 42.6, 1.6).setProfile("car");
        GHResponse rsp = hopper.route(req);
        
        if (rsp.hasErrors()) {
            System.out.println("Errors: " + rsp.getErrors());
            System.out.println("\nTry different coordinates that exist in your OSM file");
        } else {
            ResponsePath path = rsp.getBest();
            System.out.println("Route found! Distance: " + path.getDistance() + "m");
            System.out.println("Edges in route: " + path.getEdges().size());
            System.out.println("\nChecking dummy_owner on route edges:");
            
            int edgeNum = 0;
            for (EdgeIteratorState edge : path.getEdges()) {
                boolean dummy = edge.get(dummyEnc);
                System.out.println("  Edge " + edgeNum++ + " (id=" + edge.getEdge() + "): dummy_owner=" + dummy);
            }
        }
        
        hopper.close();
    }
}
