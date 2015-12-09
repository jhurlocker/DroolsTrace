package com.redhat.brms.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
//import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.tika.metadata.serialization.JsonMetadataSerializer;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.redhat.brms.poi.ParseDoc;
import com.redhat.brms.req.Requirement;
import com.redhat.brms.req.Rule;
import com.redhat.brms.req.RuleTest;
import com.redhat.git.AddBRMSGitRepoPath;
import com.redhat.git.AddReq;
import com.redhat.git.AddReqTemplate;
import com.redhat.git.AddRuleTest;
import com.redhat.git.RemoveReq;
import com.redhat.git.ReqFileFromHead;
import com.redhat.git.RuleFileFromHead;
import com.redhat.git.RuleTestFileFromHead;
//import com.sun.jersey.core.header.FormDataContentDisposition;
//import com.sun.jersey.multipart.FormDataParam;

@Path("/message")
public class PostReq {


    @PUT
    @Path("basic/{param}/{param2}/{param3}/{param4}/{param5}")
   // @Consumes("text/plain")
    public void putBasic(@PathParam("param") String ruleName, 
    		@PathParam("param2") String objId, 
    		@PathParam("param3") String reqFileName,
    		@PathParam("param4") String type,
    		@PathParam("param5") String version) {
        System.out.println("%%% RULE NAME= " + ruleName);
        System.out.println("%%% OBJ ID= " + objId);
        System.out.println("%%% REQ FILE NAME= " + reqFileName);
        System.out.println("%%% REQ TYPE= " + type);
        System.out.println("%%% REQ VERSION= " + version);
        ReqFileFromHead reqFileFromHead = new ReqFileFromHead();
        try {
        	String fullFileName = reqFileName + ".breq";
			Requirement requirement = reqFileFromHead.getReqFile(fullFileName.trim());
			boolean ruleExist = false;
			if (requirement.getAssociatedRuleList() != null){
				for (Rule existingRule : requirement.getAssociatedRuleList()){
					if (existingRule.getRuleName().equalsIgnoreCase(ruleName)){
						ruleExist = true;
					}
				}
			}
			if (ruleExist == false){
				Rule rule = new Rule();
				rule.setRuleName(ruleName);
				rule.setObjectId(objId);
				requirement.getAssociatedRuleList().add(rule);
			}
		requirement.setObjectKey(objId);
		requirement.setObjectName(ruleName);
		requirement.setStatus("In Progress");
		AddReq addReq = new AddReq();
		addReq.addFile(requirement);
        } catch (NoHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

    }

    @PUT
    @Path("assocRuleTest/{ruleName}/{objectId}/{testScenarioName}/{gitRepo}")

    public void assocRuleTest(@PathParam("ruleName") String ruleName, 
    		@PathParam("objectId") String objectId, 
    		@PathParam("testScenarioName") String testScenarioName,
    		@PathParam("gitRepo") String gitRepo) {

        RuleTestFileFromHead ruleTestFileFromHead = new RuleTestFileFromHead();
        try {
        	String fullFileName = testScenarioName;

        	RuleTest ruleTest = ruleTestFileFromHead.getRuleTestFile(testScenarioName);
        	if (ruleTest != null){        	
				boolean ruleExist = false;
				if (ruleTest.getAssociatedRuleList() != null){
					for (Rule existingRule : ruleTest.getAssociatedRuleList()){
						if (existingRule.getRuleName().equalsIgnoreCase(ruleName)){
							ruleExist = true;
						}
					}
				}
				if (ruleExist == false){
					Rule rule = new Rule();
					rule.setRuleName(ruleName);
					rule.setObjectId(objectId);
					ruleTest.getAssociatedRuleList().add(rule);
				}
        	} else {
        		ruleTest = new RuleTest();
				Rule rule = new Rule();
				rule.setRuleName(ruleName);
				rule.setObjectId(objectId);
				ruleTest.getAssociatedRuleList().add(rule);
        	}
        	ruleTest.setTestName(testScenarioName);
        	ruleTest.setRepository(gitRepo);
        	
        	AddRuleTest addRuleTest = new AddRuleTest();
        	addRuleTest.addFile(ruleTest);

        } catch (NoHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

    }
    
    @PUT
    @Path("removeAssocTest/{ruleName}/{testFileName}")
   // @Consumes("text/plain")
    public void removeAssocTest(@PathParam("ruleName") String ruleName,  
    		@PathParam("testFileName") String testFileName) {

        RuleTestFileFromHead ruleTestFileFromHead = new RuleTestFileFromHead();
        try {

			RuleTest ruleTest = ruleTestFileFromHead.getRuleTestFile(testFileName);
			boolean ruleExist = false;

			Rule rule = null;
			for (int x = 0; x < ruleTest.getAssociatedRuleList().size(); x++){

				rule = ruleTest.getAssociatedRuleList().get(x);
				
				if (rule.getRuleName().equalsIgnoreCase(ruleName)){
					ruleTest.getAssociatedRuleList().remove(x);

				}
			}

        	AddRuleTest addRuleTest = new AddRuleTest();
        	addRuleTest.addFile(ruleTest);

        } catch (NoHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    @PUT
    @Path("removeAssocRule/{param}/{param2}/{param3}/{param4}/{param5}")
   // @Consumes("text/plain")
    public void removeAssocRule(@PathParam("param") String ruleName, 
    		@PathParam("param2") String objId, 
    		@PathParam("param3") String reqFileName,
    		@PathParam("param4") String type,
    		@PathParam("param5") String version) {
        System.out.println("%%% RULE NAME= " + ruleName);
        System.out.println("%%% OBJ ID= " + objId);
        ReqFileFromHead reqFileFromHead = new ReqFileFromHead();
        try {
        	String fullFileName = reqFileName + ".breq";
			Requirement requirement = reqFileFromHead.getReqFile(fullFileName.trim());
			boolean ruleExist = false;

			Rule rule = null;
			for (int x = 0; x < requirement.getAssociatedRuleList().size(); x++){

				rule = requirement.getAssociatedRuleList().get(x);
				
				//Remove rule if in associated rule list
				if (rule.getRuleName().equalsIgnoreCase(ruleName)){
					requirement.getAssociatedRuleList().remove(x);

				}
			}


		requirement.setObjectKey(objId);
		requirement.setObjectName(ruleName);
		
		//If there are no rules associated to the requirement then set the status back to Open
		if (requirement.getAssociatedRuleList().size() == 0){
			requirement.setStatus("Open");
		}
		AddReq addReq = new AddReq();
		addReq.addFile(requirement);
        } catch (NoHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    @PUT
    @Path("addReq/{param}/{param2}/{param3}/{param4}/{param5}/{param6}/{customSectionValArray}/{gitRepo}/{assocRules}")
   // @Consumes("text/plain")
    public void addReq(@PathParam("param") String reqNameVar, 
    		@PathParam("param2") String descVar, 
    		@PathParam("param3") String versionVar,
    		@PathParam("param4") String typeVar,
    		@PathParam("param5") String statusVar,
    		@PathParam("param6") String severityVar,
    		@PathParam("customSectionValArray") String customSectionValArray,
    		@PathParam("gitRepo") String gitRepo,
    		@PathParam("assocRules") JSONArray assocRules) {
System.out.println("customSectionValArray= " + customSectionValArray.toString());
System.out.println("assocRulesValArray= " + assocRules.toString());
		
		String[] customSectionValSplit = customSectionValArray.replaceAll("\"", "").split(",");

		ArrayList<Rule> assocRulesList = new ArrayList<Rule>();

		JSONArray assocRulesArray =assocRules;
		
        System.out.println(assocRulesArray.length());

        if (!(assocRules.toString().equalsIgnoreCase("[\"none\"]"))){
        for (int i =0; i < assocRulesArray.length(); i++){
            JSONObject json = null;
			try {
				json = (JSONObject)assocRulesArray.get(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (json != null){
				Rule rule = new Rule();
	            try {
					rule.setRuleName(json.getString("ruleName"));
					rule.setObjectId(json.getString("objectId"));
					assocRulesList.add(rule);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        }
        }
		System.out.println("customSectionValSplit= " + customSectionValSplit.length);
		HashMap sectionMap = new HashMap();
		if (customSectionValSplit.length > 1){
			for (int x = 0; x < customSectionValSplit.length; x++){
				String[] customSectionVals = customSectionValSplit[x].split(":");
				sectionMap.put(customSectionVals[0], customSectionVals[1]);			
			}
		}
        ReqFileFromHead reqFileFromHead = new ReqFileFromHead();
        String fullFileName = reqNameVar + ".breq";
		Requirement requirement = new Requirement();
		requirement.setReqName(reqNameVar);
		requirement.setReqDescription(descVar);
		requirement.setReqVersion(versionVar);
		requirement.setType(typeVar);
		requirement.setStatus(statusVar);
		requirement.setSeverity(severityVar);
		requirement.setSectinMap(sectionMap);
		requirement.setRepository(gitRepo);
		requirement.setAssociatedRuleList(assocRulesList);
		boolean ruleExist = false;

AddReq addReq = new AddReq();
addReq.addFile(requirement);
        
    }
    
    @PUT
    @Path("uploadReq/{param}")

    public void uploadReq(@PathParam("param") String fileLocationVar) {
    	System.out.println("FILE LOC= " + fileLocationVar);
    	//!!! FOR OPENSHIFT !!!
    	//String fileLocationFormatted = fileLocationVar.replace("^", "\\");
    	String fileLocationFormatted = fileLocationVar.replace("^", "/");
    	System.out.println("FORMATTED FILE LOC= " + fileLocationFormatted);
    	ParseDoc parseDoc = new ParseDoc();
    	//!!! FOR OPENSHIFT !!!
    	//Requirement req = parseDoc.generate(fileLocationFormatted);
    	String[] docName = fileLocationFormatted.split("/");
    	System.out.println("DOC NAME= " + docName[docName.length]);
    	Requirement req = parseDoc.generate(docName[docName.length]);
		AddReq addReq = new AddReq();
		addReq.addFile(req);
        
    }
    
    @PUT
    @Path("removeReq/{reqFileName}")

    public void removeReq(@PathParam("reqFileName") String reqFileName) {

        ReqFileFromHead reqFileFromHead = new ReqFileFromHead();
       
        	String fullFileName = reqFileName + ".breq";
        	RemoveReq removeReq = new RemoveReq();
        	try {
				removeReq.deleteFile(fullFileName);
			} catch (IOException | GitAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
    }
    
    @PUT
    @Path("genTemp/{reqHeaders}/{project}/{reqTempName}/{reqTempDesc}/{fileName}")

    
    public void genTemp(@PathParam("reqHeaders") String reqHeaders, 
    		@PathParam("project") String project,
    		@PathParam("reqTempName") String reqTempName, 
    		@PathParam("reqTempDesc") String reqTempDesc, 
    		@PathParam("fileName") String fileName) {

    	AddReqTemplate addReqTemplate = new AddReqTemplate();
    	addReqTemplate.addFile(reqHeaders, project, reqTempName, reqTempDesc, fileName);
    }
    
    @PUT
    @Path("addBRMSGitPath/{path}")
  //@Consumes("application/json")
    public void addBRMSGitPath(@PathParam("path") String path) {

    	AddBRMSGitRepoPath addBRMSGitRepoPath = new AddBRMSGitRepoPath();
    	String pathFormatted = path.replace("^", "/");
    	System.out.println("pathFormatted= " + pathFormatted);
    	
    	addBRMSGitRepoPath.addPath(pathFormatted);
    }
    
    @POST
    @Path("uploadFile")
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input) {
    	
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        
        // Get file data to save
        List<InputPart> inputParts = uploadForm.get("file");
 
        for (InputPart inputPart : inputParts) {
            try {
 
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String fileName = getFileName(header);
   System.out.println("THE FILENAME IS= " + fileName);
                // convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class,
                        null);
 
                byte[] bytes = IOUtils.toByteArray(inputStream);
                // constructs upload file path
                //fileName = "C:/Users/jhurlock/" + fileName;
                String filePath = writeFile(bytes, fileName);
 
                ParseDoc parseDoc = new ParseDoc();

            	Requirement req = parseDoc.generate(filePath);
        		AddReq addReq = new AddReq();
        		addReq.addFile(req);
        		//System.out.println("CONTEXT URIINFO = " + info);
        		//return Response.seeOther(URI.create("/BRMS-ReqTool-Rest/reqs.html")).build();
        		return Response.noContent().build();
//        		URI absoluteURI=info.getBaseUriBuilder().path("/BRMS-ReqTool-Rest/reqs.html").build();
//        		   return Response.created(absoluteURI).build();
        		   
//        		URI uriOfCreatedResource = URI.create("/BRMS-ReqTool-Rest/reqs.html");
//                return Response.ok().location(uriOfCreatedResource).build().;
                
//        		URI uri = new URI("/BRMS-ReqTool-Rest/reqs.html");
//        		Response.status(200).location(uri).build();
                //return Response.status(200).entity("Uploaded file name : " + fileName)
                 //       .build();
 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    private String getFileName(MultivaluedMap<String, String> header) {
    	 
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
 
        for (String filename : contentDisposition) {
        	System.out.println("FILENAME= " + filename);
            if ((filename.trim().startsWith("filename"))) {
        	//if ((filename.trim().equalsIgnoreCase("uf-playground-uf1trace.docx"))) {
                String[] name = filename.split("=");
 
                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
        		//return filename;
            }
        }
        return "unknown";
    }
 
    // Utility method
    private String writeFile(byte[] content, String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("not exist> " + file.getAbsolutePath());
            file.createNewFile();
        }
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(content);
        fop.flush();
        fop.close();
        return file.getAbsolutePath();
    }
}