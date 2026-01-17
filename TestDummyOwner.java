import com.graphhopper.GraphHopper;
import com.graphhopper.config.Profile;
import com.graphhopper.routing.ev.BooleanEncodedValue;
import com.graphhopper.routing.ev.DummyOwner;
import com.graphhopper.storage.BaseGraph;
import com.graphhopper.util.EdgeIteratorState;

public class TestDummyOwner {
    public static void main(String[] args) {
        String osmFile = args.length > 0 ? args[0] : "core/files/andorra.osm.pbf";
        String graphLocation = "test-dummy-gh";
        
        GraphHopper hopper = new GraphHopper();
        hopper.setOSMFile(osmFile);
        hopper.setGraphHopperLocation(graphLocation);
        hopper.setProfiles(new Profile("car"));
        hopper.setEncodedValuesString("dummy_owner");
        hopper.importOrLoad();
        
        BaseGraph graph = hopper.getBaseGraph();
        BooleanEncodedValue dummyEnc = hopper.getEncodingManager().getBooleanEncodedValue(DummyOwner.KEY);
        
        // Check a few edges
        int checked = 0;
        int trueCount = 0;
        var iter = graph.createEdgeExplorer().setBaseNode(0);
        while (iter.next() && checked < 10) {
            boolean dummyValue = iter.get(dummyEnc);
            System.out.println("Edge " + iter.getEdge() + ": dummy_owner=" + dummyValue);
            if (dummyValue) trueCount++;
            checked++;
        }
        
        System.out.println("\nChecked " + checked + " edges, " + trueCount + " had dummy_owner=true");
        System.out.println("Total graph edges: " + graph.getEdges());
        
        hopper.close();
    }
}
