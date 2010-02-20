package org.icefaces.application;

public interface PortableRenderer {

    /**
     * Render the specified group of sessions by performing the JavaServer Faces
     * execute and render lifecycle phases.  If a FacesContext is in the
     * scope of the current thread scope, the current view will not be
     * asynchronously rendered
     * (it is already rendered as a result of the user event being
     * processed).  For more fine-grained control
     * use the RenderManager API.
     */
    void render(String group);
}
