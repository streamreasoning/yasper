package it.polimi.rsp.baselines.esper;

import it.polimi.heaven.core.teststand.EventProcessor;
import it.polimi.heaven.core.teststand.rspengine.RSPEngine;
import it.polimi.heaven.core.teststand.rspengine.events.Response;
import it.polimi.heaven.core.teststand.rspengine.events.Stimulus;
import lombok.Getter;
import lombok.Setter;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationMethodRef;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;

@Getter
public abstract class RSPEsperEngine implements RSPEngine {

	protected Configuration cepConfig;
	protected EPServiceProvider cep;
	protected EPRuntime cepRT;
	protected EPAdministrator cepAdm;
	protected ConfigurationMethodRef ref;

	protected EventProcessor<Response> next;

	@Setter
	protected Stimulus currentEvent = null;
	protected long sentTimestamp;

	protected int rspEventsNumber = 0, esperEventsNumber = 0;
	protected long currentTimestamp;

	public RSPEsperEngine(EventProcessor<Response> next, Configuration config) {
		this.next = next;
		this.cepConfig = config;
		this.currentTimestamp = 0L;
	}

}