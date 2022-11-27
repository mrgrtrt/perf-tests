import org.apache.http.entity.ContentType
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import us.abstracta.jmeter.javadsl.JmeterDsl.*

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostTodoPerformanceTest {

    companion object {

        @Container
        private val app = GenericContainer(DockerImageName.parse("todo-app"))
            .withExposedPorts(4242)
    }

    @Test
    fun `verify create TODO performance`() {
        val stats = testPlan(
            threadGroup(
                2, 100,
                httpSampler("http://localhost:${app.getMappedPort(4242)}/todos")
                    .post(
                        "{\n" +
                                "    \"id\": \${__Random(1,9999)},\n" +
                                "    \"text\": \"TestText\${__Random(1,9999)}\",\n" +
                                "    \"completed\": false\n" +
                                "}", ContentType.APPLICATION_JSON
                    )
            ),
            jtlWriter("build/jtls"),
            htmlReporter("build/reports")
        ).run()

        assertTrue(stats.overall().sampleTimePercentile99().seconds < 5)
    }
}
