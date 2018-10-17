FROM jboss/keycloak:4.5.0.Final

ENV JBOSS_HOME /opt/jboss/keycloak
ENV THEMES_HOME $JBOSS_HOME/themes/base/admin

COPY target/classes/theme/base/admin/resources/partials $THEMES_HOME/resources/partials
COPY target/classes/theme/base/admin/messages/ $THEMES_HOME/messages
RUN cat /opt/jboss/keycloak/themes/base/admin/messages/admin-messages_en.custom >> /opt/jboss/keycloak/themes/base/admin/messages/admin-messages_en.properties
RUN cat /opt/jboss/keycloak/themes/base/admin/messages/admin-messages_ru.custom >> /opt/jboss/keycloak/themes/base/admin/messages/admin-messages_ru.properties
COPY target/keycloak-russian-providers.jar $JBOSS_HOME/standalone/deployments/keycloak-russian-providers.jar

