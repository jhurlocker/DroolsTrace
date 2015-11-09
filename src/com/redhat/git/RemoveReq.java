package com.redhat.git;

import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

/**
 * 
 * @author jhurlocker
 * 
 * Remove a requirment file from the Trace Git repo
 */
public class RemoveReq {

	public void deleteFile(String fileName) throws IOException, NoFilepatternException, GitAPIException{
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

   
    git.rm()
            .addFilepattern(fileName)
            .call();
    git.commit()
    .setMessage("Removed req - " + fileName)
    .call();
    

    repository.close();
	}
}
