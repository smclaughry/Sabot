package org.sabot.shared.command;

public interface DescriptiveAction<R extends Result> extends Action<R> {
	String getDescription();
}
