package org.icefaces.component.fileentry;

import org.icefaces.application.ResourceRegistry;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

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

    private static Class org_icepush_PushContext_class;
    private static Method org_icepush_PushContext_getInstance_method;
    private static Method org_icepush_PushContext_createPushId_method;
    private static Method org_icepush_PushContext_addGroupMember_method;
    private static Method org_icepush_PushContext_removeGroupMember_method;
    private static Method org_icepush_PushContext_push_method;
    static {
        try {
            org_icepush_PushContext_class = Class.forName(
                    "org.icepush.PushContext");
            org_icepush_PushContext_getInstance_method =
                    org_icepush_PushContext_class.getMethod("getInstance",
                            javax.servlet.ServletContext.class);
            org_icepush_PushContext_createPushId_method =
                    org_icepush_PushContext_class.getMethod("createPushId",
                            javax.servlet.http.HttpServletRequest.class,
                            javax.servlet.http.HttpServletResponse.class);
            org_icepush_PushContext_addGroupMember_method =
                    org_icepush_PushContext_class.getMethod("addGroupMember",
                            String.class, String.class);
            org_icepush_PushContext_removeGroupMember_method =
                    org_icepush_PushContext_class.getMethod("removeGroupMember",
                            String.class, String.class);
            org_icepush_PushContext_push_method =
                    org_icepush_PushContext_class.getMethod("push",
                            String.class);
        } catch (ClassNotFoundException e) {
            org_icepush_PushContext_class = null;
        } catch (NoSuchMethodException e) {
            org_icepush_PushContext_class = null;
        }
    }

    static boolean isPushPresent() {
        return org_icepush_PushContext_class != null;
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
            pushId = createPushId();
            if (pushId != null) {
                comp.getAttributes().put(attribKey, pushId);

                String groupName = getPushGroupName(context, comp);
                addPushGroupMember(groupName, pushId);
            }
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

    static String createPushId() {
        ExternalContext externalContext =
                FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest)
                externalContext.getRequest();
        HttpServletResponse response = (HttpServletResponse)
                externalContext.getResponse();
        String id = null;
        // PushContext.getInstance(servletContext).createPushId(
        //         request, response);
        try {
            Object pushContext = reflectPushContextInstance();
            if (pushContext != null) {
                id = (String) org_icepush_PushContext_createPushId_method.
                        invoke(pushContext, request, response);
            }
        } catch (Exception e) {
        }
        return id;
    }

    static void addPushGroupMember(String groupName, String pushId) {
        // PushContext.getInstance(servletContext).addGroupMember(
        //         groupName, pushId);
        try {
            Object pushContext = reflectPushContextInstance();
            if (pushContext != null) {
                org_icepush_PushContext_addGroupMember_method.invoke(
                        pushContext, groupName, pushId);
            }
        } catch (Exception e) {
        }
    }

    static void removePushGroupMember(String groupName, String pushId) {
        // PushContext.getInstance(servletContext).removeGroupMember(
        //         groupName, pushId);
        try {
            Object pushContext = reflectPushContextInstance();
            if (pushContext != null) {
                org_icepush_PushContext_removeGroupMember_method.invoke(
                        pushContext, groupName, pushId);
            }
        } catch (Exception e) {
        }
    }
    
    static void push(String groupName) {
        // PushContext.getInstance(servletContext).push(groupName);
        try {
            Object pushContext = reflectPushContextInstance();
            if (pushContext != null) {
                org_icepush_PushContext_push_method.invoke(
                        pushContext, groupName);
            }
        } catch (Exception e) {
        }
    }

    private static Object reflectPushContextInstance() {
        ExternalContext externalContext =
                FacesContext.getCurrentInstance().getExternalContext();
        ServletContext servletContext = (ServletContext)
                externalContext.getContext();
        Object inst = null;
        // PushContext.getInstance(servletContext);
        try {
            if (org_icepush_PushContext_getInstance_method != null) {
                inst = org_icepush_PushContext_getInstance_method.invoke(
                        null, servletContext);
            }
        } catch(Exception e) {
        }
        return inst;
    }
}
