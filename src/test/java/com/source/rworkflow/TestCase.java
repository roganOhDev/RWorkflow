package com.source.rworkflow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collection;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = RWorkflowApplication.class)
public class TestCase {

    public static DynamicNode single(final String displayName, final Executable executable) {
        return DynamicTest.dynamicTest(displayName, executable);
    }

    public static Collection<DynamicNode> group(DynamicNode... dynamicNodes) {
        return List.of(dynamicNodes);
    }

    public static DynamicNode group(final String displayName, DynamicNode... dynamicNodes) {
        return DynamicContainer.dynamicContainer(displayName, List.of(dynamicNodes));
    }

    public static DynamicNode group(final String displayName, Collection<DynamicNode> dynamicNodes) {
        return DynamicContainer.dynamicContainer(displayName, dynamicNodes);
    }
}
