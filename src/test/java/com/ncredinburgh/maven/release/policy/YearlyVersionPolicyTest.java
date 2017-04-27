package com.ncredinburgh.maven.release.policy;

import org.apache.maven.shared.release.policy.version.VersionPolicy;
import org.apache.maven.shared.release.policy.version.VersionPolicyRequest;
import org.apache.maven.shared.release.policy.version.VersionPolicyResult;
import org.junit.Test;

import java.time.Year;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class YearlyVersionPolicyTest {
    private VersionPolicy testee;
    private VersionPolicyResult result;

    private static VersionPolicyRequest version(String version) {
        return new VersionPolicyRequest().setVersion(version);
    }

    @Test
    public void shouldRemoveSnapshotWhenYearIsCurrent() throws Exception {
        testee = new YearlyVersionPolicy(Year.of(2017));

        result = testee.getReleaseVersion(version("2017.3-SNAPSHOT"));

        assertThat(result.getVersion(), is("2017.3"));
    }

    @Test
    public void shouldDefaultToFirstReleaseOfYearOnNonsenseInput() throws Exception {
        testee = new YearlyVersionPolicy(Year.of(2017));

        result = testee.getReleaseVersion(version("This doesn't look like a version"));

        assertThat(result.getVersion(), is("2017.1"));
    }

    @Test
    public void shouldPreserveExtraSubVersion() throws Exception {
        testee = new YearlyVersionPolicy(Year.of(2017));

        result = testee.getReleaseVersion(version("2017.3.2.1-SNAPSHOT"));

        assertThat(result.getVersion(), is("2017.3.2.1"));
    }

    @Test
    public void shouldIgnoreSnapshotVersionIfOfPreviousYear() throws Exception {
        testee = new YearlyVersionPolicy(Year.of(2017));

        result = testee.getReleaseVersion(version("2016.3-SNAPSHOT"));

        assertThat(result.getVersion(), is("2017.1"));
    }

    @Test
    public void shouldIgnoreSnapshotVersionIfNotInYearForm() throws Exception {
        testee = new YearlyVersionPolicy(Year.of(2017));

        result = testee.getReleaseVersion(version("1.2.3-SNAPSHOT"));

        assertThat(result.getVersion(), is("2017.1"));
    }

    @Test
    public void shouldReturnSameVersionIfNoSnapShotPresent() throws Exception {
        testee = new YearlyVersionPolicy(Year.of(2017));

        result = testee.getReleaseVersion(version("2017.3"));

        assertThat(result.getVersion(), is("2017.3"));
    }

    @Test
    public void shouldIncrementMinorVersionAndAppendSnapshotPostfix() throws Exception {
        testee = new YearlyVersionPolicy(Year.of(2017));

        result = testee.getDevelopmentVersion(version("2017.3"));

        assertThat(result.getVersion(), is("2017.4-SNAPSHOT"));
    }

    @Test
    public void shouldUseFirstVersionOfYearWhenVersionNotInYearlyFormat() throws Exception {
        testee = new YearlyVersionPolicy(Year.of(2017));

        result = testee.getDevelopmentVersion(version("1.2.3"));

        assertThat(result.getVersion(), is("2017.1-SNAPSHOT"));
    }


    @Test
    public void shouldDefaultToFirstReleaseOfYearOnNonsenseVersion() throws Exception {
        testee = new YearlyVersionPolicy(Year.of(2017));

        result = testee.getDevelopmentVersion(version("This doesn't look like a version"));

        assertThat(result.getVersion(), is("2017.1-SNAPSHOT"));
    }


    @Test
    public void shouldGoToNextMinorVersionWhenExtraSubVersionsPresent() throws Exception {
        testee = new YearlyVersionPolicy(Year.of(2017));

        result = testee.getDevelopmentVersion(version("2017.3.2.1"));

        assertThat(result.getVersion(), is("2017.3.2.2-SNAPSHOT"));
    }
}
