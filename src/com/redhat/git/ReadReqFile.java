package com.redhat.git;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.jgit.lib.ObjectLoader;

import com.redhat.brms.req.CaseInsensitiveProps;
import com.redhat.brms.req.ReqTemplate;
import com.redhat.brms.req.Requirement;
import com.redhat.brms.req.Rule;

/**
 * 
 * @author jhurlocker
 * 
 * Read a requirement file based on a requirement template 
 */
public class ReadReqFile {

	public Requirement readFile(ObjectLoader loader, String projName, ReqTemplate reqTemplate) throws IOException {

		InputStream in = loader.openStream();
		//Setup a CaseInsensitiveProps file so we can get property keys without have to worry about case
		CaseInsensitiveProps prop = new CaseInsensitiveProps();

		// load a properties file
		prop.load(in);
		
		//If there is an associated requirement to the project

if (!(prop.isEmpty())){
		Requirement req = new Requirement();

		if (prop.getProperty("$GitRepo").trim().equalsIgnoreCase(projName)
				|| projName.equalsIgnoreCase("$all")){
		//Get the requirement name
		req.setReqName(prop.getProperty("$Name"));
		//Get the associated BRMS Git repo
		req.setRepository(prop.getProperty("$GitRepo"));
		//Set the requirement description
		req.setReqDescription(prop.getProperty("$Desc"));
		//Set the requirement version
		req.setReqVersion(prop.getProperty("$Version"));
		//Set the requirement type
		req.setType(prop.getProperty("$Type"));
		//Check if a template exists
		if (reqTemplate != null){
		String[] docHeadingsArray = reqTemplate.getDocHeadings();
		//Set the custom requirement headers
			for (int x = 0; x < docHeadingsArray.length; x++){				
				//Replace all spaces in the key with ^ because property keys cannot contain spaces
				String theValue = prop.getPropVal(docHeadingsArray[x].toString().replaceAll("\\s+", "^"));
				req.getSectionMap().put(docHeadingsArray[x].toString(), theValue);
			}
		}
		
		//Add all associated rules if they exist
		String assocRules = prop.getProperty("$AssocRules");
		if (assocRules != null){
		String[] assocRulesArray = assocRules.split(",");
		//int arrayLength = reqFileArray.length - 6;
		ArrayList ruleList = new ArrayList();

		if (assocRulesArray.length > 1){
			for (int x = 0; x < assocRulesArray.length; x++) {
				Rule rule = new Rule();
				rule.setRuleName((String) assocRulesArray[x]);
	
				rule.setObjectId((String) assocRulesArray[x + 1]);
				ruleList.add(rule);
				x++;
			}
		}
			req.setAssociatedRuleList(ruleList);
		}

		}
		in.close();
		return req;
} else
	return null;
	}
}
