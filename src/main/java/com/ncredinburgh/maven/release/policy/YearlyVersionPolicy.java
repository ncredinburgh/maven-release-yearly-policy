package com.ncredinburgh.maven.release.policy;

import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.shared.release.policy.PolicyException;
import org.apache.maven.shared.release.policy.version.VersionPolicy;
import org.apache.maven.shared.release.policy.version.VersionPolicyRequest;
import org.apache.maven.shared.release.policy.version.VersionPolicyResult;
import org.apache.maven.shared.release.versions.VersionParseException;
import org.codehaus.plexus.component.annotations.Component;

@Component(role = VersionPolicy.class,
           hint = "yearly",
           description = "A VersionPolicy implementation that uses IntelliJ-style yearly versioning")
public class YearlyVersionPolicy implements VersionPolicy {

    private static final String snapshotPostfix = "-SNAPSHOT";

    private final Year currentYear;

    public YearlyVersionPolicy(final Year currentYear) {
        this.currentYear = currentYear;
    }

    public YearlyVersionPolicy() {
        this(Year.now());
    }


    /**
     * {@inheritDoc}
     */
    public VersionPolicyResult getReleaseVersion(final VersionPolicyRequest versionPolicyRequest)
            throws PolicyException, VersionParseException {
        final String version = versionPolicyRequest.getVersion();
        final String cleanedVersion = StringUtils.removeEnd(version, snapshotPostfix);
        final String majorVersionComponent = StringUtils.substringBefore(cleanedVersion, ".");

        final String releaseVersion = matchesYear(majorVersionComponent, currentYear) ? cleanedVersion : firstOfYear();

        return new VersionPolicyResult()
                .setVersion(releaseVersion);
    }

    private boolean matchesYear(String yearString, Year year) {
        try {
            final Year parsedYear = Year.parse(yearString);
            return year.equals(parsedYear);
        } catch (DateTimeParseException dateTimeParseException) {
            return false;
        }
    }

    private String firstOfYear() {
        return currentYear.toString() + ".1";
    }

    /**
     * {@inheritDoc}
     */
    public VersionPolicyResult getDevelopmentVersion(final VersionPolicyRequest versionPolicyRequest)
            throws PolicyException, VersionParseException {
        final VersionPolicyResult result = new VersionPolicyResult();
        final String version = versionPolicyRequest.getVersion();

        if (version.endsWith(snapshotPostfix)) {
            result.setVersion(version);
        } else {
            final String majorVersionComponent = StringUtils.substringBefore(version, ".");

            if (matchesYear(majorVersionComponent, currentYear)) {
                result.setVersion(incrementVersionWithinYear(version) + snapshotPostfix);
            } else {
                result.setVersion(firstOfYear() + snapshotPostfix);
            }
        }

        return result;
    }

    private String incrementVersionWithinYear(final String version) {
        final Pattern subVersionsPattern = Pattern.compile("^\\d+(.\\d+)*");
        final String[] versionParts = version.split("\\.", 2);
        final String yearPart = versionParts[0];
        final String afterYearPart = versionParts[1];
        final Matcher subVersionMatcher = subVersionsPattern.matcher(afterYearPart);

        if (subVersionMatcher.find()) {
            final String nonNumberPart = afterYearPart.substring(subVersionMatcher.end());
            return yearPart + "." + incrementSubVersion(subVersionMatcher.group()) + nonNumberPart;
        } else {
            return firstOfYear();
        }
    }

    private String incrementSubVersion(final String subVersion) {
        if (StringUtils.isNumeric(subVersion)) {
            return Integer.toString(Integer.parseInt(subVersion) + 1);
        }

        final String headVersionPart = StringUtils.substringBeforeLast(subVersion, ".");
        final String tailVersionPart = StringUtils.substringAfterLast(subVersion, ".");
        final Integer newSubVersion = Integer.parseInt(tailVersionPart) + 1;

        return headVersionPart + "." + newSubVersion.toString();
    }
}
