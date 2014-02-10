/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icemobile.samples.spring;

/**
 *
 */

public class QRScanBean {

    // Name of the parameter in demo is scanOne
    private String scanOne;
    private String plaintextOne = "www.icesoft.org";

    public String getScanOne() {
        return scanOne;
    }

    public void setScanOne(String scanOne) {
        this.scanOne = scanOne;
    }

    public String getPlaintextOne() {
        return plaintextOne;
    }

    public void setPlaintextOne(String plaintextOne) {
        this.plaintextOne = plaintextOne;
    }
}
