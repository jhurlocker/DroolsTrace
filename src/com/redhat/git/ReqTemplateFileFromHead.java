package com.redhat.git;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
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
 * Get the Requirement template by project name from the Trace Git repo
 */
public class ReqTemplateFileFromHead {

	private ArrayList<Requirement> requirementList = new ArrayList<Requirement>();
	HashMap<String, Object> ruleContentMap = new HashMap<String, Object>();
	private Requirement requirement;
	private ReqTemplate reqTemplate;
	int count = 0;


	public ReqTemplate getReqFile(String projectName) throws IOException,
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
		System.out.println("Having tree: " + tree);

		TreeWalk treeWalk = new TreeWalk(repository);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		treeWalk.setFilter(PathFilter.ALL);

		//GET THE REQ TEMPLATE FILE
		String reqTempFileName = "";
		while (treeWalk.next()) {

			if (treeWalk.isSubtree()) {
				System.out.println("FOLDER PATH= " + treeWalk.getPathString());
			}
			
			String theFileName = treeWalk.getNameString();
			System.out.println("************ FILE NAME= " + treeWalk.getPathString());

			if (theFileName.trim().equalsIgnoreCase(projectName.replace(".git", "") + ".reqTemp")) {

				ObjectId objectId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(objectId);
				ReadReqTempFile rrtf = new ReadReqTempFile();
				this.reqTemplate = rrtf.readFile(loader, projectName);
				
				count++;
			}
		}
		
		revWalk.dispose();

		repository.close();

		return this.reqTemplate;

	}
}
