package comparison;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.AssertionFailedError;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.util.ResultsUtils;
import specreader.CheckWrapper;
import specreader.TestSpecRepository;
import specreader.spec.TestSpecSettings;

public abstract class AbstractTest {

    private final String name;

    protected AbstractTest(final String name) {
        this.name = name;
    }

    @SuppressWarnings("checkstyle:IllegalCatch")
    @ParameterizedTest
    @MethodSource("provideArguments")
    void dataTest(final CheckWrapper checkWrapper) {
        final var testId = checkWrapper.check().id();
        Allure.getLifecycle().updateTestCase(testResult -> testResult.setName(name + " on " + testId));

        Allure.epic(checkWrapper.specFile().spec().name());
        Allure.story(format(checkWrapper.check().input()));
        Allure.descriptionHtml(buildDescription(checkWrapper));
        Allure.parameter("id", testId);
        Allure.parameter("specFile", checkWrapper.specFile().file());
        Allure.parameter("skipEmptyLines", checkWrapper.specFile().spec().settings().skipEmptyLines());
        Allure.parameter("commentMode", checkWrapper.specFile().spec().settings().commentMode());

        if (!isSettingsSupported(checkWrapper.specFile().spec().settings())) {
            Assumptions.abort("Feature not supported");
        }

        var failed = 0;

        for (final var variant : TestSpecRepository.wrapVariant(checkWrapper.check()).toList()) {
            final String stepId = UUID.randomUUID().toString();
            Allure.getLifecycle().startStep(stepId, new StepResult().setName("step"));
            Allure.getLifecycle().updateStep(step -> step
                .setName("Variant: `" + format(variant.variant().data() + "`")));

            try {
                final List<List<String>> expected = variant.records();
                final List<List<String>> actual =
                    parseCsvRecords(checkWrapper.specFile().spec().settings(), variant.variant().data());

                assertEquals(format(expected.toString()), format(actual.toString()));

                Allure.getLifecycle().updateStep(stepId, step -> step.setStatus(Status.PASSED));
            } catch (final AssertionFailedError e) {
                Allure.getLifecycle().updateStep(stepId, step -> step
                    .setStatus(Status.FAILED)
                    .setStatusDetails(ResultsUtils.getStatusDetails(e).orElse(null)));
                failed++;
            } catch (final Throwable e) {
                if (checkWrapper.specFile().spec().settings().exceptionAllowed()) {
                    Allure.getLifecycle().updateStep(stepId, step -> step
                        .setStatus(Status.PASSED)
                        .setStatusDetails(new StatusDetails()
                            .setMessage("Exception allowed: " + e.getMessage())));
                } else {
                    Allure.getLifecycle().updateStep(stepId, step -> step
                        .setStatus(Status.FAILED)
                        .setStatusDetails(ResultsUtils.getStatusDetails(e).orElse(null)));
                    failed++;
                }
            } finally {
                Allure.getLifecycle().stopStep(stepId);
            }
        }

        if (failed > 0) {
            // Allure uses this assertion message for categorization
            fail("Unexpected results for " + testId);
        }
    }

    private static String buildDescription(final CheckWrapper checkWrapper) {
        final var description = new StringBuilder();

        if (checkWrapper.check().description() != null) {
            description.append("<p>").append(checkWrapper.check().description()).append("</p>");
        }

        description
            .append("<p><strong>Spec</strong></p>")
            .append("<p>").append(checkWrapper.specFile().spec().description()).append("</p>");

        return description.toString();
    }

    static Stream<Arguments> provideArguments() {
        final Path testsPath = Path.of(System.getProperty("tests.path"));
        return TestSpecRepository.loadChecks(testsPath)
            .map(Arguments::arguments);
    }

    /**
     * Returns the input string with special characters replaced by their escape sequences
     * to visualize the string in a more readable way.
     *
     * @param data the input string
     * @return the input string with special characters replaced by their escape sequences.
     */
    private static String format(final String data) {
        return data
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
            .replace("\f", "\\f")
            .replace(" ", "_");
    }

    protected boolean isSettingsSupported(final TestSpecSettings settings) {
        return true;
    }

    protected abstract List<List<String>> parseCsvRecords(TestSpecSettings settings, String input)
        throws Exception;

}
