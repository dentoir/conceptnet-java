import java.util.Optional;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.Comparator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import net.intertextueel.conceptnet.*;
import org.apache.commons.math3.linear.RealVector;

public class NearestNeighbors {

    public static void main(String[] args) throws IOException {

        ConceptNet conceptnet = null;
        String[] languages = { "nl" };
        ConceptNetFilter filter = new ConceptNetFilter(false, false, languages);
        try {
            conceptnet = new ConceptNet("/var/local/conceptnet5/conceptnet-assertions-5.5.5.csv", "/var/local/conceptnet5/numberbatch-17.06.txt", filter, true);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("READY");
        
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String text;
        for (;;) {
            System.out.println("INPUT CONCEPT");
            String concept_string = stdin.readLine(); 
            long time_start = System.currentTimeMillis();
            Optional<Concept> ocn = conceptnet.get(new Concept(concept_string));
            if (!ocn.isPresent()) {
                System.err.println("Concept is not known.");
            } else {
                Concept concept = ocn.get();
                // TODO BUGFIX (IN LIBRARY?): there appear to be duplicate entries coming out of this conceptnet.stream()
                List<Concept> concepts_with_vectors = conceptnet.parallelStream().filter(c -> c.getVector().isPresent()).collect(Collectors.toList());
                List<Concept> nns = concepts_with_vectors.parallelStream().sorted(Comparator.comparingDouble(c -> c.getVector().get().getDistance(concept.getVector().get()))).collect(Collectors.toList());
                int printed = 0;
                for (Concept nn : nns) {
                    Double distance = nn.getVector().get().getDistance(concept.getVector().get());
                    System.out.println(String.format("%5.3f %s", distance, nn));
                    if (printed++ == 15) { break; }
                }
            }

            long time_end = System.currentTimeMillis();
            long time_diff = time_end - time_start;
            System.out.println("TIME SPENT " + time_diff + "ms");
 
        }
    }

}
