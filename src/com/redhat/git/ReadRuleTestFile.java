package com.redhat.git;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.eclipse.jgit.lib.ObjectLoader;

import com.redhat.brms.req.Rule;
import com.redhat.brms.req.RuleTest;

/**
 * 
 * @author jhurlocker
 * 
 * Returns a rule test scenario file form the Trace Git repo
 *
 */
public class ReadRuleTestFile {

	public RuleTest readFile(ObjectLoader loader, String projName) throws IOException {

		InputStream in = loader.openStream();
		Properties prop = new Properties();

		// load a properties file
		prop.load(in);
		//If there is an associated requirement to the project

		if (!(prop.isEmpty())){
		RuleTest ruleTest = new RuleTest();

		if (prop.getProperty("$GitRepo").trim().equalsIgnoreCase(projName)
				|| projName.equalsIgnoreCase("$all")){
		//Get the rule test file name
		ruleTest.setTestName(prop.getProperty("$Name"));
		//Get the associated BRMS Git repo
		ruleTest.setRepository(prop.getProperty("$GitRepo"));

		//GET ASSOCIATED RULE TESTS
		String assocRuleTests = prop.getProperty("$AssocRuleTests");
		if (assocRuleTests != null){
		String[] assocRulesTestArray = assocRuleTests.split(",");

		ArrayList ruleList = new ArrayList();

		if (assocRulesTestArray.length > 1){
				for (int x = 0; x < assocRulesTestArray.length; x++) {
		
					Rule rule = new Rule();
					rule.setRuleName((String) assocRulesTestArray[x]);
		
					rule.setObjectId((String) assocRulesTestArray[x + 1]);
					
					ruleList.add(rule);
					x++;
				}
		}
		ruleTest.setAssociatedRuleList(ruleList);
		}
		}
in.close();
		return ruleTest;
} else
	return null;
	}
}
