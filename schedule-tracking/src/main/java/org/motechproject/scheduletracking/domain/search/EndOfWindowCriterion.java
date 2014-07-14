package org.motechproject.scheduletracking.domain.search;

import org.joda.time.DateTime;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.WindowName;
import org.motechproject.scheduletracking.repository.AllEnrollments;

import java.util.ArrayList;
import java.util.List;

public class EndOfWindowCriterion implements Criterion {
    private WindowName windowName;
    private DateTime start;
    private DateTime end;

    public EndOfWindowCriterion(WindowName windowName, DateTime start, DateTime end) {
        this.windowName = windowName;
        this.start = start;
        this.end = end;
    }

    @Override
    public List<Enrollment> fetch(AllEnrollments allEnrollments) {
        return filter(allEnrollments.retrieveAll());
    }

    @Override
    public List<Enrollment> filter(List<Enrollment> enrollments) {
        List<Enrollment> filteredEnrollments = new ArrayList<Enrollment>();
        for (Enrollment enrollment : enrollments) {
            DateTime endOfWindowForCurrentMilestone = enrollment.getEndOfWindowForCurrentMilestone(windowName);
            if (DateUtil.inRange(endOfWindowForCurrentMilestone, start, end)) {
                filteredEnrollments.add(enrollment);
            }
        }
        return filteredEnrollments;
    }
}
