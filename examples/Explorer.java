import java.util.Optional;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import java.util.Set;
import java.util.HashSet;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import net.intertextueel.conceptnet.*;
import org.apache.commons.math3.linear.RealVector;

public class Explorer {

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
            List<ConceptNetRelation> path = conceptnet.findShortestPath(start, destination, 0, false, true);
            long time_end = System.currentTimeMillis();
            if (path.isEmpty()) {
                System.out.println("NO PATHS FOUND");
            } else {
                for (ConceptNetRelation cr : path) {
                    if (cr.isNegative()) {
                        System.out.println(cr.getRelationType() + "(" + ((Concept) cr.getTarget()).toString() + ") with weight " + cr.getWeight() + " is a negative relation.");
                    } else {
                        System.out.println(cr.getRelationType() + "(" + ((Concept) cr.getTarget()).toString() + ") with weight " + cr.getWeight() + " is a positive relation.");
                    }
                }
            }
            long time_diff = time_end - time_start;
            System.out.println("TIME SPENT " + time_diff + "ms");
            time_start = System.currentTimeMillis();
            Optional<Concept> ocn_start = conceptnet.get(start);
            Optional<Concept> ocn_destination = conceptnet.get(destination);
            if (!ocn_start.isPresent()) {
                System.err.println("Start concept is not known.");
            }
            if (!ocn_destination.isPresent()) {
                System.err.println("Destination concept is not known.");
            }
            // Now do Euclidean distance
            if (ocn_start.isPresent() && ocn_destination.isPresent()) {
                Concept cn_start = ocn_start.get();
                Concept cn_destination = ocn_destination.get();
                Optional<RealVector> vector_start = cn_start.getVector();
                Optional<RealVector> vector_destination = cn_destination.getVector();
                if (!vector_start.isPresent()) {
                    System.err.println("Start concept does not contain Numberbatch information.");
                }
                if (!vector_destination.isPresent()) {
                    System.err.println("Destination concept does not contain Numberbatch information.");
                }
                if (vector_start.isPresent() && vector_destination.isPresent()) {
                    double distance = vector_start.get().getDistance(vector_destination.get());
                    System.out.println("Distance: " + distance);
                }
            }
            // Now use native library function
            Optional<Double> distance = conceptnet.getDistance(start, destination);
            if (distance.isPresent()) {
                System.out.println("Library response distance: " + distance.get());
            } else {
                System.out.println("Library response distance could no be calculated.");
            }
            // Now do cosine similarity using library
            Optional<Double> similarity = conceptnet.getCosineSimilarity(start, destination);
            if (similarity.isPresent()) {
                System.out.println("Library response cosine similarity: " + similarity.get());
            } else {
                System.out.println("Library response cosine similarity could no be calculated.");
            }

            time_end = System.currentTimeMillis();
            time_diff = time_end - time_start;
            System.out.println("TIME SPENT " + time_diff + "ms");
 
        }
    }

}
