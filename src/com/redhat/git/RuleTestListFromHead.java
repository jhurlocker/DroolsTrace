package com.redhat.git;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.redhat.brms.req.RuleTest;

public class RuleTestListFromHead {

	private ArrayList<RuleTest> ruleTestList = new ArrayList<RuleTest>();
	HashMap<String, Object> ruleContentMap = new HashMap<String, Object>();
	private RuleTest ruleTest;
	private ReqTemplate reqTemplate;
	int count = 0;

	public ArrayList<RuleTest> getRuleTestList(String projName) throws IOException,
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

			//Only concerned with test files in trace repo
			if (!(theFileName.trim().startsWith("."))
					&& theFileName.trim().endsWith(".btest")) {
				this.ruleTest = new RuleTest();

				ObjectId objectId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(objectId);
				ReadRuleTestFile rrtf = new ReadRuleTestFile();
				this.ruleTest = rrtf.readFile(loader, projName);
				if ((ruleTest.getRepository() != null &&
						ruleTest.getRepository().equalsIgnoreCase(projName)) ||
						projName.equalsIgnoreCase("$all")){
					ruleTestList.add(ruleTest);
				}
				count++;
			}
		}

		revWalk.dispose();

		repository.close();

		return this.ruleTestList;

	}
}
