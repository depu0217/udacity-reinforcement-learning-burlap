package com.github.gauravanand25.udacity.rl;

import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.planning.commonpolicies.GreedyQPolicy;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.domain.singleagent.graphdefined.GraphDefinedDomain;
import burlap.domain.singleagent.graphdefined.GraphTF;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.behavior.statehashing.DiscreteStateHashFactory;
import burlap.behavior.statehashing.StateHashFactory;

/**
 * Created by Gaurav Anand on 3/15/17.
 */
public class SecondMDP {

    GraphDefinedDomain gdd;
    Domain domain;
    State initialState;     // the state in which the agent will be.
    RewardFunction rf;
    TerminalFunction tf;
    StateHashFactory hashFactory;
    int numStates = 6;

    public SecondMDP(double p1, double p2) {
        this.gdd = new GraphDefinedDomain(this.numStates);

        //actions from initial state 0
        this.gdd.setTransition(0, 1, 0, p1);
        this.gdd.setTransition(0, 1, 1, 1.-p1);
        this.gdd.setTransition(0, 2, 2, 1.);

        //actions from state 1
        this.gdd.setTransition(1, 3, 3, 1.-p2);
        this.gdd.setTransition(1, 4, 4, 1.);
        this.gdd.setTransition(1, 3, 5, p2);

        //transitions from state 2
        this.gdd.setTransition(2, 0, 1, 1.);

        //transitions from state 3
        this.gdd.setTransition(3, 0, 1, 1.);

        //transitions from state 4
        this.gdd.setTransition(4, 0, 5, 1.);

        this.domain = this.gdd.generateDomain();
        this.initialState = GraphDefinedDomain.getState(this.domain, 0);
        this.rf = new FixedParamRF();

        this.tf = new GraphTF(5);    //terminal state is 5
        this.hashFactory = new DiscreteStateHashFactory();        //???
    }

    public static class FixedParamRF implements RewardFunction {

        @Override
        public double reward(State s, GroundedAction a, State sprime) {
            if(GraphDefinedDomain.getNodeId(s) == 0 && GraphDefinedDomain.getNodeId(sprime) == 1) {
                return 3.0f;
            } else if(GraphDefinedDomain.getNodeId(s) == 0 && GraphDefinedDomain.getNodeId(sprime) == 0) {
                return -1.0f;
            } else if(GraphDefinedDomain.getNodeId(s) == 0 && GraphDefinedDomain.getNodeId(sprime) == 2) {
                return 1.0f;
            } else if(GraphDefinedDomain.getNodeId(s) == 1 && GraphDefinedDomain.getNodeId(sprime) == 3) {
                return 1.0f;
            } else if(GraphDefinedDomain.getNodeId(s) == 1 && GraphDefinedDomain.getNodeId(sprime) == 4) {
                return 2.0f;
            } else {
                return 0.0f;
            }
        }

    }

    private GreedyQPolicy computeValue(double gamma) {
        double maxDelta = 0.0001;
        int maxIterations = 1000;
        ValueIteration vi = new ValueIteration(this.domain, this.rf, this.tf, gamma, this.hashFactory, maxDelta, maxIterations);
        vi.planFromState(this.initialState);     //runs value iteration; greedily selects the action with the highest Q-value and breaks ties uniformly randomly.
        GreedyQPolicy pi = new GreedyQPolicy(vi);
        return pi;
    }
    public String bestActions(double gamma) {
        // Return one of the following Strings
        // "a,c"
        // "a,d"
        // "b,c"
        // "b,d"
        // based on the optimal actions at states S0 and S1. If
        // there is a tie, break it in favor of the letter earlier in
        // the alphabet (so if "a,c" and "a,d" would both be optimal,
        // return "a,c").
        GreedyQPolicy policy = this.computeValue(gamma);
        String bestS0Action = policy.getAction(this.gdd.getState(this.domain, 0)).actionName().replaceAll("action1", "a").replaceAll("action2", "b");
        String bestS1Action = policy.getAction(this.gdd.getState(this.domain, 1)).actionName().replaceAll("action3", "c").replaceAll("action4", "d");

        return bestS0Action+","+bestS1Action;
    }

    public static void main(String[] args) {
        double p1 = 0.5;
        double p2 = 0.5;
        SecondMDP mdp = new SecondMDP(p1,p2);

        double gamma = 0.5;
        System.out.println("Best actions: " + mdp.bestActions(gamma));
    }
}}