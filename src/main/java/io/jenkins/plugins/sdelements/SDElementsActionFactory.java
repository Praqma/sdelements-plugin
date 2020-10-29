package io.jenkins.plugins.sdelements;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.Job;
import hudson.model.Run;
import jenkins.model.TransientActionFactory;

import javax.annotation.Nonnull;
import java.lang.System;
import java.lang.Thread;
import java.util.concurrent.TimeUnit;
import java.util.Collection;
import java.util.Collections;



/**
 * Creates our action for showing and displaying current status.
 */
@Extension
public class SDElementsActionFactory extends TransientActionFactory<Job> {
    // seconds to wait for a complete build
    private static final int TIMEOUT = 10;
    // milliseconds to wait between build checks
    private static final int INTERVAL = 1000;

    @Override
    public Class<Job> type() {
        return Job.class;
    }

    private void waitForBuild(@Nonnull Job job, int timeout) throws InterruptedException {
        long timeStarted = System.currentTimeMillis();
        long timeoutMs = TimeUnit.SECONDS.toMillis(timeout);

        while (job.getLastCompletedBuild() == null && System.currentTimeMillis() - timeStarted < timeoutMs) {
            Thread.sleep(INTERVAL);
        }
    }

    @Nonnull
    @Override
    public Collection<? extends Action> createFor(@Nonnull Job job) {
        try {
            waitForBuild(job, TIMEOUT);
        } catch (InterruptedException e) {
            // sleep was interrupted, continue
        }
        Run<?,?> r = job.getLastCompletedBuild();
        SDElementsRiskIndicatorBuildAction sdba = r.getAction(SDElementsRiskIndicatorBuildAction.class);
        SDElementsRiskIndicatorProjectAction sdpa = new SDElementsRiskIndicatorProjectAction(sdba.getRiskIndicator(), sdba.getProjectUrl(), sdba.getBaseUrl());
        return Collections.singleton(sdpa);
    }
}
