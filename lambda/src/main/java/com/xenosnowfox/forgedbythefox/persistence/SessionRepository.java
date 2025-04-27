package com.xenosnowfox.forgedbythefox.persistence;

import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.models.session.SessionIdentifier;

public interface SessionRepository {
	Session retrieve(SessionIdentifier withIdentifier);
}
