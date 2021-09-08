package ru.playa.keycloak.modules.requiredactions;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.keycloak.Config;
import org.keycloak.OAuth2Constants;
import org.keycloak.authentication.DisplayTypeRequiredActionFactory;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.authentication.requiredactions.ConsoleTermsAndConditions;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.UserModel;
import org.keycloak.userprofile.UserProfile;
import org.keycloak.userprofile.profile.UserProfileContextFactory;
import org.keycloak.userprofile.utils.UserUpdateHelper;
import org.keycloak.userprofile.validation.UserProfileValidationResult;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class MyTermsAndConditions
        implements RequiredActionProvider, RequiredActionFactory, DisplayTypeRequiredActionFactory {
    public static final String PROVIDER_ID = "my_terms_and_conditions";
    public static final String USER_ATTRIBUTE = PROVIDER_ID;

    @Override
    public RequiredActionProvider create(KeycloakSession session) {
        return this;
    }

    @Override
    public RequiredActionProvider createDisplay(KeycloakSession session, String displayType) {
        if (displayType == null) {
            return this;
        }
        if (!OAuth2Constants.DISPLAY_CONSOLE.equalsIgnoreCase(displayType)) {
            return null;
        }
        return ConsoleTermsAndConditions.SINGLETON;
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }


    @Override
    public void evaluateTriggers(RequiredActionContext context) {

    }

    @Override
    public void requiredActionChallenge(RequiredActionContext context) {
        Response challenge = context.form().createForm("my_terms.ftl");
        context.challenge(challenge);
    }

    @Override
    public void processAction(RequiredActionContext context) {
        if (context.getHttpRequest().getDecodedFormParameters().containsKey("cancel")) {
            context.getUser().removeAttribute(USER_ATTRIBUTE);
            context.failure();
            return;
        }

        final UserModel user = context.getUser();
        final MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        final UserProfileValidationResult result = UserProfileContextFactory
                .forUpdateProfile(user, formData, context.getSession())
                .validate();
        final UserProfile updatedProfile = result.getProfile();

        updatedProfile.getAttributes().setAttribute("lastName_1", formData.get("lastName"));
        updatedProfile.getAttributes().setAttribute("firstName_1", formData.get("firstName"));

        UserUpdateHelper.updateUserProfile(context.getRealm(), user, updatedProfile);

        context.success();
    }

    @Override
    public String getDisplayText() {
        return "My Terms and Conditions";
    }

    @Override
    public void close() {

    }

}
