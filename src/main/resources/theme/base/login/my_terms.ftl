<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=false; section>
    <#if section = "header">
        ${msg("termsTitle")}
    <#elseif section = "form">
    <div id="kc-terms-text">
        ${kcSanitize(msg("termsText"))?no_esc}
    </div>
    <form class="form-actions" action="${url.loginAction}" method="POST">
        <label for="firstName" class="${properties.kcLabelClass!}">FirstName</label>
        <input tabindex="1" id="firstName" class="${properties.kcInputClass!}" name="firstName" value="${(firstName!'')}" type="text"/>

        <label for="lastName" class="${properties.kcLabelClass!}">LastName</label>
        <input tabindex="1" id="lastName" class="${properties.kcInputClass!}" name="lastName" value="${(lastName!'')}" type="text"/>

        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonLargeClass!}" name="accept" id="kc-accept" type="submit" value="${msg("doAccept")}"/>
        <input class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonLargeClass!}" name="cancel" id="kc-decline" type="submit" value="${msg("doDecline")}"/>
    </form>
    <div class="clearfix"></div>
    </#if>
</@layout.registrationLayout>
