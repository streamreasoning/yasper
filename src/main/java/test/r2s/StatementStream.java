package test.r2s;

import it.polimi.rsp.core.rsp.RSPQLEngine;
import it.polimi.rsp.core.rsp.stream.item.jena.StatementStimulus;
import lombok.AllArgsConstructor;
import org.apache.jena.rdf.model.*;

/**
 * Created by Riccardo on 13/08/16.
 */
@AllArgsConstructor
public class StatementStream implements Runnable {

    RSPQLEngine e;
    private String name;
    private String stream_uri;
    private int grow_rate;

    @Override
    public void run() {


        int i = 1;
        int j = 1;
        while (true) {
            Model m = ModelFactory.createDefaultModel();
            Property predicate = ResourceFactory.createProperty("http://somewhere/num");
            Literal object = m.createTypedLiteral(new Integer(i * 1000));
            Resource subject = ResourceFactory.createResource("http://somewhere/" + name + j);
            StatementStimulus t = new StatementStimulus(i * 1000, ResourceFactory.createStatement(subject, predicate, object), stream_uri);
            System.out.println("[" + System.currentTimeMillis() + "] Sending [" + t + "] on " + stream_uri + " at " + i * 1000);
            e.process(t);
            try {
                Thread.sleep(grow_rate * 998);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i += grow_rate;
            j++;
        }

    }
}
