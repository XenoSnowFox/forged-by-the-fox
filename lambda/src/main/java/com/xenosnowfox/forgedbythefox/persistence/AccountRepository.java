package com.xenosnowfox.forgedbythefox.persistence;

import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;

public interface AccountRepository {

	Account retrieve(AccountIdentifier withIdentifier);
}
