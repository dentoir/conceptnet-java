import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import java.util.Set;
import java.util.HashSet;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import net.intertextueel.conceptnet.*;

public class FindPaths {

    public static void main(String[] args) throws IOException {
        ConceptNet conceptnet = null;
        String[] languages = { "nl", "en" };
        ConceptNetFilter filter = new ConceptNetFilter(false, false, languages);
        try {
            conceptnet = new ConceptNet("/var/local/conceptnet5/conceptnet-assertions-5.7.0.csv", "/var/local/conceptnet5/numberbatch-19.08.txt", filter, true);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("READY");
        
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String text;
        for (;;) {
            System.out.println("INPUT CONCEPT");
            String text_start = stdin.readLine(); 
            System.out.println("DESTINATION CONCEPT");
            String text_destination = stdin.readLine(); 
            long time_start = System.currentTimeMillis();
            Concept start = new Concept(text_start);
            Concept destination = new Concept(text_destination);
            List<List<ConceptNetRelation>> paths = conceptnet.findOptimalPaths(start, destination, 4, false);
            long time_end = System.currentTimeMillis();
            if (paths.isEmpty()) {
                System.out.println("NO PATHS FOUND");
            } else {
                for (List<ConceptNetRelation> path : paths) {
                    System.out.println("== NEW PATH");
                    for (ConceptNetRelation cr : path) {
                        if (cr.isNegative()) {
                            System.out.println(cr.getRelationType() + "(" + ((Concept) cr.getTarget()).toString() + ") with weight " + cr.getWeight() + " is a negative relation.");
                        } else {
                            System.out.println(cr.getRelationType() + "(" + ((Concept) cr.getTarget()).toString() + ") with weight " + cr.getWeight() + " is a positive relation.");
                        }
                    }
                    System.out.println("== END OF PATH");
                }
            }
            long time_diff = time_end - time_start;
            System.out.println("TIME SPENT " + time_diff + "ms");
        }
    }

}
