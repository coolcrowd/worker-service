package edu.ipd.kit.crowdcontrol.workerservice.strategies;

import java.util.HashMap;

/**
 * @author LeanderK
 * @version 1.0
 */
public class Strategies {
    private HashMap<String, Strategy> strategies =  new HashMap<>();

    public Strategies() {

    }

    public void registerStrategy(Strategy strategy) {
        strategies.put(strategy.getName(), strategy);
    }
}
