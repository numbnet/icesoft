/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */
package com.icesoft.faces.async.server;

public class AsyncHttpServerSettings {
    private boolean blocking = false;
    private boolean compression = true;
    private boolean persistent = true;
    private int port = 51315;
    private int executeQueueSize = 30;
    private int responseQueueSize = 100;
    private int updatedViewsQueueSize = 100;
    private double responseQueueThreshold = 0.7d;
    private double updatedViewsQueueThreshold = 0.7d;
    private String purgeMessageContents = "all";

    public AsyncHttpServerSettings() {
        // do nothing.
    }

    public int getExecuteQueueSize() {
        return executeQueueSize;
    }

    public int getPort() {
        return port;
    }

    public String getPurgeMessageContents() {
        return purgeMessageContents;
    }

    public int getResponseQueueSize() {
        return responseQueueSize;
    }

    public double getResponseQueueThreshold() {
        return responseQueueThreshold;
    }

    public int getUpdatedViewQueueSize() {
        return updatedViewsQueueSize;
    }

    public double getUpdatedViewQueueThreshold() {
        return updatedViewsQueueThreshold;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setBlocking(final boolean blocking) {
        this.blocking = blocking;
    }

    public void setCompression(final boolean compression) {
        this.compression = compression;
    }

    public void setExecuteQueueSize(final int executeQueueSize) {
        this.executeQueueSize = executeQueueSize;
    }

    public void setPersistent(final boolean persistent) {
        this.persistent = persistent;
    }

    public void setPort(final int port)
    throws IllegalArgumentException {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Illegal port: " + port);
        }
        this.port = port;
    }

    public void setPurgeMessageContents(final String purgeMessageContents) {
        this.purgeMessageContents = purgeMessageContents;
    }

    public void setResponseQueueSize(final int responseQueueSize) {
        this.responseQueueSize = responseQueueSize;
    }

    public void setResponseQueueThreshold(final double responseQueueThreshold) {
        this.responseQueueThreshold = responseQueueThreshold;
    }

    public void setUpdatedViewsQueueSize(
        final int updatedViewsQueueSize) {

        this.updatedViewsQueueSize = updatedViewsQueueSize;
    }

    public void setUpdatedViewsQueueThreshold(
        final double updatedViewsQueueThreshold) {

        this.updatedViewsQueueThreshold = updatedViewsQueueThreshold;
    }

    public boolean useCompression() {
        return compression;
    }
}
