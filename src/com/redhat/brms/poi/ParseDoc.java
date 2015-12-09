package com.redhat.brms.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;

import com.redhat.brms.req.Requirement;


public class ParseDoc {
	
public Requirement generate(String filename) {
	//!!! FOR OPENSHIFT !!!
	File file = new File(filename);
	System.out.println("FILENAME= " + filename);
	//File file = new File("/var/lib/openshift/564ccd0b2d527177f3000234/brms/jboss/bin/rm/" + filename);
	
	InputStream fis = null;
	try {
		fis = new FileInputStream(file);
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} 
	
	XWPFDocument xdoc = null;
		try {
			xdoc = new XWPFDocument(OPCPackage.open(fis));
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
		    String fileData=extractor.getText();

		    
		List<IBodyElement> bodyElements = xdoc.getBodyElements();
		for (IBodyElement be : bodyElements){
			be.getBody().toString();
		}
	    XWPFStyles styles = xdoc.getStyles();

	    XWPFRun r1 = xdoc.getParagraphs().get(0).createRun();

		Requirement req = new Requirement();
		for (int x = 0; x < xdoc.getParagraphs().size(); x++){
			String ptext = xdoc.getParagraphs().get(x).getText().trim();
			String style = xdoc.getParagraphs().get(x).getStyle();
			if (style != null && style.trim().equalsIgnoreCase("Heading1")){
			 switch (ptext) {
	            case "REQUIREMENT NAME":  req.setReqName(xdoc.getParagraphs().get(x + 1).getText().trim());
	                break;
	            case "GIT REPO":  req.setRepository(xdoc.getParagraphs().get(x + 1).getText().trim());
	            	break;
	            case "TYPE":  req.setType(xdoc.getParagraphs().get(x + 1).getText().trim());
                	break;
	            case "VERSION":  req.setReqVersion(xdoc.getParagraphs().get(x + 1).getText().trim());
            		break;
	            case "STATUS":  req.setStatus(xdoc.getParagraphs().get(x + 1).getText().trim());
        			break;
	            case "SEVERITY":  req.setSeverity(xdoc.getParagraphs().get(x + 1).getText().trim());
        			break;
	            case "DESCRIPTION":  req.setReqDescription(xdoc.getParagraphs().get(x + 1).getText().trim());
        			break;
        		default : if ((x + 1) <= xdoc.getParagraphs().size()){req.getSectionMap().put(xdoc.getParagraphs().get(x).getText().trim(), 
        				xdoc.getParagraphs().get(x + 1).getText().trim());}
			 }
			}
		}

    return req;
}
}
