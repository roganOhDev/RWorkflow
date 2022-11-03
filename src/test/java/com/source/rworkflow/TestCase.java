package com.source.rworkflow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Collection;
import java.util.List;

@SqlGroup({
        @Sql(config = @SqlConfig(dataSource = "dataSource"), value = {"classpath:/db/drop_tables.sql", "classpath:/db/app.sql"})
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "spring.config.location=" + "classpath:/application.yaml")
@ContextConfiguration(classes = RWorkflowApplication.class)
public class TestCase {

    @Getter
    @AllArgsConstructor
    public static class Header {
        private String token;
    }

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
