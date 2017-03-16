package com.github.gauravanand25.udacity.rl;

import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.domain.singleagent.graphdefined.GraphDefinedDomain;
import burlap.oomdp.auxiliary.common.NullTermination;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.behavior.statehashing.DiscreteStateHashFactory;
import burlap.behavior.statehashing.StateHashFactory;
import java.util.Random;

/**
 * Created by gaanand on 3/16/2017.
 */
public class MDPSolver {

    GraphDefinedDomain gdd;
    Domain domain;
    State initialState;     // the state in which the agent will be.
    RewardFunction rf;
    TerminalFunction tf;
    StateHashFactory hashFactory;
    int numStates;

    public MDPSolver(int numStates, int numActions, double[][][] probabilitiesOfTransitions,
                     double[][][] rewards) {

        //no explicitly defined terminal states.
        this.numStates = numStates;

        this.gdd = new GraphDefinedDomain(this.numStates);

        //set transitions
        for( int i = 0; i < probabilitiesOfTransitions.length; ++i ) {
            for( int j = 0; j < probabilitiesOfTransitions[i].length; ++j ) {
                for ( int k = 0; k < probabilitiesOfTransitions[i][j].length; ++k ) {
                    this.gdd.setTransition(i, j, k, probabilitiesOfTransitions[i][j][k]);
                }
            }
        }

        this.domain = this.gdd.generateDomain();

        //Assume random initial state; it doesn't matter.
        Random random = new Random();
        this.initialState = GraphDefinedDomain.getState(this.domain, random.nextInt(numStates));

        this.rf = new MDPRewards(rewards);

        this.tf = new NullTermination();    //it should also not matter
        this.hashFactory = new DiscreteStateHashFactory();        //???

    }

    public static class MDPRewards implements RewardFunction {
        double[][][] rewards;

        public MDPRewards(double[][][] rewards) {
            this.rewards = rewards;
        }

        @Override
        public double reward(State s, GroundedAction a, State sprime) {
            return rewards[GraphDefinedDomain.getNodeId(s)][Integer.parseInt(a.actionName().substring(6))][GraphDefinedDomain.getNodeId(sprime)];
        }

    }

    private ValueIteration computeValue(double gamma) {
        double maxDelta = 0.0001;
        int maxIterations = 1000;
        ValueIteration vi = new ValueIteration(this.domain, this.rf, this.tf, gamma, this.hashFactory, maxDelta, maxIterations);
        vi.planFromState(this.initialState);     //runs value iteration; greedily selects the action with the highest Q-value and breaks ties uniformly randomly.
        return vi;
    }
    public double valueOfState(int state, double gamma) {
        ValueIteration valueFunction = this.computeValue(gamma);
        return valueFunction.value(this.gdd.getState(this.domain, state));
    }
}
