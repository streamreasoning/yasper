package it.polimi.yasper.core.timevarying;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import it.polimi.yasper.core.enums.Maintenance;
import it.polimi.yasper.core.enums.Report;
import it.polimi.yasper.core.query.InstantaneousItem;
import it.polimi.yasper.core.query.operators.s2r.WindowOperator;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import java.util.HashSet;
import java.util.Set;

@Log4j
@Getter
public class DefaultTVG extends TimeVaryingGraph {

    private Set<WindowOperator> windowOperatorSet;

    public DefaultTVG(Maintenance maintenance, InstantaneousItem g) {
        super(maintenance, g, null);
        this.windowOperatorSet = new HashSet<>();
    }

    @Override
    public synchronized void update(EventBean[] newData, EventBean[] oldData, EPStatement stmt, EPServiceProvider esp) {
        log.info("[" + Thread.currentThread() + "][" + System.currentTimeMillis() + "] FROM STATEMENT: " + stmt.getText() + " AT "
                + esp.getEPRuntime().getCurrentTime());

        super.update(newData, oldData, stmt, esp);

        notifyObservers(Report.WINDOW_CLOSE);

    }

    public void addStatement(WindowOperator wo) {
        windowOperatorSet.add(wo);
        wo.addListener(this);
    }

    @Override
    public WindowOperator getTriggeringStatement() {
        return window_operator;
    }


}