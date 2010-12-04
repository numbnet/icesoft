package org.icefaces.component.fileentry;

import org.icefaces.util.EnvUtils;
import org.icefaces.application.ResourceRegistry;
import org.icepush.PushContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Consolidates the push functionality used by the FileEntry component, for
 * pushing the progress information to the browser.
 *
 * TODO: Use reflection
 * TODO: Handle portlets scenario
 */
class PushUtils {
    static String PROGRESS_PREFIX =
            "org.icefaces.component.fileentry.progress.";
    private static final String COMPONENT_ATTRIBUTE_PROGRESS_RESOURCE_PATH =
            PROGRESS_PREFIX + "path_";
    private static final String COMPONENT_ATTRIBUTE_PROGRESS_PUSH_ID =
            PROGRESS_PREFIX + "push_id_";
    private static final String COMPONENT_ATTRIBUTE_PROGRESS_GROUP_NAME =
            PROGRESS_PREFIX + "group_name_";
    private static final String PROGRESS_GROUP_NAME_PREFIX =
            PROGRESS_PREFIX + "group_name.";

    static boolean isPushPresent() {
        return EnvUtils.isICEpushPresent();
    }

    /**
     * Create a path that is unique to the view and the component, in
     * iteration, and register a progress resource under that path.
     *
     * Note: Calling this several times in a lifecycle needs to be
     * deterministic, since the FileEntryConfig needs to store it away for
     * later, and FormScriptWriter renders it out.
     *
     * @param context FacesContext
     * @param comp The component the resource is tied to
     * @return Resource path
     */
    static String getProgressResourcePath(
            FacesContext context, UIComponent comp) {
        // It's an implementation detail for the resource to be added in
        // session scope
        final String attribKey = COMPONENT_ATTRIBUTE_PROGRESS_RESOURCE_PATH +
                comp.getClientId(context);
        String resPath = (String) comp.getAttributes().get(attribKey);
        if (resPath == null) {
            String identifier = FileEntry.getGloballyUniqueComponentIdentifier(
                    context, comp.getClientId(context));
            Resource res = new ProgressResource(identifier);
            resPath = ResourceRegistry.addSessionResource(res);
            comp.getAttributes().put(attribKey, resPath);
        }
        return resPath;
    }

    /**
     * Create a push id that is tied to the component, in iteration. It is
     * created once, and stored in the component itself.
     *
     * Note: This method has side-effects, and so should only be called by
     * FormScriptWriter.
     *
     * @param context FacesContext
     * @param comp The component the push id is tied to, and stored in
     * @return Push id
     */
    static String getPushId(FacesContext context, UIComponent comp) {
        final String attribKey = COMPONENT_ATTRIBUTE_PROGRESS_PUSH_ID +
                comp.getClientId(context);
        String pushId = (String) comp.getAttributes().get(attribKey);
        if (pushId == null) {
            pushId = createPushId(context);
            comp.getAttributes().put(attribKey, pushId);

            String groupName = getPushGroupName(context, comp);
            addPushGroupMember(groupName, pushId);
        }
        return pushId;
    }

    /**
     * The push api doesn't cover the simplest case of just pushing by push id,
     * as it assumes you'll want to push a group of push ids at once, so our
     * progress notifications will be done via a unique group name, that's tied
     * to the component, in iteration.
     *
     * Note: Calling this several times in a lifecycle needs to be
     * deterministic, since the FileEntryConfig needs to store it away for
     * later, and getPushId() may use it.
     *
     * @param context FacesContext
     * @param comp The component the group name is tied to
     * @return Push group name
     */
    static String getPushGroupName(FacesContext context, UIComponent comp) {
        final String attribKey = COMPONENT_ATTRIBUTE_PROGRESS_GROUP_NAME +
                comp.getClientId(context);
        String groupName = (String) comp.getAttributes().get(attribKey);
        if (groupName == null) {
            String identifier = FileEntry.getGloballyUniqueComponentIdentifier(
                    context, comp.getClientId(context));
            groupName = PROGRESS_GROUP_NAME_PREFIX + identifier;
            comp.getAttributes().put(attribKey, groupName);
        }
        return groupName;
    }

    static String createPushId(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        ServletContext servletContext = (ServletContext)
                externalContext.getContext();
        HttpServletRequest request = (HttpServletRequest)
                externalContext.getRequest();
        HttpServletResponse response = (HttpServletResponse)
                externalContext.getResponse();
        return PushContext.getInstance(servletContext).createPushId(
                request, response);
    }

    static void addPushGroupMember(String groupName, String pushId) {
        ExternalContext externalContext =
                FacesContext.getCurrentInstance().getExternalContext();
        ServletContext servletContext = (ServletContext)
                externalContext.getContext();
        PushContext.getInstance(servletContext).addGroupMember(
                groupName, pushId);
    }

    static void removePushGroupMember(String groupName, String pushId) {
        ExternalContext externalContext =
                FacesContext.getCurrentInstance().getExternalContext();
        ServletContext servletContext = (ServletContext)
                externalContext.getContext();
        PushContext.getInstance(servletContext).removeGroupMember(
                groupName, pushId);
    }
    
    static void push(String groupName) {
        ExternalContext externalContext =
                FacesContext.getCurrentInstance().getExternalContext();
        ServletContext servletContext = (ServletContext)
                externalContext.getContext();
        PushContext.getInstance(servletContext).push(groupName);
    }
}
