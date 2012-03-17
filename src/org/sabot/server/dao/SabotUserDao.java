package org.sabot.server.dao;

import org.sabot.shared.beans.SabotUser;

public interface SabotUserDao<T extends SabotUser>  {

	T getCurrentUser();

}
