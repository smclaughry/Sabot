package org.sabot.server.utility;

import org.apache.commons.logging.Log;
import org.sabot.client.util.StatsCollector;


public class StatsLogger {
    private final StatsCollector collector;

    public StatsLogger() {
        this.collector = new StatsCollector();
    }

    public void log(String statistic, long value) {
        this.collector.log(statistic, value);
    }

    public void count(String statistic) {
        this.collector.count(statistic);
    }

    public enum LogLevel{
    	TRACE{
				@Override
				public boolean isEnabledIn(Log log) {
					return log.isTraceEnabled();
				}

				@Override
				public void logAt(Log log, String stuff) {
					log.trace(stuff);
				}
		},    	
		DEBUG{
			@Override
			public boolean isEnabledIn(Log log) {
				return log.isDebugEnabled();
			}

			@Override
			public void logAt(Log log, String stuff) {
				log.debug(stuff);
			}
		},
    	INFO{
			@Override
			public boolean isEnabledIn(Log log) {
				return log.isInfoEnabled();
			}

			@Override
			public void logAt(Log log, String stuff) {
				log.info(stuff);
			}
		},
    	WARN{
			@Override
			public boolean isEnabledIn(Log log) {
				return log.isWarnEnabled();
			}

			@Override
			public void logAt(Log log, String stuff) {
				log.warn(stuff);
			}
		},
    	ERROR{
			@Override
			public boolean isEnabledIn(Log log) {
				return log.isErrorEnabled();
			}

			@Override
			public void logAt(Log log, String stuff) {
				log.error(stuff);
			}
		},
    	FATAL{
			@Override
			public boolean isEnabledIn(Log log) {
				return log.isFatalEnabled();
			}

			@Override
			public void logAt(Log log, String stuff) {
				log.fatal(stuff);
			}

		};
    	
    	public abstract boolean isEnabledIn(Log log);
    	public abstract void logAt(Log log, String stuff);
    }
    
    public void report(Log log, LogLevel logLevel) {
        if (logLevel.isEnabledIn(log)) { // No point collecting stats if they won't be reported
            String allStats = collector.report();
            if (0 != allStats.length()) {
            	logLevel.logAt(log, allStats);
            }
        }
    }

	public void log(String statistic, double value) {
		this.collector.log(statistic, value);
	}
} // class StatsLogger
