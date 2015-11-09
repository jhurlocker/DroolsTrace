package com.redhat.brms.rest;

import java.io.File;
import java.io.IOException;

public class SplitArray {
	static short ashort;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String pathString = "vfs:/C:/Users/jhurlock/brms-6.0.3/jboss-eap-6.1/bin/content/BRMS-ReqTool-Rest.war/WEB-INF/classes/com/redhat/git/AddBRMSGitRepoPath.class";
		String[] pathSplit = pathString.replace("vfs:/", "").split("/bin/");
		System.out.println("PATH SPLIT SIZE= " + pathSplit.length);
		System.out.println("ONE= " + pathSplit[0] + "/bin/rm/");
		System.out.println("ONE= " + pathSplit[1]);
		String arrayString = "[{\"label\":\"head 1\",\"$$hashKey\":\"object:3\"},{\"label\":\"head 2\",\"$$hashKey\":\"object:5\"},{\"label\":\"Header three\",\"$$hashKey\":\"object:7\"},{\"label\":\"JH header 4\",\"$$hashKey\":\"object:9\"}]";
		System.out.println("ARRAY STRING= " + arrayString);
		String[] splitArray = arrayString.split(",");
		
		File aDir = new File("C:\\Users\\jhurlock\\testDir");
		if (!aDir.exists()){
			aDir.mkdir();
			File traceDir = new File(aDir.toString() + "\\..\\rm");
			traceDir.mkdir();
		}
		double one = 12;
		double two = 25;
		double result;
		result = (one/two);
		System.out.println("DIV RESULT= " + result);
		System.out.println("A SHORT= " + getAshort().toString());
		System.out.println("ARRAYSPLIT 0= " + splitArray[0]);
		System.out.println("ARRAYSPLIT 1= " + splitArray[1]);
		for (int x = 0; x < splitArray.length; x++){
			if (splitArray[x].toString().contains("label")){
				String[] headerArray = splitArray[x].toString().split(":");
				String theHeader = headerArray[1].replace("\"", "");
				System.out.println("THE HEADER= " + theHeader);
			}
		}
		
		String arrayString2 = "Heading 1";
		System.out.println("ARRAY STRING= " + arrayString);
		System.out.println("AS2= " + arrayString2.replaceAll("\\s+", "^"));
		
		String[] splitArray2 = arrayString2.split(",");

		
		System.out.println("ARRAYSPLIT 0= " + splitArray2[0]);
		System.out.println("ARRAYSPLIT 1= " + splitArray2[1]);
		for (int x = 0; x < splitArray2.length; x++){
			if (splitArray2[x].toString().contains("label")){
				String[] headerArray = splitArray2[x].toString().split(":");
				String theHeader = headerArray[1].replace("\"", "");
				System.out.println("THE HEADER2= " + theHeader);
			}
		}
		
		short[] transCodeArray = new short[]{150, 290, 291, 294};
		short transCd = 0;
		for (int x = 0; x < transCodeArray.length; x++){
			if (transCd == transCodeArray[x]){
				System.out.println("TRANS CD VALUE= " + transCodeArray[x]);
			}
		}
	}
	public static Short getAshort() {
		return ashort;
	}
	public void setAshort(Short ashort) {
		this.ashort = ashort;
	}

}
