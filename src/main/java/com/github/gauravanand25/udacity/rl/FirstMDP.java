package com.github.gauravanand25.udacity.rl;

import burlap.domain.singleagent.graphdefined.GraphDefinedDomain;
import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.Domain;

/**
 * Created by gaanand on 3/10/2017.
 */
public class FirstMDP {

    DomainGenerator dg;
    Domain domain;

    public FirstMDP() {
        int numStates = 6;
        this.dg = new GraphDefinedDomain(numStates);
        this.domain = this.dg.generateDomain();
    }
}
