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
	
    public Rule getFile(String fileName) throws IOException, NoHeadException, GitAPIException {
       // Repository repository = CookbookHelper.openJGitCookbookRepository();
	    Repository repository = new FileRepository("C:/Users/jhurlock/brms-6.0.3/jboss-eap-6.1/bin/.niogit/uf-playground.git");
	    repository.getAllRefs();
	    for (Map.Entry<String, Ref> entry : repository.getAllRefs().entrySet()){
	    	System.out.println("%%%%%%%%%%%%%%%%% " + entry.getKey() + "/" + entry.getValue());
	    }
        // find the HEAD
        ObjectId lastCommitId = repository.resolve(Constants.HEAD);

        // a RevWalk allows to walk over commits based on some filtering that is defined
        Git git = new Git(repository);
        //Iterable<RevCommit> commits = git.log().all().call();
        RevWalk revWalk = new RevWalk(repository);
        
       // for (RevCommit commit : commits) {
        RevCommit commit = revWalk.parseCommit(lastCommitId);
        
        // and using commit's tree find the path
        RevTree tree = commit.getTree();
        System.out.println("Having tree: " + tree);

        // now try to find a specific file
        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        treeWalk.setFilter(PathFilter.ALL);
//        if (!treeWalk.next()) {
//            throw new IllegalStateException("Did not find expected file 'README.md'");
//        }
        while (treeWalk.next()){
        	
        	//String theFileName = treeWalk.getNameString();
        	
        	if (fileName.trim().equalsIgnoreCase(treeWalk.getNameString().trim())){
        		rule = new Rule();
        		System.out.println("************ FILE NAME= " + treeWalk.getNameString());
        		ruleContentMap.put("ruleName", treeWalk.getNameString());
        		rule.setRuleName(treeWalk.getNameString());
        		System.out.println("************ OBJECT ID= " + treeWalk.getObjectId(0).toString());
        		ruleContentMap.put("objectId", treeWalk.getObjectId(0).toString());
        		rule.setObjectId(treeWalk.getObjectId(0).toString());
	        ObjectId objectId = treeWalk.getObjectId(0);
	        ObjectLoader loader = repository.open(objectId);
        
        // and then one can the loader to read the file
	    
        //loader.copyTo(System.out);
        InputStream in = loader.openStream();
		// or
		//loader.copyTo(out)
		int k=0;
    	StringBuffer ruleFileContent = new StringBuffer();
    	 while((k=in.read())!=-1)
    	  {
    		//System.out.print("@@@@@@@@@@ " + (char)k);
    		 ruleFileContent.append((char)k);	        		 
    		 
    	  }

	    	String formattedRuleFileContent = ruleFileContent.toString().replace("\n", "<br/>").replace("\t", "&nbsp;&nbsp;");
	        ruleContentMap.put("ruleText", ruleFileContent);
	        System.out.println("RULE FORMATTED= " + formattedRuleFileContent);
	        rule.setRuleContent(formattedRuleFileContent);

        this.ruleFileList.add(rule);
        count++;
        }
        }
        //}
//        ObjectId objectId = ObjectId.fromString("afda245cc08e7fd8ecb669fa1fd239bcf5574f33");
//        ObjectLoader loader = repository.open(objectId);
//    
//    // and then one can the loader to read the file
//        System.out.println("=================================FINAL TESTDRL");
//    loader.copyTo(System.out);
        treeWalk.close();
        
        revWalk.close();
        revWalk.dispose();

        repository.close();
        System.out.println("^^^^^RULEFILELIST SIZE= " + ruleFileList.size());
        System.out.println("^^^^^COUNT= " + count);
		return rule;
    }
}
