package org.jenkinsci.plugins.scm_filter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.plugins.git.GitSCM;
import hudson.scm.SCMDescriptor;
import jenkins.scm.api.SCMHead;
import jenkins.scm.api.trait.SCMHeadFilter;
import jenkins.scm.api.trait.SCMSourceContext;
import jenkins.scm.api.trait.SCMSourceRequest;
import jenkins.scm.api.trait.SCMSourceTrait;
import jenkins.scm.api.trait.SCMSourceTraitDescriptor;

/**
 * @author witokondoria
 */
public abstract class CommitSkipTrait extends SCMSourceTrait {

    private final String tokens;

    /**
     * Constructor for stapler.
     */
    protected CommitSkipTrait(String tokens) {
        this.tokens = tokens;
    }

    public String getTokens() {
        return tokens;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected abstract void decorateContext(SCMSourceContext<?, ?> context);

    /**
     * Our descriptor.
     */
    public abstract static class CommitSkipTraitDescriptorImpl extends SCMSourceTraitDescriptor {

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDisplayName() {
            return Messages.CommitSkipTrait_DisplayName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isApplicableToSCM(@NonNull SCMDescriptor<?> scm) {
            return scm instanceof GitSCM.DescriptorImpl;
        }
    }

    /**
     * Filter that excludes pull requests according to its last commit message (if
     * it contains [ci skip] or [skip ci], case unsensitive).
     */
    public abstract static class ExcludePRsSCMHeadFilter extends SCMHeadFilter {

        private final Set<String> tokenList;

        public ExcludePRsSCMHeadFilter(String tokens) {
            Set<String> list = new HashSet<String>();
            for (String token : tokens.split(",")) {
                token = token.trim().toLowerCase();
                if (!token.isEmpty()) {
                    list.add(token);
                }
            }
            tokenList = Collections.unmodifiableSet(list);
        }

        @Override
        abstract public boolean isExcluded(@NonNull SCMSourceRequest scmSourceRequest, @NonNull SCMHead scmHead)
                throws IOException, InterruptedException;

        public boolean containsSkipToken(String commitMsg) {
            for (String token : tokenList) {
                if (commitMsg.contains(token)) {
                    return true;
                }
            }
            return false;
        }
    }
}
