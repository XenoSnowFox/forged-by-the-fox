package com.xenosnowfox.forgedbythefox.services.charactermanagement;

import com.xenosnowfox.forgedbythefox.library.models.Playbook;
import com.xenosnowfox.forgedbythefox.library.models.account.AccountIdentifier;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record CreateCharacterRequest(@NonNull AccountIdentifier accountIdentifier, @NonNull Playbook playbook) {}
