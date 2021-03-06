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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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

package org.icefaces.push.server;

public class ProductInfo {
    /**
     * The company that owns this product.
     */
    public static String COMPANY = "@company@";
    /**
     * The name of the product.
     */
    public static String PRODUCT = "@product@";
    /**
     * The 3 levels of version identification, e.g. 1.0.0.
     */
    public static String PRIMARY = "@version.primary@";
    public static String SECONDARY = "@version.secondary@";
    public static String TERTIARY = "@version.tertiary@";
    /**
     * The release type of the product (alpha, beta, production).
     */
    public static String RELEASE_TYPE = "@release.type@";
    /**
     * The build number.  Typically this would be tracked and maintained
     * by the build system (i.e. Ant).
     */
    public static String BUILD_NO = "@build.number@";
    /**
     * The revision number retrieved from the repository for this build.
     * This is substitued automatically by subversion.
     */
    public static String REVISION = "@revision@";

    /**
     * Convenience method to get all the relevant product information.
     * @return
     */
    public String toString(){
        return
            new StringBuffer().
                append("\r\n").
                append(COMPANY).append("\r\n").
                append(PRODUCT).append(" ").
                    append(PRIMARY).append(".").
                        append(SECONDARY).append(".").
                        append(TERTIARY).append(" ").
                    append(RELEASE_TYPE).append("\n").
                append("Build number: ").append(BUILD_NO).append("\n").
                append("Revision: ").append(REVISION).append("\n").
                    toString();
    }

    public static void main(final String[] arguments) {
        System.out.println(new ProductInfo());
    }
}
