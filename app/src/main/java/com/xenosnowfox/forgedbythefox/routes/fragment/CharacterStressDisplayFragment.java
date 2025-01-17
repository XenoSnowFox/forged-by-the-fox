package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.xenosnowfox.forgedbythefox.models.Trauma;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class CharacterStressDisplayFragment extends CharacterDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CharacterStressDisplayFragment(final TemplateService templateService) {
        super("stress", templateService);
    }

    public void handlePostRequest(
            @NotNull final io.javalin.http.Context context,
            final Account account,
            final Session session,
            final Character character) {

        // ensure the character can only be edited by the owner's account
        if (!character.accountIdentifier().equals(account.identifier())) {
            return;
        }

        final Set<Trauma> trauma = new HashSet<>();
        Arrays.stream(Trauma.values())
                .filter(t -> context.formParam("trauma." + t.name()) != null)
                .forEach(trauma::add);

        final Character mutatedCharacter = this.templateService()
                .characterService()
                .mutate(character)
                .withStress(Optional.ofNullable(context.formParam("stress"))
                        .map(Integer::parseInt)
                        .orElse(null))
                .withTrauma(trauma)
                .orNull();

        renderPage(context, account, mutatedCharacter);
    }
}
