package com.predic8.membrane.core.rules;

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.predic8.membrane.core.interceptor.Interceptor;

public interface Rule {
	
	public List<Interceptor> getInInterceptors();
	
	public List<Interceptor> getOutInterceptors();
	
	public boolean isBlockRequest();
	
	public boolean isBlockResponse();
	
	public RuleKey getRuleKey();
	
	public void setRuleKey(RuleKey ruleKey);
	
	public void write(XMLStreamWriter out) throws XMLStreamException;
	
	public void setName(String name);
	
	public String getName();
	
	public void setBlockRequest(boolean blockStatus);
	
	public void setBlockResponse(boolean blockStatus);
}
