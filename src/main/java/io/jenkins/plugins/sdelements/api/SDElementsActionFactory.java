package io.jenkins.plugins.sdelements.api;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.Job;
import hudson.model.Run;
import io.jenkins.plugins.sdelements.SDElementsRiskIndicatorBuildAction;
import io.jenkins.plugins.sdelements.SDElementsRiskIndicatorProjectAction;
import jenkins.model.TransientActionFactory;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;

/**
 * Creates our action for showing and displaying current status.
 */
@Extension
public class SDElementsActionFactory extends TransientActionFactory<Job> {
    @Override
    public Class<Job> type() {
        return Job.class;
    }

    @Nonnull
    @Override
    public Collection<? extends Action> createFor(@Nonnull Job job) {
        Run<?,?> r = job.getLastCompletedBuild();
        SDElementsRiskIndicatorBuildAction sdba = r.getAction(SDElementsRiskIndicatorBuildAction.class);
        SDElementsRiskIndicatorProjectAction sdpa = new SDElementsRiskIndicatorProjectAction(sdba.getRiskIndicator());
        return Collections.singleton(sdpa);
    }
}
