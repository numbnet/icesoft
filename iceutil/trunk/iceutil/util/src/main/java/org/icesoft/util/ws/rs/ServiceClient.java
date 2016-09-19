package org.icesoft.util.ws.rs;

import java.io.Closeable;
import java.util.Map;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ServiceClient
extends Closeable {
    Response delete(
        String requestURI)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    Response delete(
        String requestURI, Map<String, Object> headerMap)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    Response delete(
        String requestURI, Map<String, Object> headerMap, MediaType mediaType)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    Response delete(
        String requestURI, MediaType mediaType)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    Response delete(
        String requestURI,
        String userName, String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    Response delete(
        String requestURI, Map<String, Object> headerMap,
        String userName, String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    Response delete(
        String requestURI, Map<String, Object> headerMap, MediaType mediaType,
        String userName, String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    Response delete(
        String requestURI, MediaType mediaType,
        String userName, String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    Response get(
        String requestURI)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    Response get(
        String requestURI, Map<String, Object> headerMap)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    Response get(
        String requestURI, Map<String, Object> headerMap, MediaType mediaType)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    Response get(
        String requestURI, MediaType mediaType)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    Response get(
        String requestURI,
        String userName, String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    Response get(
        String requestURI, Map<String, Object> headerMap,
        String userName, String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    Response get(
        String requestURI, Map<String, Object> headerMap, MediaType mediaType,
        String userName, String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    Response get(
        String requestURI, MediaType mediaType,
        String userName, String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException;

    boolean isClosed();

    Response post(
        String requestURI, String entityBody)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException;

    Response post(
        String requestURI, Map<String, Object> headerMap, String entityBody)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException;

    Response post(
        String requestURI, Map<String, Object> headerMap, MediaType mediaType, String entityBody)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException;

    Response post(
        String requestURI, MediaType mediaType, String entityBody)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException;

    Response post(
        String requestURI, String entityBody,
        String userName, String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException;

    Response post(
        String requestURI, Map<String, Object> headerMap, String entityBody,
        String userName, String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException;

    Response post(
        String requestURI, Map<String, Object> headerMap, MediaType mediaType, String entityBody,
        String userName, String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException;

    Response post(
        String requestURI, MediaType mediaType, String entityBody,
        String userName, String password)
    throws
        IllegalArgumentException, IllegalStateException, NullPointerException, ProcessingException,
        ResponseProcessingException;
}
