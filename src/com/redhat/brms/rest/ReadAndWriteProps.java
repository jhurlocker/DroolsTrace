package com.redhat.brms.rest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.redhat.brms.req.CaseInsensitiveProps;

public class ReadAndWriteProps {
	
	public String readBRMSRepoPath(){
	InputStream in = null;
	in = ReadAndWriteProps.class.getResourceAsStream("path.props");
	//in = new FileInputStream("path.props");
	//Setup a CaseInsensitiveProps file so we can get property keys without have to worry about case
	CaseInsensitiveProps prop = new CaseInsensitiveProps();

	// load a properties file
	try {
		prop.load(in);
		System.out.println("BRMSGITREPO READ= " + prop.getProperty("$brmsGitRepo"));
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	return prop.getProperty("$brmsGitRepo");
	}
	
	public void write(String brmsGitRepo){
	FileOutputStream out = null;
	try {
		//out = ReadAndWriteProps.class.;
		out = new FileOutputStream(ReadAndWriteProps.class.getResource("path.props").getFile());
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//Setup a CaseInsensitiveProps file so we can get property keys without have to worry about case
	CaseInsensitiveProps prop = new CaseInsensitiveProps();

	// load a properties file
	try {
		prop.setProperty("$brmsGitRepo", brmsGitRepo);
		prop.store(out, null);
		//System.out.println("BRMSGITREPO READ= " + prop.getProperty("$brmsGitRepo"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	}
}
