package com.xenosnowfox.forgedbythefox.service.session;

import com.xenosnowfox.forgedbythefox.models.session.SessionIdentifier;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record ExtendSessionRequest(@NonNull SessionIdentifier identifier) {}
