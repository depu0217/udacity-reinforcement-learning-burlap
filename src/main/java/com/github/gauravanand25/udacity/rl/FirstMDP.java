package com.github.gauravanand25.udacity.rl;

import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.domain.singleagent.graphdefined.GraphDefinedDomain;
import burlap.oomdp.auxiliary.common.NullTermination;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.statehashing.DiscretizingHashableStateFactory;
import burlap.oomdp.statehashing.HashableStateFactory;


/**
 * Created by Gaurav Anand on 3/10/2017.
 */
public class FirstMDP {

    GraphDefinedDomain			gdd;
    Domain domain;
    State initialState;     // the state in which the agent will be.
    RewardFunction rf;
    TerminalFunction tf;
    HashableStateFactory hashFactory;
    int numStates = 6;

    public FirstMDP(double p1, double p2, double p3, double p4) {

        this.gdd = new GraphDefinedDomain(this.numStates);

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

        this.tf = new NullTermination();    //never ends; no terminal state
        this.hashFactory = new DiscretizingHashableStateFactory(0.);        //???
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

    private ValueIteration computeValue(double gamma) {
        double maxDelta = 0.0001;
        int maxIterations = 1000;
        ValueIteration vi = new ValueIteration(this.domain, this.rf, this.tf, gamma, this.hashFactory, maxDelta, maxIterations);
        GreedyQPolicy pi = vi.planFromState(this.initialState);     //runs value iteration; greedily selects the action with the highest Q-value and breaks ties uniformly randomly.
        return vi;
    }

    public String bestFirstAction(double gamma) {
        // Return "action a" if a is the best action based on the discount factor given.
        // Return "action b" if b is the best action based on the discount factor given.
        // Return "action c" if c is the best action based on the discount factor given.
        // If there is a tie between actions, give preference to the earlier action in the alphabet:
        //   e.g., if action a and action c are equally good, return "action a".
        ValueIteration valueFunction = this.computeValue(gamma);

        double[] utility = new double[this.numStates];
        for( int i = 0; i < utility.length; ++i ) {
            State s = this.gdd.getState(this.domain, i);
            utility[i] = valueFunction.value(s);
        }

        String ret = "";
        double bestUtility = Double.MIN_VALUE;
        for( int i = 1; i < 4; ++i ) {
            if( utility[i] > bestUtility) {
                bestUtility = utility[i];
                ret = Character.toString((char)(96+i));
            }
        }
        ret = "action " + ret;
        return ret;
    }

}
