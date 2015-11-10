package com.redhat.brms.poi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import com.redhat.brms.req.Requirement;
import com.redhat.brms.req.Rule;
import com.redhat.git.BRMSGitPathFileFromHead;
import com.redhat.git.RuleFileFromHead;
import com.redhat.git.TraceGitRepoPath;

public class GenerateDoc {
	
	//public static void main(String[] args) throws IOException {
	public static void generate(ArrayList<Requirement> allReqsList, String outputPath, String projName) {
	
    XWPFDocument doc = new XWPFDocument();
    
    XWPFParagraph p1 = doc.createParagraph();
    
    p1.setAlignment(ParagraphAlignment.CENTER);

    p1.setVerticalAlignment(TextAlignment.TOP);

    XWPFRun r1 = p1.createRun();
    
    r1.setBold(true);
    
    
    r1.setFontFamily("Arial");
    r1.setFontSize(18);
    r1.setText(projName.toUpperCase() + " Documentation");
    r1.addCarriageReturn();
    r1.addCarriageReturn();
    System.out.println("REQS SIZE= " + allReqsList.size());
    
    XWPFParagraph p2 = doc.createParagraph();
    p2.setSpacingAfterLines(0);
    p2.setSpacingBeforeLines(0);

    p2.setVerticalAlignment(TextAlignment.TOP);
    XWPFParagraph p3 = doc.createParagraph();
    
    	XWPFRun r2 = p2.createRun();
    	XWPFRun r3 = p2.createRun();
    	XWPFRun r4 = p2.createRun();
    	XWPFRun r5 = p2.createRun();
    	
    	for (Requirement req : allReqsList){
	        r2.setBold(false);
	        //r2.setFontSize(14);
	        r2.setText("REQUIREMENT NAME: " + req.getReqName());	
	        r2.addCarriageReturn();
	        r2.setText("DESCRIPTION: " + req.getReqDescription());	
	        r2.addCarriageReturn();
	        r2.setText("STATUS: " + req.getStatus());	
	        r2.addCarriageReturn();
	        r2.setText("VERSION: " + req.getReqVersion());
	        r2.addCarriageReturn();
	        Map<String, String> sectionMap = req.getSectionMap();
	        for (Map.Entry<String, String> entry : sectionMap.entrySet()) {
	        	r2.setText(entry.getKey().toUpperCase() + ": " + entry.getValue());
	        	r2.addCarriageReturn();
	        }
//	        r2.addCarriageReturn();
	        r2.addCarriageReturn();
	        r2.setFontFamily("Arial");
    		for (Rule rule : req.getAssociatedRuleList()){
    	        //r3.setBold(true);
    	        //r3.setFontSize(14);
    			RuleFileFromHead ruleFileFromHead = new RuleFileFromHead();

					try {
						rule = ruleFileFromHead.getFile(rule.getRuleName(), projName);
					} catch (IOException | GitAPIException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 			
    	        r2.setText("  RULE: " + rule.getRuleName());	
    	        r2.setFontFamily("Arial");
    	        r2.addCarriageReturn();
    	        r2.addCarriageReturn();	
    	        r2.setText("  RULE CONTENT");
    	        r2.addCarriageReturn();
    	        String formattedRuleFileContent = rule.getRuleContent().toString().replace("&nbsp;&nbsp;", "    ");
	        	String[] theRuleDRL = formattedRuleFileContent.split("<br/>");
	        	
	        	
	        	//r2.setFontFamily("Courier New");
	        	for (int q =0; q < theRuleDRL.length; q++){
	        		r2.setText("     " + theRuleDRL[q]);
	        		r2.addCarriageReturn();
	        	}
	        	r2.addCarriageReturn();
//		        r2.setText("***************************************************************************************");

    		}
    	
    	
	        r2.addCarriageReturn();
	        
	        
	        //r2.setBold(true);
	        r2.setText("----------------------------------------------------------------------------------------------------------------------------");
	        r2.addCarriageReturn();
    	
    	}
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    String dateString = sdf.format(date);
    try {
    	TraceGitRepoPath traceGitRepoPath = new TraceGitRepoPath();

//    	FileOutputStream outWord = new FileOutputStream(traceGitRepoPath.returnPath().replace("/.git", "") + "/JBossBRMSRuleDocumentation - " + dateString.replaceAll(":", "_").toString() + ".doc");
    	FileOutputStream outWord = new FileOutputStream(traceGitRepoPath.returnPath().replace("/.git", "") + "/DroolsTraceDocumentation - " + projName.replace(".git", "") + ".doc");

        doc.write(outWord);
		outWord.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
    
}

}
