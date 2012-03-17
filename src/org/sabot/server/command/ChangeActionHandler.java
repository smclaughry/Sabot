package org.sabot.server.command;

import org.sabot.server.ActionHandler;
import org.sabot.server.ExecutionContext;
import org.sabot.shared.command.Action;
import org.sabot.shared.command.Result;
import org.sabot.shared.exception.ActionException;


public interface ChangeActionHandler<A extends Action<R>, R extends Result> extends ActionHandler<A,R> {

    /**
     * Attempts to roll back the specified action.
     * 
     * @param action
     *            The action.
     * @param result
     *            The result of the action.
     * @param context
     *            The execution context.
     * @throws ServiceException
     * @throws ActionException
     */
    void rollback( A action, R result, ExecutionContext context ) throws ActionException;
}
