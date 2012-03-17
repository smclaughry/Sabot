package org.sabot.client.util;

public final class PhoneFormatter {
	
	private PhoneFormatter(){
		//not for you
	}
	
	public static String cleanPhoneNumber(String target){
		  //TODO we could assume that if there are more than 10 then the final parts are an extension
		  String currentTarget = target.replaceAll("[^0-9]", "");
		  StringBuilder retval = new StringBuilder();
		  if(currentTarget.length() == 10){
			retval.append("(").append(currentTarget.substring(0, 3)).append(")").append(" ");
			currentTarget = currentTarget.substring(3);
		  }
		  if(currentTarget.length() == 7){
			  retval.append(currentTarget.substring(0, 3)).append("-");
			  currentTarget = currentTarget.substring(3);
		  }
		  if(currentTarget.length() == 4){
			  retval.append(currentTarget);
		  }
		  if(retval.length() == 0){
			  return target;
		  }
		  return retval.toString();
	  }
}
