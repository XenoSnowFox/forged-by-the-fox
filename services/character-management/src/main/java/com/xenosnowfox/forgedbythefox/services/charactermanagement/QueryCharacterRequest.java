package com.xenosnowfox.forgedbythefox.services.charactermanagement;

import com.xenosnowfox.forgedbythefox.library.models.Playbook;
import com.xenosnowfox.forgedbythefox.library.models.account.AccountIdentifier;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record QueryCharacterRequest(@NonNull AccountIdentifier accountIdentifier, Playbook playbook) {}
