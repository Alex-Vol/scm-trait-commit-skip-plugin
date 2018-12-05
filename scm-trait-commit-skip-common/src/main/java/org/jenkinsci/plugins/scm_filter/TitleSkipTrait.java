package org.jenkinsci.plugins.scm_filter;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.plugins.git.GitSCM;
import hudson.scm.SCMDescriptor;
import jenkins.scm.api.trait.SCMSourceTraitDescriptor;

/**
 * @author avolanis
 */
public abstract class TitleSkipTrait extends CommitSkipTrait {

    protected TitleSkipTrait(String tokens) {
        super(tokens);
    }

    /**
     * Our descriptor.
     */
    public abstract static class TitleSkipTraitDescriptorImpl extends SCMSourceTraitDescriptor {

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDisplayName() {
            return Messages.TitleSkipTrait_DisplayName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isApplicableToSCM(@NonNull SCMDescriptor<?> scm) {
            return scm instanceof GitSCM.DescriptorImpl;
        }
    }
}
