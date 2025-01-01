package com.xenosnowfox.forgedbythefox.services.sessionmanagement;

import com.xenosnowfox.forgedbythefox.library.models.account.AccountIdentifier;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record CreateSessionRequest(@NonNull AccountIdentifier accountIdentifier) {}
