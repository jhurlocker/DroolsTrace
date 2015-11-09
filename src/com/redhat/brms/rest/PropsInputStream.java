package com.redhat.brms.rest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsInputStream {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			InputStream in = new FileInputStream("C:\\Users\\jhurlock\\brms-6.0.3\\jboss-eap-6.1\\bin\\rm\\requirementProjTemplate.reqTemp");
			Properties prop = new Properties();
			prop.load(in);
			System.out.println("GIT PROJ= " + prop.getProperty("$DocHeadings"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
