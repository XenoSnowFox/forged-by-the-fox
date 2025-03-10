package com.xenosnowfox.forgedbythefox.routes.fragment;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.xenosnowfox.forgedbythefox.models.Attribute;
import com.xenosnowfox.forgedbythefox.models.account.Account;
import com.xenosnowfox.forgedbythefox.models.character.Character;
import com.xenosnowfox.forgedbythefox.models.session.Session;
import com.xenosnowfox.forgedbythefox.service.character.CharacterMutationExecutor;
import com.xenosnowfox.forgedbythefox.service.template.TemplateService;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CharacterExperienceDisplayFragment extends CharacterDisplayFragment {

    @lombok.Builder(builderClassName = "Builder")
    public CharacterExperienceDisplayFragment(final TemplateService templateService) {
        super("experience", templateService);
    }

    public APIGatewayProxyResponseEvent handlePostRequest(
            final APIGatewayProxyRequestEvent event,
            final Context context,
            final Account account,
            final Session session,
            final Character character) {

        // ensure the character can only be edited by the owner's account
        if (!character.accountIdentifier().equals(account.identifier())) {
            return null;
        }

        final CharacterMutationExecutor mutator =
                this.templateService().characterService().mutate(character);

        Map<String, List<String>> form = this.parseBodyString(event.getBody());

        String attribute =
                Optional.ofNullable(form.get("attribute")).map(List::getFirst).orElse(null);

        if (attribute.equalsIgnoreCase("playbook")) {
            int experience = character.experience().playbook();
            if (form.containsKey("decrement")) {

                experience -= Optional.ofNullable(form.get("decrement"))
                        .map(List::getFirst)
                        .map(Integer::parseInt)
                        .orElse(0);
            }

            if (form.containsKey("increment")) {
                experience += Optional.ofNullable(form.get("increment"))
                        .map(List::getFirst)
                        .map(Integer::parseInt)
                        .orElse(0);
            }
            experience = Math.clamp(experience, 0, 8);
            mutator.withPlaybookExperience(experience);
        }

        for (final Attribute a : Attribute.values()) {

            if (!attribute.equalsIgnoreCase(a.name())) {
                continue;
            }

            int experience = character.experience().experienceForAttribute(a);
            if (form.containsKey("decrement")) {

                experience -= Optional.ofNullable(form.get("decrement"))
                        .map(List::getFirst)
                        .map(Integer::parseInt)
                        .orElse(0);
            }

            if (form.containsKey("increment")) {
                experience += Optional.ofNullable(form.get("increment"))
                        .map(List::getFirst)
                        .map(Integer::parseInt)
                        .orElse(0);
            }
            experience = Math.clamp(experience, 0, 6);
            mutator.withAttributeExperience(a, experience);
        }

        return renderPage(event, account, mutator.orNull());
    }
}
