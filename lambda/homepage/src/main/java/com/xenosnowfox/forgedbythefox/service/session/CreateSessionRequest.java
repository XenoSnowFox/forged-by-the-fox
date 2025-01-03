package com.xenosnowfox.forgedbythefox.service.session;

import com.xenosnowfox.forgedbythefox.models.account.AccountIdentifier;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record CreateSessionRequest(@NonNull AccountIdentifier accountIdentifier) {}
