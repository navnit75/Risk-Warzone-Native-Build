package org.Model;
import org.Model.Phases.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)

@Suite.SuiteClasses({
        MapTest.class,
        StartUpPhaseTest.class,
        IssueOrderPhaseTest.class,
        OrderExecutionPhaseTest.class
})
public class JUnitTestSuite {
}



