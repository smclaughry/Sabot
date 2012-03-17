package org.sabot.shared.beans;

import java.io.Serializable;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

public class ClientActivityInfoConfiguration implements Serializable {

	private static final long serialVersionUID = 4971684220629755656L;

	public static enum ClientActivityInfoReportingPeriodType{
		OFF,
		REPEAT;
	}
	
	private ClientActivityInfoReportingPeriodType statisticsReportingPeriodType;
	private int statisticsReportingDelayMillis;

	public void setInfoReportingPeriodType(
			ClientActivityInfoReportingPeriodType statisticsReportingPeriodType) {
		this.statisticsReportingPeriodType = statisticsReportingPeriodType;
	}

	public ClientActivityInfoReportingPeriodType getInfoReportingPeriodType() {
		return statisticsReportingPeriodType;
	}

	public void setInfoReportingDelayMillis(int statisticsReportingDelayMillis) {
		this.statisticsReportingDelayMillis = statisticsReportingDelayMillis;
	}

	public int getInfoReportingDelayMillis() {
		return statisticsReportingDelayMillis;
	}
	
	@Override
	public boolean equals(Object other){
		if (other == null) { return false; }
		if (other == this) { return true; }
		if (other.getClass() != getClass()) {return false;}
		
		ClientActivityInfoConfiguration otherSpecific = (ClientActivityInfoConfiguration) other;
		
		return new EqualsBuilder().append(statisticsReportingPeriodType, otherSpecific.statisticsReportingPeriodType)
								  .append(statisticsReportingDelayMillis, otherSpecific.statisticsReportingDelayMillis)
								  .isEquals();
	}
	
	@Override
	public int hashCode(){
		return new HashCodeBuilder(51, 41).append(statisticsReportingPeriodType)
										  .append(statisticsReportingDelayMillis)
										  .toHashCode();
	}
}
