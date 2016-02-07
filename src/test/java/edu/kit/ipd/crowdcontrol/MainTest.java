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
        String[] args = new String[2];
        args[0] = "/example_config_without_Pool.properties";
        args[1] = "true";
        Main.main(args);
    }

    @Test(expected = RuntimeException.class)
    public void testMainCanParsePoolExample() throws Exception {
        String[] args = new String[2];
        args[0] = "/conf/example_config.properties";
        args[1] = "true";
        Main.main(args);
    }
}