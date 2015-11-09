package com.redhat.git;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import com.redhat.brms.req.ReqTemplate;
import com.redhat.brms.req.Requirement;
import com.redhat.brms.req.RuleTest;


public class RuleTestFileFromHead {

	private ArrayList<Requirement> requirementList = new ArrayList<Requirement>();

	private RuleTest ruleTest;
	private ReqTemplate reqTemplate;
	int count = 0;


	public RuleTest getRuleTestFile(String ruleTestFileName) throws IOException,
			NoHeadException, GitAPIException {
	    Repository repository = null;
	    Git git = null;
		try {
			//Create a new FileRepostiory to the Trace Git repo
			TraceGitRepoPath traceGitRepoPath = new TraceGitRepoPath();
			repository = new FileRepository(traceGitRepoPath.returnPath());
			git = new Git(repository);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ObjectId lastCommitId = repository.resolve(Constants.HEAD);

		RevWalk revWalk = new RevWalk(repository);

		RevCommit commit = revWalk.parseCommit(lastCommitId);

		RevTree tree = commit.getTree();

		TreeWalk treeWalk = new TreeWalk(repository);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		treeWalk.setFilter(PathFilter.ALL);

		while (treeWalk.next()) {

			String theFileName = treeWalk.getNameString();

			if (ruleTestFileName.replace(".scenario", ".btest").equalsIgnoreCase(theFileName.trim().toString())) {
				this.ruleTest = new RuleTest();

				ObjectId objectId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(objectId);
				ReadRuleTestFile rrtf = new ReadRuleTestFile();
				
				this.ruleTest = rrtf.readFile(loader, "$all");
				count++;
				return this.ruleTest;
				
			}
		}

		revWalk.dispose();

		repository.close();

		return null;

	}
}
