package com.redhat.git;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
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
import org.jboss.as.cli.CliInitializationException;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandContextFactory;
import org.jboss.as.cli.CommandLineException;

import com.redhat.brms.rest.ReadAndWriteProps;

/**
 * 
 * @author jhurlocker
 *
 *	Get the BRMS Git Path from the Trace Git repo Head branch
 */
public class BRMSGitPathFileFromHead {

	public String getBRMSGitPath(String brmsGitPathFileName) {
		//System.getProperties().setProperty("brmsGitPath", "C:/Users/jhurlock");
		//System.out.println("BRMSGITPATH PROP= " + System.getProperty("brmsGitPath"));
	    Repository repository = null;
	    Git git = null;
		try {
			//Create a new FileRepostiory to the Trace Git repo
			//TraceGitRepoPath traceGitRepoPath = new TraceGitRepoPath();
//			URL url = BRMSGitPathFileFromHead.class.getResource("BRMSGitPathFileFromHead.class");
//			String path = BRMSGitPathFileFromHead.class.getClass().getClassLoader().getParent()
//			String traceRepoPath = brmsGitPath.replace("\"", "").concat("/../rm/.git");
			
			URL url1 = BRMSGitPathFileFromHead.class.getResource("BRMSGitPathFileFromHead.class");
			System.out.println("URL STRING= " + url1.toURI().toString());
		
			String[] tracePathArray = url1.toURI().toString().split("/bin/");
			String traceGitRepoPath = tracePathArray[0].replace("vfs:/", "").concat("/bin/rm/.git");
			System.out.println("TRACEGITREPO= " + traceGitRepoPath);
			ReadAndWriteProps readAndWriteProps = new ReadAndWriteProps();
			traceGitRepoPath = readAndWriteProps.readBRMSRepoPath();
			repository = new FileRepository(traceGitRepoPath + "/../rm/.git");
			//!!!FOR OPENSHIFT!!!
			//repository = new FileRepository("/var/lib/openshift/564ccd0b2d527177f3000234/brms/jboss/bin/rm/.git");
			git = new Git(repository);
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObjectId lastCommitId;
		RevTree tree = null;
		RevWalk revWalk = null;
		try {
			lastCommitId = repository.resolve(Constants.HEAD);
			revWalk = new RevWalk(repository);

			//Get the latest branch by commit id
			RevCommit commit = revWalk.parseCommit(lastCommitId);
			tree = commit.getTree();
		} catch (RevisionSyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TreeWalk treeWalk = new TreeWalk(repository);
		try {
			treeWalk.addTree(tree);
			treeWalk.setRecursive(true);
			treeWalk.setFilter(PathFilter.ALL);

			//Go through tree and find the BRMS Git path file
			while (treeWalk.next()) {

				String theFileName = treeWalk.getNameString();

				if (brmsGitPathFileName.equalsIgnoreCase(theFileName.trim().toString())) {

					//Get the object id of the file
			        ObjectId objectId = treeWalk.getObjectId(0);
			        //Open the file in an ObjectLoader
			        ObjectLoader loader = repository.open(objectId);
		        
			        InputStream in = loader.openStream();

			        //Append the file contents to a StringBuffer
				int k=0;
		    	StringBuffer brmsGitPath = new StringBuffer();
		    	 while((k=in.read())!=-1)
		    	  {
		    		 brmsGitPath.append((char)k);	        		 
		    		 
		    	  }
		    	 String[] pathArray = brmsGitPath.toString().split("\n");
		    	 //return brmsGitPath.toString();
		    	 String[] brmsRepo = pathArray[0].split("=");

		    	 return brmsRepo[1].toString();
					
				}
			}
		} catch (MissingObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CorruptObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		revWalk.dispose();

		repository.close();

		return "";

	}
	
}
