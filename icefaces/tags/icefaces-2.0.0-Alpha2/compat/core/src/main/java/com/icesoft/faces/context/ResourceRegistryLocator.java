package com.icesoft.faces.context;

import org.icefaces.push.DynamicResourceDispatcher;
import org.icefaces.push.http.DynamicResource;
import org.icefaces.push.http.DynamicResourceLinker;

import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ResourceRegistryLocator {
    public static ResourceRegistry locate(FacesContext context) {
        Map session = context.getExternalContext().getSessionMap();
        if (session.containsKey(ResourceRegistry.class.getName())) {
            return (ResourceRegistry) session.get(ResourceRegistry.class.getName());
        } else {
            ResourceRegistry registry = new DynamicResourceDispatcherAdapter((DynamicResourceDispatcher) session.get(DynamicResourceDispatcher.class.getName()), context);
            session.put(ResourceRegistry.class.getName(), registry);
            return registry;
        }
    }

    private static class DynamicResourceDispatcherAdapter implements ResourceRegistry {
        private ArrayList cssRuleURIs = new ArrayList();
        private ArrayList jsCodeURIs = new ArrayList();
        private DynamicResourceDispatcher resourceDispatcher;

        private DynamicResourceDispatcherAdapter(DynamicResourceDispatcher resourceDispatcher, FacesContext context) {
            this.resourceDispatcher = resourceDispatcher;
        }

        public URI loadJavascriptCode(final Resource resource) {
            String uri = resourceDispatcher.registerResource(new DynamicResourceAdapter(resource)).toString();
            if (!jsCodeURIs.contains(uri)) {
                jsCodeURIs.add(uri);
            }
            return resolve(uri);
        }

        public URI loadJavascriptCode(Resource resource, final ResourceLinker.Handler linkerHandler) {
            String uri = resourceDispatcher.registerResource(new DynamicResourceAdapter(resource), new DynamicHandlerAdapter(linkerHandler)).toString();
            if (!jsCodeURIs.contains(uri)) {
                jsCodeURIs.add(uri);
            }
            return resolve(uri);
        }

        public URI loadCSSRules(Resource resource) {
            String uri = resourceDispatcher.registerResource(new DynamicResourceAdapter(resource)).toString();
            if (!cssRuleURIs.contains(uri)) {
                cssRuleURIs.add(uri);
            }
            return resolve(uri);
        }


        public URI loadCSSRules(Resource resource, ResourceLinker.Handler linkerHandler) {
            String uri = resourceDispatcher.registerResource(new DynamicResourceAdapter(resource), new DynamicHandlerAdapter(linkerHandler)).toString();
            if (!cssRuleURIs.contains(uri)) {
                cssRuleURIs.add(uri);
            }
            return resolve(uri);
        }

        public URI registerResource(Resource resource) {
            return resolve(resourceDispatcher.registerResource(new DynamicResourceAdapter(resource)).toString());
        }

        public URI registerResource(Resource resource, ResourceLinker.Handler linkerHandler) {
            return resolve(resourceDispatcher.registerResource(new DynamicResourceAdapter(resource), new DynamicHandlerAdapter(linkerHandler)).toString());
        }

        private URI resolve(String uri) {
            FacesContext context = FacesContext.getCurrentInstance();
            return URI.create(context.getApplication().getViewHandler().getResourceURL(context, uri));
        }

        private static class DynamicResourceAdapter implements DynamicResource {
            private final Resource resource;

            public DynamicResourceAdapter(Resource resource) {
                this.resource = resource;
            }

            public String calculateDigest() {
                return resource.calculateDigest();
            }

            public InputStream open() throws IOException {
                return resource.open();
            }

            public Date lastModified() {
                return resource.lastModified();
            }

            public void withOptions(final Options options) throws IOException {
                resource.withOptions(new Resource.Options() {
                    public void setMimeType(String mimeType) {
                        options.setMimeType(mimeType);
                    }

                    public void setLastModified(Date date) {
                        options.setLastModified(date);
                    }

                    public void setFileName(String fileName) {
                        options.setFileName(fileName);
                    }

                    public void setExpiresBy(Date date) {
                        options.setExpiresBy(date);
                    }

                    public void setAsAttachement() {
                        options.setAsAttachement();
                    }
                });
            }
        }

        private static class DynamicHandlerAdapter implements DynamicResourceLinker.Handler {
            private final ResourceLinker.Handler linkerHandler;

            public DynamicHandlerAdapter(ResourceLinker.Handler linkerHandler) {
                this.linkerHandler = linkerHandler;
            }

            public void linkWith(final DynamicResourceLinker linker) {
                linkerHandler.linkWith(new ResourceLinker() {
                    public void registerRelativeResource(String path, Resource resource) {
                        linker.registerRelativeResource(path, new DynamicResourceAdapter(resource));
                    }
                });
            }
        }
    }

    public interface ExtendedResourceOptions extends Resource.Options {
        public void setContentDispositionFileName(String contentDispositionFileName);
    }
}
