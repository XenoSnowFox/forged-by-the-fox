package com.xenosnowfox.forgedbythefox.services.accountmanagement;

import com.xenosnowfox.forgedbythefox.library.models.account.AccountName;
import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public record CreateAccountRequest(@NonNull AccountName name, @NonNull String profilePictureUrl) {}
