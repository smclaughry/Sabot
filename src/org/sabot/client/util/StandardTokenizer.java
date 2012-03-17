package org.sabot.client.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sabot.shared.beans.IsKeyed;

public class StandardTokenizer{
	private static final String PARAM_SEPARATOR = "~";
	private static final String PARAM_PATTERN = PARAM_SEPARATOR + "(?!" + PARAM_SEPARATOR + ")";
    private static final String PARAM_ESCAPE = PARAM_SEPARATOR + PARAM_SEPARATOR;
    private static final String VALUE_SEPARATOR = "=";
    private static final String VALUE_PATTERN = VALUE_SEPARATOR + "(?!" + VALUE_SEPARATOR + ")";
    private static final String VALUE_ESCAPE = VALUE_SEPARATOR + VALUE_SEPARATOR;

    private final Map<Parameters, String> params = new HashMap<Parameters, String>();
    
    public StandardTokenizer() {
    	// just a blank jobbie
    }
    
    public StandardTokenizer(Parameters key, String value) {
    	this.params.put(key, value);
    }
    
    public StandardTokenizer(Parameters key, Long value) {
    	this.params.put(key, String.valueOf(value));
    }
    
    private StandardTokenizer(StandardTokenizer util, Parameters key, String value) {
    	if(util.params != null){
    		this.params.putAll(util.params);
    	}
    	this.params.put(key, value);
    }
    

	public StandardTokenizer with(Parameters key, String value){
    	return new StandardTokenizer(this, key, value);
    }
    
    public StandardTokenizer with(Parameters key, Long value){
    	return with(key, String.valueOf(value));
    }
    
    public StandardTokenizer with(Parameters key, Boolean value){
    	return with(key, String.valueOf(value));
    }
 
    public StandardTokenizer with(Parameters key, Date value){
    	if(value == null){
    		return this;
    	}else{
    		return with(key, value.getTime());
    	}
    }
    
    public <T extends IsKeyed<T>> StandardTokenizer with(Parameters key, T value){
    	if(value == null){
    		return this;
    	}else{
    		return with(key, value.getKey());
    	}
    }
    
    public <T extends IsKeyed<T>> StandardTokenizer with(Parameters param, List<T> isKeyeds) {
    	if(isKeyeds == null){
    		return this;
    	}
    	StringBuilder values = new StringBuilder();
    	for(T keyed : isKeyeds){
    		if(values.length() > 0){
    			values.append(", ");
    		}
    		values.append(keyed.getKey());
    	}
    	return this.with(param, values.toString());
    }
    
    @Override
	public String toString(){
		StringBuilder out = new StringBuilder();

        if ( params != null && !params.isEmpty() ) {
            for ( Map.Entry<Parameters, String> entry : params.entrySet() ) {
                out.append( PARAM_SEPARATOR );
                out.append( escape( entry.getKey().getKey() ) ).append( VALUE_SEPARATOR )
                        .append( escape( entry.getValue() ) );
            }
        }
        return out.toString();
	}
	
	public String getStringParameter(Parameters param){
		return params.get(param);
	}

	public Long getLongParameter(Parameters param){
		try{
			return Long.parseLong(params.get(param));
		}catch(NumberFormatException exception){
			return null;
		}
	}
	
	public Boolean getBooleanParameter(Parameters param) {
		return Boolean.parseBoolean(params.get(param));
	}
	
	public Date getDateParameter(Parameters startDate) {
		Long longParameter = getLongParameter(startDate);
		if(longParameter == null){
			return null;
		}
		return new Date(longParameter);
	}
	
	public <T extends IsKeyed<T>> T getIsKeyedParameter(Parameters isKeyedParam, T starter){
		return starter.forKey(getStringParameter(isKeyedParam));
	}
	
	public <T extends IsKeyed<T>> List<T> getIsKeyedListParameter(Parameters isKeyedParam, T starter){
		List<T> values = new ArrayList<T>();
		String keyList = getStringParameter(isKeyedParam);
		if(keyList != null){
			for(String key : keyList.split(",")){
				T isKeyed = starter.forKey(key);
				if(isKeyed != null){
					values.add(isKeyed);
				}
			}
		}
		return values;
	}
	
    private static String escape( String value ) {
    	if (null == value) {
    		return null;
    	} else {
    		return value.replaceAll( PARAM_SEPARATOR, PARAM_ESCAPE ).replaceAll( VALUE_SEPARATOR, VALUE_ESCAPE );
    	}
    }
    
    public static StandardTokenizer fromString( String token, Parameters defaultParameter ) {
    	StandardTokenizer req = new StandardTokenizer();
    	String[] paramTokens = token.split( PARAM_PATTERN );
        for ( String paramToken : paramTokens ) {
            String[] param = paramToken.split( VALUE_PATTERN );
            if ( param.length == 2 ){                	
            	req = req.with(defaultParameter.forString(unescape( param[0])), unescape( param[1] ) );
            }
        }
        return req;
    }
    
    private static String unescape( String value ) {
        return value.replaceAll( PARAM_ESCAPE, PARAM_SEPARATOR ).replaceAll( VALUE_ESCAPE, VALUE_SEPARATOR );
    }
    
    public static boolean safeEquals(Object lhs, Object rhs){
		if(lhs == rhs) {return true;}
		if(lhs == null || rhs == null) {return false;}
		return lhs.equals(rhs);
	}

}