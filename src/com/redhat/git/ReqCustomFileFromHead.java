package com.redhat.git;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
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

/**
 * 
 * @author jhurlocker
 * 
 * Return a Requirement based on the custom template
 */
public class ReqCustomFileFromHead {

	private ArrayList<Requirement> requirementList = new ArrayList<Requirement>();
	HashMap<String, Object> ruleContentMap = new HashMap<String, Object>();
	private Requirement requirement;
	private ReqTemplate reqTemplate;
	int count = 0;

	public Requirement getReqFile(String reqFileName)  {

 		Repository repository = null;
		TraceGitRepoPath traceGitRepoPath = new TraceGitRepoPath();
		try {
			//Create a new FileRepostiory to the Trace Git repo
			repository = new FileRepository(traceGitRepoPath.returnPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Git git = new Git(repository);
		
		ObjectId lastCommitId;
		RevCommit commit = null;
		RevWalk revWalk = null;
		try {
			lastCommitId = repository.resolve(Constants.HEAD);
			revWalk = new RevWalk(repository);

			commit = revWalk.parseCommit(lastCommitId);
		} catch (RevisionSyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RevTree tree = commit.getTree();

		TreeWalk treeWalk = new TreeWalk(repository);
		try {
			treeWalk.addTree(tree);

		treeWalk.setRecursive(true);
		treeWalk.setFilter(PathFilter.ALL);

		//GET THE REQ TEMPLATE FILE
		String reqTempFileName = "";
		while (treeWalk.next()) {

			String theFileName = treeWalk.getNameString();
			
			//Only interested in requirment template files here
			if (!(theFileName.trim().startsWith("."))
					&& theFileName.trim().endsWith(".reqTemp")) {

				ObjectId objectId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(objectId);
				ReadReqTempFile rrtf = new ReadReqTempFile();
				
				this.reqTemplate = rrtf.readFile(loader, "$all");

				count++;
			}
		}
		
		while (treeWalk.next()) {

			String theFileName = treeWalk.getNameString();

			if (reqFileName.equalsIgnoreCase(theFileName.trim().toString())) {
				this.requirement = new Requirement();
				
				ObjectId objectId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(objectId);
				ReadReqFile rrf = new ReadReqFile();
				
				this.requirement = rrf.readFile(loader, "$all", this.reqTemplate);
				count++;
				return this.requirement;
				
			}
		}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		revWalk.dispose();

		repository.close();

		return null;

	}
}
