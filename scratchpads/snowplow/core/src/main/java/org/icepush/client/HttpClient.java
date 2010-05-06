package org.icepush.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.icepush.util.ThreadFactory;

public class HttpClient {
    private static final Logger LOGGER = Logger.getLogger(HttpClient.class.getName());

    private final ObjectPool<HttpConnection> connectionPool =
        new ObjectPool<HttpConnection>(2) {
            protected HttpConnection newObject() {
                return new HttpConnection(cookieHandler);
            }
        };
    private final CookieHandler cookieHandler;
    private final Map<URI, ScheduledFuture> pendingRequestMap = new HashMap<URI, ScheduledFuture>();
    private final Map<URI, HttpConnection> connectionMap = new HashMap<URI, HttpConnection>();
    private final ScheduledExecutorService scheduledExecutorService;
    {
        ThreadFactory _threadFactory = new ThreadFactory();
        _threadFactory.setDaemon(false);
        _threadFactory.setPrefix("HTTP Client");
        scheduledExecutorService = new ScheduledThreadPoolExecutor(2, _threadFactory);
    }

    public HttpClient() {
        this(new CookieHandler());
    }

    public HttpClient(final CookieHandler cookieHandler) {
        this.cookieHandler = cookieHandler;
    }

    public void cancel(final HttpRequest request) {
        URI _requestURI = request.getRequestURI();
        synchronized (pendingRequestMap) {
            synchronized (connectionMap) {
                if (pendingRequestMap.containsKey(_requestURI)) {
                    LOGGER.log(Level.INFO, "[Jack] Closing previous connection.");
                    pendingRequestMap.remove(_requestURI).cancel(true);
                    if (connectionMap.containsKey(_requestURI)) {
                        HttpConnection _connection = connectionMap.remove(_requestURI);
                        _connection.close();
                        connectionPool.invalidateObject(_connection);
                    }
                }
            }
        }
    }

    public void send(final HttpRequest request) {
        final URI _requestURI = request.getRequestURI();
        synchronized (pendingRequestMap) {
            synchronized (connectionMap) {
                pendingRequestMap.put(
                    _requestURI,
                    scheduledExecutorService.schedule(
                        new Runnable() {
                            public void run() {
                                HttpConnection _connection;
                                synchronized (connectionMap) {
                                    _connection = connectionPool.borrowObject();
                                    connectionMap.put(_requestURI, _connection);
                                }
                                try {
                                    request.onResponse((HttpResponse)_connection.send(request));
                                } catch (MalformedURLException exception) {
                                    if (LOGGER.isLoggable(Level.WARNING)) {
                                        LOGGER.log(
                                            Level.WARNING,
                                            "",
                                            exception);
                                    }
                                } catch (SocketException exception) {
                                    // Do nothing.
                                    // This is probably happening due to closing the connection.
                                } catch (IOException exception) {
                                    if (LOGGER.isLoggable(Level.WARNING)) {
                                        LOGGER.log(
                                            Level.WARNING,
                                            "",
                                            exception);
                                    }
                                } finally {
                                    synchronized (connectionMap) {
                                        connectionMap.remove(_requestURI);
                                        connectionPool.returnObject(_connection);
                                    }
                                }
                            }
                        },
                        0,
                        TimeUnit.MILLISECONDS));
            }
        }
    }

    public Object sendNow(final HttpRequest request)
    throws IOException, MalformedURLException {
        HttpConnection _connection = connectionPool.borrowObject();
        Object _response = _connection.send(request);
        connectionPool.returnObject(_connection);
        return _response;
    }
}
