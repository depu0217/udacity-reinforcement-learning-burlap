package com.github.gauravanand25.udacity.rl;

import burlap.domain.singleagent.graphdefined.GraphDefinedDomain;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;


/**
 * Created by Gaurav Anand on 3/10/2017.
 */
public class FirstMDP {

    GraphDefinedDomain			gdd;
    Domain domain;
    State initialState;     // the state in which the agent will be.
    RewardFunction rf;

    public FirstMDP(double p1, double p2, double p3, double p4) {
        int numStates = 6;
        this.gdd = new GraphDefinedDomain(numStates);

        //actions from initial state 0
        this.gdd.setTransition(0, 0, 1, 1.);
        this.gdd.setTransition(0, 1, 2, 1.);
        this.gdd.setTransition(0, 2, 3, 1.);

        //transitions from action "a" outcome state
        this.gdd.setTransition(1, 0, 1, 1.);

        //transitions from action "b" outcome state
        this.gdd.setTransition(2, 0, 4, 1.);
        this.gdd.setTransition(4, 0, 2, 1.);

        //transitions from action "c" outcome state
        this.gdd.setTransition(3, 0, 5, 1.);
        this.gdd.setTransition(5, 0, 5, 1.);

        this.domain = this.gdd.generateDomain();
        this.initialState = GraphDefinedDomain.getState(this.domain, 0);
        this.rf = new FourParamRF(p1,p2,p3,p4);
    }

    public static class FourParamRF implements RewardFunction {
        double p1;
        double p2;
        double p3;
        double p4;

        public FourParamRF(double p1, double p2, double p3, double p4) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
            this.p4 = p4;
        }

        @Override
        public double reward(State s, GroundedAction a, State sprime) {
            switch (GraphDefinedDomain.getNodeId(s)) {
                case 1:  return this.p1;
                case 2: return this.p2;
                case 4: return this.p3;
                case 5: return this.p4;
                default: return 0.0f;
            }
        }

    }
}
