package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.xenosnowfox.forgedbythefox.models.Ability;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class CharacterAbilitiesDisplayFragment extends CharacterDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CharacterAbilitiesDisplayFragment(final TemplateService templateService) {
        super("abilities", templateService);
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

        final Map<String, List<String>> data = context.formParamMap();

        final Set<Ability> abilities =
                new HashSet<>(List.of(character.playbook().startingAbility()));
        character.playbook().specialAbilities().stream()
                .filter(ability -> data.containsKey("ability." + ability.name()))
                .forEach(abilities::add);

        final Character mutatedCharacter = this.templateService()
                .characterService()
                .mutate(character)
                .withAbilities(abilities)
                .orNull();

        renderPage(context, account, mutatedCharacter);
    }
}
