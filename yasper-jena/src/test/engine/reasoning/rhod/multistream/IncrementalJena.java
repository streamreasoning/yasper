package engine.reasoning.rhod.multistream;

import it.polimi.jasper.engine.JenaRSPQLEngineImpl;
import it.polimi.jasper.engine.query.RSPQuery;
import it.polimi.jasper.engine.query.formatter.ResponseFormatterFactory;
import it.polimi.jasper.parser.RSPQLParser;
import it.polimi.yasper.core.enums.Entailment;
import it.polimi.yasper.core.enums.Maintenance;
import it.polimi.yasper.core.query.execution.ContinuousQueryExecution;
import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.system.IRIResolver;
import org.parboiled.Parboiled;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import engine.GraphStream;

import java.io.File;
import java.io.IOException;

/**
 * Created by Riccardo on 03/08/16.
 */
public class IncrementalJena {

    public static void main(String[] args) throws InterruptedException, IOException {


        RSPQuery q = getRspQuery();
        // RSPQuery q1 = getRspQuery();

        JenaRSPQLEngineImpl sr = new JenaRSPQLEngineImpl(0);
        sr.startProcessing();
        ContinuousQueryExecution cqe = sr.registerQuery(q, ModelFactory.createDefaultModel(), Maintenance.INCREMENTAL, Entailment.RHODF, false);

        // SDS sds = sr.getSDS(q);
        // sr.registerQuery(q1, sds);

        //executes a query creating the SDS (if it does not exists yet)
        // ContinuousQueryExecutionImpl cqe = je.registerQuery(q, SDS); //executes the query on the given SDS (if compatible)

        if (q.isSelectType())
            sr.registerObserver(q.getName(), ResponseFormatterFactory.getSelectResponseSysOutFormatter(true)); // attaches a new *RSP-QL query to the SDS
        if (q.isConstructType())
            sr.registerObserver(q.getName(), ResponseFormatterFactory.getConstructResponseSysOutFormatter(true)); // attaches a new *RSP-QL query to the SDS


        (new Thread(new GraphStream("Painter", "http://streamreasoning.org/iminds/massif/stream1", 1))).start();

    }

    private static RSPQuery getRspQuery() throws IOException {
        String input = getInput();

        RSPQLParser parser = Parboiled.createParser(RSPQLParser.class);

        parser.setResolver(IRIResolver.create());

        ParsingResult<RSPQuery> result = new ReportingParseRunner(parser.Query()).run(input);

        if (result.hasErrors()) {
            for (ParseError arg : result.parseErrors) {
                System.out.println(input.substring(0, arg.getStartIndex()) + "|->" + input.substring(arg.getStartIndex(), arg.getEndIndex()) + "<-|" + input.substring(arg.getEndIndex() + 1, input.length() - 1));
            }
        }
        return result.resultValue;
    }

    public static String getInput() throws IOException {
        File file = new File("/Users/riccardo/_Projects/RSP/RSP-Baselines/src/it.polimi.jasper.test/resources/q52.rspql");
        return FileUtils.readFileToString(file);
    }
}
