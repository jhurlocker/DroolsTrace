package com.redhat.git;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;










import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
//import org.dstadler.jgit.helper.CookbookHelper;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.AnyObjectId;
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

import com.redhat.brms.req.Rule;


public class RuleFileFromHead {

	private ArrayList<Rule> ruleFileList = new ArrayList<Rule>();
	HashMap<String, Object> ruleContentMap = new HashMap<String, Object>();
	private Rule rule; 
	int count = 0;
	
    public Rule getFile(String fileName, String projName) throws IOException, NoHeadException, GitAPIException {
 	    BRMSGitPathFileFromHead brmsGitPathFromHead = new BRMSGitPathFileFromHead();
	    String brmsGitPath = brmsGitPathFromHead.getBRMSGitPath("brmsMetaData.path");
    	Repository repository = new FileRepository(brmsGitPath + "/" + projName);
    	
//	    Repository repository = new FileRepository("C:/Users/jhurlock/brms-6.0.3/jboss-eap-6.1/bin/.niogit/uf-playground.git");
	    repository.getAllRefs();

        ObjectId lastCommitId = repository.resolve(Constants.HEAD);


        Git git = new Git(repository);

        RevWalk revWalk = new RevWalk(repository);
        

        RevCommit commit = revWalk.parseCommit(lastCommitId);
        

        RevTree tree = commit.getTree();

        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        treeWalk.setFilter(PathFilter.ALL);

        while (treeWalk.next()){        	
        	
        	if (fileName.trim().equalsIgnoreCase(treeWalk.getNameString().trim())){
        		rule = new Rule();

        		ruleContentMap.put("ruleName", treeWalk.getNameString());
        		rule.setRuleName(treeWalk.getNameString());
        		ruleContentMap.put("objectId", treeWalk.getObjectId(0).toString());
        		rule.setObjectId(treeWalk.getObjectId(0).toString());
	        ObjectId objectId = treeWalk.getObjectId(0);
	        ObjectLoader loader = repository.open(objectId);
        
        InputStream in = loader.openStream();

		int k=0;
    	StringBuffer ruleFileContent = new StringBuffer();
    	 while((k=in.read())!=-1)
    	  {
    		 ruleFileContent.append((char)k);	        		 
    		 
    	  }

	    	String formattedRuleFileContent = ruleFileContent.toString().replace("\n", "<br/>").replace("\t", "&nbsp;&nbsp;");
	        ruleContentMap.put("ruleText", ruleFileContent);

	        rule.setRuleContent(formattedRuleFileContent);

        this.ruleFileList.add(rule);
        count++;
        }
        }

        treeWalk.close();
        
        revWalk.close();
        revWalk.dispose();

        repository.close();

		return rule;
    }
}
