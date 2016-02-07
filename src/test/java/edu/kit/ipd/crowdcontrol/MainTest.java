package edu.kit.ipd.crowdcontrol;

import edu.kit.ipd.crowdcontrol.workerservice.Main;
import org.junit.Test;

/**
 * @author LeanderK
 * @version 1.0
 */
public class MainTest {

    @Test
    public void testMainCanParseExample() throws Exception {
        System.setProperty("workerservice.config", "/example_config_without_Pool.properties");
        String[] args = new String[1];
        args[0] = "true";
        Main.main(args);
    }

    @Test(expected = RuntimeException.class)
    public void testMainCanParsePoolExample() throws Exception {
        System.setProperty("workerservice.config", "/example_config_with_Pool.properties");
        String[] args = new String[1];
        args[0] = "true";
        Main.main(args);
    }
}