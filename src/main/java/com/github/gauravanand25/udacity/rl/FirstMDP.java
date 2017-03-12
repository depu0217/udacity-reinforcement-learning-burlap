package com.github.gauravanand25.udacity.rl;

import burlap.domain.singleagent.graphdefined.GraphDefinedDomain;
import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;


/**
 * Created by Gaurav Anand on 3/10/2017.
 */
public class FirstMDP {

    DomainGenerator dg;
    Domain domain;
    State initialState;

    public FirstMDP() {
        int numStates = 6;
        this.dg = new GraphDefinedDomain(numStates);
        this.domain = this.dg.generateDomain();
        this.initialState = GraphDefinedDomain.getState(this.domain, 0);
    }
}
