package org.icesoft.util.ws.rs;

import static org.icesoft.util.PreCondition.checkIfIsGreaterThan;
import static org.icesoft.util.PreCondition.checkIfIsNotNull;
import static org.icesoft.util.PreCondition.checkIfIsNotNullAndIsNotEmpty;
import static org.icesoft.util.MapUtilities.isNotNullAndIsNotEmpty;
import static org.icesoft.util.StringUtilities.isNotNullAndIsNotEmpty;

import java.io.Closeable;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public abstract class AbstractServiceClient
implements Closeable, ServiceClient {
    private static final Logger LOGGER = Logger.getLogger(AbstractServiceClient.class.getName());

    private final Client client = ClientBuilder.newClient();

    private final String scheme;
    private final String host;
    private final int port;
    private final String contextPath;
    private final String basePath;

    private boolean closed = false;

    protected AbstractServiceClient(
        final String scheme, final String host, final int port, final String contextPath, final String basePath)
    throws IllegalArgumentException, NullPointerException {
        this.scheme =
            checkIfIsNotNullAndIsNotEmpty(scheme, "Illegal argument scheme: '" + scheme + "'");
        this.host =
            checkIfIsNotNullAndIsNotEmpty(host, "Illegal argument host: '" + host + "'");
        this.port =
            checkIfIsGreaterThan(port, 0, "Illegal argument port: " + port);
        this.contextPath =
            checkIfIsNotNullAndIsNotEmpty(contextPath, "Illegal argument contextPath: '" + contextPath + "'");
        this.basePath = basePath;
    }

    public void close() {
        getClient().close();
        closed = true;
    }

    public Response delete(
        final String requestURI)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        return
            delete(
                requestURI, Collections.<String, Object>emptyMap(), MediaType.APPLICATION_JSON_TYPE
            );
    }

    public Response delete(
        final String requestURI, final Map<String, Object> headerMap)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        return
            delete(
                requestURI, headerMap, MediaType.APPLICATION_JSON_TYPE
            );
    }

    public Response delete(
        final String requestURI, final Map<String, Object> headerMap, final MediaType mediaType)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        checkIfIsNotNull(mediaType);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "DELETE " + requestURI + " with " +
                    "Headers '" + headerMap + "' and Content-Type '" + mediaType + "'."
            );
        }
        Invocation.Builder _builder =
            getClient().
                target(requestURI).
                request(mediaType);
        if (isNotNullAndIsNotEmpty(headerMap)) {
            _builder.headers(new MultivaluedHashMap<String, Object>(headerMap));
        }
        return _builder.delete();
    }

    public Response delete(
        final String requestURI, final MediaType mediaType)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        checkIfIsNotNull(mediaType);
        return
            delete(
                requestURI, Collections.<String, Object>emptyMap(), MediaType.APPLICATION_JSON_TYPE
            );
    }

    public Response delete(
        final String requestURI,
        final String userName, final String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        return
            delete(
                requestURI, Collections.<String, Object>emptyMap(), MediaType.APPLICATION_JSON_TYPE,
                userName, password
            );
    }

    public Response delete(
        final String requestURI, final Map<String, Object> headerMap,
        final String userName, final String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        return
            delete(
                requestURI, headerMap, MediaType.APPLICATION_JSON_TYPE,
                userName, password
            );
    }

    public Response delete(
        final String requestURI, final Map<String, Object> headerMap, final MediaType mediaType,
        final String userName, final String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        checkIfIsNotNull(mediaType);
        if (isNotNullAndIsNotEmpty(userName) && isNotNullAndIsNotEmpty(password)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                    Level.FINE,
                    "DELETE " + requestURI + " with " +
                        "Headers '" + headerMap + "' and Content-Type '" + mediaType + "'.  " +
                            "(User Name: '" + userName + "', Password '" + password.replaceAll(".", "*") + "')"
                );
            }
            Invocation.Builder _builder =
                getClient().
                    register(HttpAuthenticationFeature.basic(userName, password)).
                    target(requestURI).
                    request(mediaType);
            if (isNotNullAndIsNotEmpty(headerMap)) {
                _builder = _builder.headers(new MultivaluedHashMap<String, Object>(headerMap));
            }
            return _builder.delete();
        } else {
            return delete(requestURI, headerMap, mediaType);
        }
    }

    public Response delete(
        final String requestURI, final MediaType mediaType,
        final String userName, final String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        checkIfIsNotNull(mediaType);
        return
            delete(
                requestURI, Collections.<String, Object>emptyMap(), mediaType,
                userName, password
            );
    }

    public Response get(
        final String requestURI)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        return
            get(
                requestURI, Collections.<String, Object>emptyMap(), MediaType.APPLICATION_JSON_TYPE
            );
    }

    public Response get(
        final String requestURI, final Map<String, Object> headerMap)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        return
            get(
                requestURI, headerMap, MediaType.APPLICATION_JSON_TYPE
            );
    }

    public Response get(
        final String requestURI, final Map<String, Object> headerMap, final MediaType mediaType)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        checkIfIsNotNull(mediaType);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "GET " + requestURI + " with " +
                    "Headers '" + headerMap + "' and Content-Type '" + mediaType + "'."
            );
        }
        Invocation.Builder _builder =
            getClient().
                target(requestURI).
                request(mediaType);
        if (isNotNullAndIsNotEmpty(headerMap)) {
            _builder = _builder.headers(new MultivaluedHashMap<String, Object>(headerMap));
        }
        return _builder.get();
    }

    public Response get(
        final String requestURI, final MediaType mediaType)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        checkIfIsNotNull(mediaType);
        return
            get(
                requestURI, Collections.<String, Object>emptyMap(), mediaType
            );
    }

    public Response get(
        final String requestURI,
        final String userName, final String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        return
            get(
                requestURI, Collections.<String, Object>emptyMap(), MediaType.APPLICATION_JSON_TYPE,
                userName, password
            );
    }

    public Response get(
        final String requestURI, final Map<String, Object> headerMap,
        final String userName, final String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        return
            get(
                requestURI, headerMap, MediaType.APPLICATION_JSON_TYPE,
                userName, password
            );
    }

    public Response get(
        final String requestURI, final Map<String, Object> headerMap, final MediaType mediaType,
        final String userName, final String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        checkIfIsNotNull(mediaType);
        if (isNotNullAndIsNotEmpty(userName) && isNotNullAndIsNotEmpty(password)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                    Level.FINE,
                    "GET " + requestURI + " with " +
                        "Headers '" + headerMap + "' and Content-Type '" + mediaType + "'.  " +
                            "(User Name: '" + userName + "', Password '" + password.replaceAll(".", "*") + "')"
                );
            }
            Invocation.Builder _builder =
                getClient().
                    register(HttpAuthenticationFeature.basic(userName, password)).
                    target(requestURI).
                    request(mediaType);
            if (isNotNullAndIsNotEmpty(headerMap)) {
                _builder = _builder.headers(new MultivaluedHashMap<String, Object>(headerMap));
            }
            return _builder.get();
        } else {
            return get(requestURI, headerMap, mediaType);
        }
    }

    public Response get(
        final String requestURI, final MediaType mediaType,
        final String userName, final String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        checkIfIsNotNull(mediaType);
        return
            get(
                requestURI, Collections.<String, Object>emptyMap(), mediaType,
                userName, password
            );
    }

    public boolean isClosed() {
        return closed;
    }

    public Response post(
        final String requestURI, final String entityBody)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        return
            post(
                requestURI, Collections.<String, Object>emptyMap(), MediaType.APPLICATION_JSON_TYPE, entityBody
            );
    }

    public Response post(
        final String requestURI, final Map<String, Object> headerMap, final String entityBody)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        return
            post(
                requestURI, headerMap, MediaType.APPLICATION_JSON_TYPE, entityBody
            );
    }

    public Response post(
        final String requestURI, final Map<String, Object> headerMap, final MediaType mediaType,
        final String entityBody)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        checkIfIsNotNull(mediaType);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(
                Level.FINE,
                "POST " + requestURI + " with " +
                    "Headers '" + headerMap + "', Content-Type '" + mediaType + "' and " +
                    "Entity-Body '" + entityBody + "."
            );
        }
        Invocation.Builder _builder =
            getClient().
                target(requestURI).
                request(mediaType);
        if (isNotNullAndIsNotEmpty(headerMap)) {
            _builder = _builder.headers(new MultivaluedHashMap<String, Object>(headerMap));
        }
        return _builder.post(Entity.entity(entityBody, mediaType));
    }

    public Response post(
        final String requestURI, final MediaType mediaType, final String entityBody)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        checkIfIsNotNull(mediaType);
        return
            post(
                requestURI, Collections.<String, Object>emptyMap(), mediaType, entityBody
            );
    }

    public Response post(
        final String requestURI, final String entityBody,
        final String userName, final String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        return
            post(
                requestURI, Collections.<String, Object>emptyMap(), MediaType.APPLICATION_JSON_TYPE, entityBody,
                userName, password
            );
    }

    public Response post(
        final String requestURI, final Map<String, Object> headerMap, final String entityBody,
        final String userName, final String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        return
            post(
                requestURI, headerMap, MediaType.APPLICATION_JSON_TYPE, entityBody,
                userName, password
            );
    }

    public Response post(
        final String requestURI, final Map<String, Object> headerMap, final MediaType mediaType,
        final String entityBody,
        final String userName, final String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        checkIfIsNotNull(mediaType);
        if (isNotNullAndIsNotEmpty(userName) && isNotNullAndIsNotEmpty(password)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                    Level.FINE,
                    "POST " + requestURI + " with " +
                        "Headers '" + headerMap + "', Content-Type '" + mediaType + "' and " +
                        "Entity-Body '" + entityBody + ".  " +
                            "(User Name: '" + userName + "', Password '" + password.replaceAll(".", "*") + "')"
                );
            }
            Invocation.Builder _builder =
                getClient().
                    register(HttpAuthenticationFeature.basic(userName, password)).
                    target(requestURI).
                    request(mediaType);
            if (isNotNullAndIsNotEmpty(headerMap)) {
                _builder = _builder.headers(new MultivaluedHashMap<String, Object>(headerMap));
            }
            return _builder.post(Entity.entity(entityBody, mediaType));
        } else {
            return post(requestURI, headerMap, mediaType, entityBody);
        }
    }

    public Response post(
        final String requestURI, final MediaType mediaType, final String entityBody,
        final String userName, final String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException {

        checkIfIsNotNullAndIsNotEmpty(requestURI, "Illegal argument requestURI: '" + requestURI + "'");
        checkIfIsNotNull(mediaType);
        return
            post(
                requestURI, Collections.<String, Object>emptyMap(), mediaType, entityBody,
                userName, password
            );
    }

    protected String getBasePath() {
        return basePath;
    }

    protected String getBaseRequestURI() {
        return
            new StringBuilder().
                append(getScheme()).append("://").append(getHost()).append(":").append(getPort()).
                    append("/").append(getContextPath()).
                        append("/").append(getBasePath() != null ? getBasePath() + "/" : "").
                    toString();
    }

    protected Client getClient() {
        return client;
    }

    protected String getContextPath() {
        return contextPath;
    }

    protected String getHost() {
        return host;
    }

    protected int getPort() {
        return port;
    }

    protected String getScheme() {
        return scheme;
    }
}
