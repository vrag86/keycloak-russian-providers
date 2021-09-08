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
public class EmailEnter
        implements RequiredActionProvider, RequiredActionFactory, DisplayTypeRequiredActionFactory {
    public static final String PROVIDER_ID = "email_enter";

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
        Response challenge = context.form().createForm("email_enter.ftl");
        context.challenge(challenge);
    }

    @Override
    public void processAction(RequiredActionContext context) {
        final UserModel user = context.getUser();
        final MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        final UserProfileValidationResult result = UserProfileContextFactory
                .forUpdateProfile(user, formData, context.getSession())
                .validate();
        final UserProfile updatedProfile = result.getProfile();

        user.setEmail(formData.getFirst("email"));

        Singleton.SINGLETON.put(formData.getFirst("email"), formData.getFirst("email") + "_1");

        UserUpdateHelper.updateUserProfile(context.getRealm(), user, updatedProfile);

        context.success();
    }

    @Override
    public String getDisplayText() {
        return "Enter email";
    }

    @Override
    public void close() {

    }
}
