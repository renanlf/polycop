package edu.br.ufpe.cin.sword.cm.alchb.states;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbTerm;
import edu.br.ufpe.cin.sword.cm.alchb.model.ALCHbVariable;

public class ALCHbConnState {
	
	private final Map<ALCHbVariable, ALCHbTerm> subs;

	public ALCHbConnState(Map<ALCHbVariable, ALCHbTerm> subs) {
		this.subs = Collections.unmodifiableMap(new HashMap<>(subs));
	}

	public Map<ALCHbVariable, ALCHbTerm> getSubs() {
		return subs;
	}
	
}
