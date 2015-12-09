package com.redhat.brms.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
//import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;

import com.redhat.brms.poi.GenerateDoc;
import com.redhat.brms.req.CaseInsensitiveProps;
import com.redhat.brms.req.ReqTemplate;
import com.redhat.brms.req.ReqTestCvrg;
import com.redhat.brms.req.ReqTestCvrgList;
import com.redhat.brms.req.Requirement;
import com.redhat.brms.req.RequirementList;
import com.redhat.brms.req.Rule;
import com.redhat.brms.req.RuleList;
import com.redhat.brms.req.RuleTest;
import com.redhat.brms.req.RuleTestList;
import com.redhat.git.BRMSGitPathFileFromHead;
import com.redhat.git.ListOfGitRepos;
import com.redhat.git.ReqCustomFileFromHead;
import com.redhat.git.ReqListFromHead;
import com.redhat.git.ReqFileFromHead;
import com.redhat.git.ReqTemplateFileFromHead;
import com.redhat.git.RuleListFromHead;
import com.redhat.git.RuleScenarioListFromHead;
import com.redhat.git.RuleTestListFromHead;
import com.redhat.git.TraceGitRepoPath;


@Path("/message")
public class RestReq {
	
	@GET
	@Path("rules/head")
	@Produces("application/json")
	public RuleList getHeadRules() {
		RuleListFromHead ruleFilesFromHead = new RuleListFromHead();
	
		
		RuleList ruleList = null;
		try {
			
				ruleList = new RuleList(ruleFilesFromHead.getFiles("$all"));
			
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

		return ruleList;
	}
	
	@GET
	@Path("rulesCoverage")
	@Produces("application/json")
	public ReqTestCvrgList getReqsCoverage() {

		ArrayList<String> gitRepos = this.getListOfGitRepos();
		ArrayList<ReqTestCvrg> reqTestCvrgArrayList = new ArrayList<ReqTestCvrg>();
		for (int x = 0; x < gitRepos.size(); x++){
			RequirementList reqList = this.getReqsByProj(gitRepos.get(x));
			
			double ruleCount = 0;
			double testCount = 0;
			for (Requirement req : reqList.getRequirementList()){
				if (req.getAssociatedRuleList().size() > 0){
					ruleCount++;
				};
			}
			double reqCvrg = 0;
			double numReqs = reqList.getRequirementList().size();

			if (ruleCount > 0){
				reqCvrg = (ruleCount/numReqs);
			}
			
			RuleList ruleList = this.getRulesByProject(gitRepos.get(x));
			RuleTestList ruleTestList = this.getRuleTestsByProject(gitRepos.get(x));

			ArrayList ruleTestsCoveredList = new ArrayList();
			for (Rule rule : ruleList.getDocumentationList()){
				for (RuleTest ruleTest : ruleTestList.getRuleTestList()){					
					for (Rule ruleAssocToTest : ruleTest.getAssociatedRuleList()){

						if (ruleAssocToTest.getRuleName().equalsIgnoreCase(rule.getRuleName())){
							//Make sure we don't count the same test twice
							if (!(ruleTestsCoveredList.contains(rule.getRuleName()))){
								ruleTestsCoveredList.add(rule.getRuleName());
								
								testCount++;
							}
						}
				}
				}
			}
			double testCvrg = 0;
			double numRules = ruleList.getDocumentationList().size();

			if (testCount > 0){
				testCvrg = (testCount/numRules);
			}
			ReqTestCvrg reqTestCvrg = new ReqTestCvrg();
			reqTestCvrg.setProjName(gitRepos.get(x));
			reqTestCvrg.setReqCvrg(reqCvrg);
			reqTestCvrg.setTestCvrg(testCvrg);
			reqTestCvrgArrayList.add(reqTestCvrg);
		}
		ReqTestCvrgList reqTestCvrgList = new ReqTestCvrgList(reqTestCvrgArrayList);
		return reqTestCvrgList;
	}
	
	@GET
	@Path("rulesByProject/{projName}")
	@Produces("application/json")
	public RuleList getRulesByProject(@PathParam("projName") String projName) {
		RuleListFromHead ruleFilesFromHead = new RuleListFromHead();
		
		RuleList ruleList = null;
		try {
			
				ruleList = new RuleList(ruleFilesFromHead.getFiles(projName));
			
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

		return ruleList;
	}
	
	@GET
	@Path("testsByProject/{projName}")
	@Produces("application/json")
	public RuleTestList getRuleTestsByProject(@PathParam("projName") String projName) {
		RuleTestListFromHead ruleTestFilesFromHead = new RuleTestListFromHead();
		
		RuleTestList ruleTestList = null;
		try {
			
				ruleTestList = new RuleTestList(ruleTestFilesFromHead.getRuleTestList(projName));
			
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

		return ruleTestList;
	}

	@GET
	@Path("testScenariosByProject/{projName}")
	@Produces("application/json")
	public RuleTestList getRuleTestScenariosByProject(@PathParam("projName") String projName) {
		RuleScenarioListFromHead ruleScenarioListFromHead = new RuleScenarioListFromHead();
		
		RuleTestList ruleTestList = null;
		try {
			
				ruleTestList = new RuleTestList(ruleScenarioListFromHead.getFiles(projName));
			
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

		return ruleTestList;
	}
	
	@GET
	@Path("requirements/all")
	@Produces("application/json")
	public RequirementList getReqCats() {
		ReqListFromHead reqCatsFromHead = new ReqListFromHead();
			
		RequirementList requirementList = null;
		try {			
			requirementList = new RequirementList(reqCatsFromHead.getReqs("$all"));			
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

		return requirementList;
	}
	
	@GET
	@Path("requirements/{byProject}")
	@Produces("application/json")
	public RequirementList getReqByProj(@PathParam("byProject") String projName) {
		ReqListFromHead reqCatsFromHead = new ReqListFromHead();
			
		RequirementList requirementList = null;
		try {			
			requirementList = new RequirementList(reqCatsFromHead.getReqs(projName));			
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

		return requirementList;
	}
	
	@GET
	@Path("requirements/reqTemp/{projectName}")
	@Produces("application/json")
	public ReqTemplate getReqTemp(@PathParam("projectName") String projectName) {
		ReqTemplateFileFromHead reqTemplateFileFromHead = new ReqTemplateFileFromHead();
			
		RequirementList requirementList = null;
		try {		
			ReqTemplate reqTemp = reqTemplateFileFromHead.getReqFile(projectName);
			//System.out.println("^^^^^^^^^^^^ REQTEMP= " + reqTemp.getDocHeadings().length);
			return reqTemp;
			//requirementList = new RequirementList(reqCatsFromHead.getReqs("$all"));			
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

		return null;
	}
	
	@GET
	@Path("requirementsByProject/{projectName}")
	@Produces("application/json")
	public RequirementList getReqsByProj(@PathParam("projectName") String projectName) {

		ReqListFromHead reqCatsFromHead = new ReqListFromHead();
			
		RequirementList requirementList = null;
		try {			
			requirementList = new RequirementList(reqCatsFromHead.getReqs(projectName));			
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

		return requirementList;
	}
	
	@GET
	@Path("brmsGitPath/{path}")
	//@Produces("application/json")
	public String getBRMSGitPath(@PathParam("path") String path) {
		BRMSGitPathFileFromHead brmsGitPathFileFromHead = new BRMSGitPathFileFromHead();
						
			String brmsRepoPath = brmsGitPathFileFromHead.getBRMSGitPath(path);
			String[] splitBrmsRepoPath = brmsRepoPath.split("=");
			for (int i = 0; i < splitBrmsRepoPath.length; i++){
				String theKey = splitBrmsRepoPath[i].toString();
				if (theKey.equalsIgnoreCase("$brmsGitPath")){
					brmsRepoPath = splitBrmsRepoPath[i + 1];
				}
			}

			return "\"" + brmsRepoPath.replace("\\", "^").replace("$traceGitPath", "").trim() + "\"";
						
			
	}
	
	@GET
	@Path("listOfGitRepos")
	//@Produces("application/json")
	public ArrayList<String> getListOfGitRepos() {
		
		BRMSGitPathFileFromHead brmsGitPathFileFromHead = new BRMSGitPathFileFromHead();
		ListOfGitRepos listOfGitRepos = new ListOfGitRepos();
		ArrayList<String> gitRepoList = new ArrayList<String>();
						
			String brmsRepoPath = brmsGitPathFileFromHead.getBRMSGitPath("brmsMetaData.path");

			String[] splitBrmsRepoPath = brmsRepoPath.split("=");
			for (int i = 0; i < splitBrmsRepoPath.length; i++){
				String theKey = splitBrmsRepoPath[i].toString();
				if (theKey.equalsIgnoreCase("$brmsGitPath")){
					brmsRepoPath = splitBrmsRepoPath[i + 1];
				}
			}
			gitRepoList = listOfGitRepos.getProjectRepos(brmsRepoPath.replace("$traceGitPath", "").trim());
//			for (int x = 0; x < gitRepoList.size(); x++){
//				System.out.println("************ " + x + " " + gitRepoList.get(x));
//			}

			return gitRepoList;
						
			
	}
	
	@GET
	@Path("requirements/desc/{param}")
	@Produces("application/json")
	public RequirementList getReqDesc(@PathParam("param") String fileName) {
		System.out.println("IN getReqDesc " + fileName);
		ReqFileFromHead reqFileFromHead = new ReqFileFromHead();

		RequirementList requirementList = null;
		ArrayList reqList = new ArrayList();

		try {
			reqList.add(reqFileFromHead.getReqFile(fileName));
		} catch (NoHeadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (GitAPIException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		requirementList = new RequirementList(reqList);

		return requirementList;

	}
	
	@GET
	@Path("requirements/genDoc/{projName}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getGenDoc(@PathParam("projName") String projName) {
		System.out.println("ABOUT TO GENERATE DOCUMENTATION");
		ReqListFromHead allReqs = new ReqListFromHead();
		ArrayList<Requirement> allReqsList = null;
		try {
			allReqsList = allReqs.getReqs(projName);
		} catch (IOException | GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ABOUT TO GENERATE DOCUMENTATION");
		GenerateDoc.generate(allReqsList, "", projName);
		
		TraceGitRepoPath traceGitRepoPath = new TraceGitRepoPath();
		  File file = new File(traceGitRepoPath.returnPath().replace("/.git", "") + "/" + "DroolsTraceDocumentation - " + projName.replace(".git", ".doc"));
		  return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
		      .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
		      .build();
	}
	
	@GET
	@Path("requirements/docReqTempFile/{projName}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getDocReqTempFile(@PathParam("projName") String projName) {
		System.out.println("ABOUT TO GENERATE TEMPLATE DOCUMENTATION");
		TraceGitRepoPath traceGitRepoPath = new TraceGitRepoPath();
	  File file = new File(traceGitRepoPath.returnPath().replace("/.git", "") + "/" + projName.replace(".git", ".docx"));
	  return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
	      .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
	      .build();
	}
}