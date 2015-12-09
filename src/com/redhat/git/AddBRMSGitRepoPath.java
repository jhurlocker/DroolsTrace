package com.redhat.git;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.jboss.as.cli.CliInitializationException;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandContextFactory;
import org.jboss.as.cli.CommandLineException;

import com.redhat.brms.rest.ReadAndWriteProps;

/**
 * 
 * @author jhurlocker
 * 
 * Adds the path to the BRMS Git repository path.  A brmsMetaData.path file is created that contains
 * a BRMS Git repo path key value pair and a Trace Git repo path.  The Trace Git repo path is created
 * by default under the <JBOSS_HOME>/bin/rm directory.
 *
 */
public class AddBRMSGitRepoPath {

	public void addPath(String brmsRepoPath) {
	
		//Default Drools Trace Git repo path
		//brmsRepoPath = "/var/lib/openshift/564ccd0b2d527177f3000234/brms/jboss/bin/.niogit";
		if(brmsRepoPath.endsWith("/"))
		{
			brmsRepoPath = brmsRepoPath.substring(0,brmsRepoPath.length() - 1);
		}
		File traceGitPath = new File(brmsRepoPath + "/../rm");
		ReadAndWriteProps readAndWriteProps = new ReadAndWriteProps();
		
		readAndWriteProps.write(brmsRepoPath);
		
		//Create a Git repo for Drools Trace if it doesn't exist
		if (!traceGitPath.exists()){
	        try {
				Git.init().setDirectory(traceGitPath).call();
			} catch (IllegalStateException | GitAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
    
	
    Repository repository = null;
	try {
		//this.setPropValByCLI(traceGitPath.getCanonicalPath());
		repository = new FileRepository(traceGitPath.getCanonicalPath() + "/.git");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    Git git = new Git(repository);

    String fileName = "brmsMetaData.path";

    File brmsMetaDataFile = new File(git.getRepository().getDirectory().getParent(), fileName);

    try {
		brmsMetaDataFile.createNewFile();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    FileWriter fw = null;
	try {
		fw = new FileWriter(brmsMetaDataFile.getAbsoluteFile());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	BufferedWriter bw = new BufferedWriter(fw);
	try {
		bw.write("$brmsGitPath=" + brmsRepoPath + "\n");
		bw.write("$traceGitPath=" + traceGitPath.getCanonicalPath());
		bw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//Add the brmsMetaData.path to the Trace Git repo
    try {
		git.add()
		        .addFilepattern(fileName)
		        .call();
	    git.commit()
	    .setMessage("Added brmsMetaData.path")
	    .call();
	} catch (GitAPIException e) {
		// TODO Auto-generated catch block
		System.out.println("Not adding brmsMetaData.path");
		e.printStackTrace();
	}

 
    repository.close();
	}
	
//	public String setPropValByCLI(String traceGitRepoPath){
//        // Initialize the CLI context
//        final CommandContext ctx;
//        try {
//            ctx = CommandContextFactory.getInstance().newCommandContext();
//        } catch(CliInitializationException e) {
//            throw new IllegalStateException("Failed to initialize CLI context", e);
//        }
//
//        try {
//            // connect to the server controller
//            ctx.connectController("localhost", 9999);
//
//            // execute commands and operations
//            ctx.handle("/system-property=brmsGitPath/:remove");
//            ctx.handle("/system-property=brmsGitPath/:add(value=" + traceGitRepoPath + ")");
//        } catch (CommandLineException e) {
//            // the operation or the command has failed
//        } finally {
//            // terminate the session and
//            // close the connection to the controller
//            ctx.terminateSession();
//        }
//        return System.getProperty("brmsGitPath");
//	}
}
