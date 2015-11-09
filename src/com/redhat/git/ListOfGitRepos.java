package com.redhat.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;

/**
 * 
 * @author jhurlocker
 *
 * Get a list of the BRMS Git Project Repos.  BRMS can contain multiple Git repositories.  Every
 * new project created has its own Git repo.
 */
public class ListOfGitRepos {

	public ArrayList<String> getProjectRepos(String gitRepo)  {

		ArrayList<String> gitRepoList = new ArrayList<String>();
		
		File brmsGitDir = new File(gitRepo);
		File[] directoryListing = brmsGitDir.listFiles();
		if (directoryListing != null) {
			for (File projects : directoryListing) {
				String projDirName = projects.getName();
				//Only add in projects with a .git extension.  Don't add in Drools specific Git repositories.
				if (projDirName.contains(".git")
						&& !(projDirName.equalsIgnoreCase("system.git") || 
								projDirName.equalsIgnoreCase("repository1.git"))) {
					gitRepoList.add(projDirName);
				}
			}
		}
		return gitRepoList;
	}
}
