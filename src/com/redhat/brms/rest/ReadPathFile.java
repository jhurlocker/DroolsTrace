	package com.redhat.brms.rest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import com.redhat.git.BRMSGitPathFileFromHead;

public class ReadPathFile {

	public static void main(String[] args) throws URISyntaxException {
		// TODO Auto-generated method stub
		URL url1 = BRMSGitPathFileFromHead.class.getResource("BRMSGitPathFileFromHead.class");
		System.out.println("URL STRING= " + url1.toURI().toString());
		String[] tracePathArray = url1.toURI().toString().split("/bin/");
		String tracePath = tracePathArray[0].replace("vfs:/", "").concat("/bin/rm/.git");
		InputStream pathPropsFile = 
				   ReadPathFile.class.getResourceAsStream("path.props");
		Properties pathProps = new Properties();
		try {
			pathProps.load(pathPropsFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("BRMSGITREPO= " + pathProps.getProperty("$brmsGitRepo"));
		try {
			URL url = ReadPathFile.class.getResource("ReadPathFile.class");
			System.out.println("URL= " + url.toString().replace("file:/", ""));
			OutputStream output = new FileOutputStream(url.toString().replace("file:/", ""));
			pathProps.setProperty("$brmsGitRepo", "TheBRMSRepoPath");
			pathProps.store(output, null);
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
