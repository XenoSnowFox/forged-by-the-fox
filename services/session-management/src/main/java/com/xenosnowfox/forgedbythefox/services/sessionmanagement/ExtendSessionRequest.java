package com.xenosnowfox.forgedbythefox.services.sessionmanagement;

import com.xenosnowfox.forgedbythefox.library.models.session.SessionIdentifier;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record ExtendSessionRequest(@NonNull SessionIdentifier identifier) {}
