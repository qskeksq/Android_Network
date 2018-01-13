<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    
    <%
    	final int new_version = 5;
    	
   	String result = null;
   	String cur_version = request.getParameter("CURRENT_VERSION");
   	
   	if( cur_version != null){
   		result = String.valueOf(new_version);
   	}
   	
   	out.println("App_Version ="+result+"\n");
   	
    %>
  