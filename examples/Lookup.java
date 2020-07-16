import java.util.Optional;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import net.intertextueel.conceptnet.*;

public class Lookup {

    public static void main(String[] args) throws IOException {
        ConceptNet conceptnet = null;
        try {
            String[] languages = { "nl", "en" };
            ConceptNetFilter filter = new ConceptNetFilter(true, false, languages);
            conceptnet = new ConceptNet("/var/local/conceptnet5/conceptnet-assertions-5.7.0.csv", "", filter);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String text;
        for (;;) {
            System.out.println("INPUT CONCEPT");
            String concept_string = stdin.readLine(); 
            Optional<Concept> ocn = conceptnet.get(new Concept(concept_string));
            if (!ocn.isPresent()) {
                System.err.println("Concept is not known.");
            } else {
                Concept concept = ocn.get();

                List<ConceptNetRelation> cnr = conceptnet.getRelationsFrom(concept);
                System.out.println("Displaying from relations for concept");
                if (cnr == null) {
                    System.err.println("cnr is null");
                }
                for (ConceptNetRelation relation : cnr) {
                    if (!relation.isMeta()) {
                        Concept target = (Concept) relation.getTarget();
                        System.out.println(relation.getRelationType() + " " + target.getWordphrase());
                    } else {
                        System.err.println("Warning! Meta relation.");
                    }
                }
                System.out.println("Displaying to relations for concept");
                cnr = conceptnet.getRelationsTo(concept); 
                for (ConceptNetRelation relation : cnr) {
                    if (!relation.isMeta()) {
                        Concept target = (Concept) relation.getTarget();
                        System.out.println(relation.getRelationType() + " " + target.getWordphrase());
                    } else {
                        System.err.println("Warning! Meta relation.");
                    }
                }

            }
        }

/*
        Assertion as = new Assertion("/a/[/r/IsA/,/c/en/alphabet/,/c/en/letter/]");
        System.out.println("Investigating assertion /a/[/r/IsA/,/c/en/alphabet/,/c/en/letter/]");
        System.out.println("toString(): " + as.toString());
        System.out.println("Assertion relationtype: " + as.getRelationType());
        System.out.println("Assertion source toString(): " + as.getSourceConcept().toString());
        System.out.println("Assertion target toString(): " + as.getTargetConcept().toString());
        cnr = conceptnet.getRelationsFrom(as);
        System.out.println("Displaying from relations for assertion 'alphabet is a letter'");
        if (cnr == null) {
            System.err.println("cnr is null");
        }
        for (ConceptNetRelation relation : cnr) {
            System.out.println(relation.getRelationType() + " " + relation.getTarget().toString());
        }
        System.out.println("Displaying to relations for assertion 'alphabet is a letter'");
        cnr = conceptnet.getRelationsTo(as); 
        for (ConceptNetRelation relation : cnr) {
            System.out.println(relation.getRelationType() + " " + relation.getTarget().toString());
        }
*/
    }

}
