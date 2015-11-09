package com.redhat.git;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.jgit.lib.ObjectLoader;

import com.redhat.brms.req.ReqTemplate;

/**
 * 
 * @author jhurlocker
 * 
 * Returns a requirement template from the Trace Git repo
 */
public class ReadReqTempFile {

	public ReqTemplate readFile(ObjectLoader loader, String projName) throws IOException {

		InputStream in = loader.openStream();
		Properties prop = new Properties();

		// load a properties file
		prop.load(in);


		if (prop.getProperty("$GitProj").equalsIgnoreCase(projName) ||
			projName.equalsIgnoreCase("$all")){
			ReqTemplate reqTemplate = new ReqTemplate();
			reqTemplate.setGitProj(prop.getProperty("$GitProj"));
			reqTemplate.setId(prop.getProperty("$ID"));

			reqTemplate.setDocHeadings(prop.getProperty("$DocHeadings"));
			in.close();

		return reqTemplate;
} else
	in.close();
	return null;

	}
}
