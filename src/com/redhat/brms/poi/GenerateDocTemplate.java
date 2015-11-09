package com.redhat.brms.poi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import com.redhat.brms.req.Requirement;
import com.redhat.brms.req.Rule;
import com.redhat.brms.rest.ReadPathFile;
import com.redhat.git.RuleFileFromHead;
import com.redhat.git.TraceGitRepoPath;

public class GenerateDocTemplate {
	
	//public static void main(String[] args) throws IOException {
	public static void generate(String[] splitReqHeaders, String outputPath, String fileName, String project) {
		   Repository repository = null;
		   TraceGitRepoPath traceGitRepoPath = null;
		try {
		   	traceGitRepoPath = new TraceGitRepoPath();
		    
			repository = new FileRepository(traceGitRepoPath.returnPath());
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		    Git git = new Git(repository);
		
    XWPFDocument doc = new XWPFDocument();
    
    XWPFDocument document = null;
    
    try {
    	InputStream fis = GenerateDocTemplate.class.getResourceAsStream("template.docx");
		document = new XWPFDocument(fis);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    XWPFParagraph p1 = document.createParagraph();
    
    p1.setAlignment(ParagraphAlignment.CENTER);

    p1.setVerticalAlignment(TextAlignment.TOP);

    XWPFRun r1 = p1.createRun();
    
    r1.setBold(true);
    
    
    r1.setFontFamily("Arial");
    r1.setFontSize(18);
    r1.setText(project.toUpperCase().replace(".git", "") + " Requirement Document");
    r1.addCarriageReturn();
    r1.addCarriageReturn();
    
    //SET REQUIREMENT NAME
	XWPFParagraph pName = document.createParagraph();
	XWPFParagraph pNameText = document.createParagraph();
	
	XWPFRun rName = pName.createRun();
	XWPFRun rNameText = pNameText.createRun(); 
	
	pName.setStyle("Heading1");
    rName.setBold(false);
    rName.setText("REQUIREMENT NAME");	
    rName.addCarriageReturn();
   // p2.setStyle("Normal");
    rNameText.setText("<Add Text Here>");
    rNameText.addCarriageReturn();
    
    //SET DESCRIPTION
	XWPFParagraph pDesc = document.createParagraph();
	XWPFParagraph pDescText = document.createParagraph();
	
	XWPFRun rDesc = pDesc.createRun();
	XWPFRun rDescText = pDescText.createRun(); 
	
	pDesc.setStyle("Heading1");
	rDesc.setBold(false);
	rDesc.setText("DESCRIPTION");	
	rDesc.addCarriageReturn();
   // p2.setStyle("Normal");
	rDescText.setText("<Add Text Here>");
	rDescText.addCarriageReturn();
    
    //SET GIT REPO NAME
	XWPFParagraph pGitName = document.createParagraph();
	XWPFParagraph pGitNameText = document.createParagraph();
	
	XWPFRun rGitName = pGitName.createRun();
	XWPFRun rGitNameText = pGitNameText.createRun(); 
	
	pGitName.setStyle("Heading1");
    rGitName.setBold(false);
    rGitName.setText("GIT REPO");	
    rGitName.addCarriageReturn();
   // p2.setStyle("Normal");
    rGitNameText.setText(project);
    rGitNameText.addCarriageReturn();
    
   	
    	for (int x = 0; x < splitReqHeaders.length; x++){
    		if (splitReqHeaders[x].toString().contains("label")){
    			String[] headerArray = splitReqHeaders[x].toString().split(":");
    			String theHeader = headerArray[1].replace("\"", "");
    			
    			XWPFParagraph p2 = document.createParagraph();
    			XWPFParagraph p3 = document.createParagraph();
    		    //p2.setStyle("Heading1");
    		   // p2.setSpacingAfterLines(0);
    		   // p2.setSpacingBeforeLines(0);
    		    
    		    //p2.setStyle("Heading1");   
    		    
    		    p2.setVerticalAlignment(TextAlignment.TOP);
    		    
    		    XWPFRun r2 = p2.createRun();
    		    XWPFRun r3 = p3.createRun();   	    
    	        
    	        //r2.setFontSize(14);
    	        p2.setStyle("Heading1");
    	        r2.setBold(false);
    	        r2.setText(theHeader.toUpperCase());	
    	        r2.addCarriageReturn();
    	       // p2.setStyle("Normal");
    	        r3.setText("<Add Text Here>");
    	        r3.addCarriageReturn();

    		}
    	}
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    String dateString = sdf.format(date);
    try {
     //   FileOutputStream outWord = new FileOutputStream(genDocLocation + "/JBossBRMSRuleDocumentation - " + dateString.replaceAll(":", "_").toString() + ".doc");
        FileOutputStream outWord = new FileOutputStream(traceGitRepoPath.returnPath().replace("/.git", "") + "/" + fileName.replace(".reqTemp",  ".docx"));

        document.write(outWord);
		outWord.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
    
    try {
		git.add()
		.addFilepattern(traceGitRepoPath.returnPath().replace("/.git", "") + fileName.replace(".reqTemp",  ".docx"))
		.call();
	} catch (GitAPIException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
try {
	git.commit()
	.setMessage("Added requirement template docx")
	.call();
} catch (GitAPIException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
//System.out.println("Added file " + myfile + " to repository at " + repository.getDirectory());

repository.close();
}

}
