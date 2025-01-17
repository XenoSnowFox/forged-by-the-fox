package com.xenosnowfox.forgedbythefox.service.account;

import com.xenosnowfox.forgedbythefox.models.account.AccountName;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record CreateAccountRequest(@NonNull AccountName name, @NonNull String profilePictureUrl) {}
