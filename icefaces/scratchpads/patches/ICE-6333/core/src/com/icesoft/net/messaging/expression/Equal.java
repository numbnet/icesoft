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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
package com.icesoft.net.messaging.expression;

import com.icesoft.net.messaging.Message;

/**
 * <p>
 *   The Equal class represents the comparison <code>=</code> operator. Its
 *   responsibility is to evaluate whether its left operand is <b>equal</b> to
 *   its right operand.
 * </p>
 *
 * @see        NotEqual
 */
public class Equal
extends ComparisonOperator
implements Operand {
    /**
     * <p>
     *   Constructs a new Equal comparison operator object with the specified
     *   <code>leftOperand</code> and <code>rightOperand</code>.
     * </p>
     *
     * @param      leftOperand
     *                 the Operand that is to be to the left of the Equal
     *                 comparison operator.
     * @param      rightOperand
     *                 the Operand that is to be to the right of the Equal
     *                 comparison operator.
     * @throws     IllegalArgumentException
     *                 if one of the following occurs:
     *                 <ul>
     *                   <li>
     *                     the specified <code>leftOperand</code> is
     *                     <code>null</code>, or
     *                   </li>
     *                   <li>
     *                     the specified <code>rightOperand</code> is
     *                     <code>null</code>.
     *                   </li>
     *                 </ul>
     */
    public Equal(final Identifier leftOperand, final Literal rightOperand)
    throws IllegalArgumentException {
        super(leftOperand, rightOperand);
    }

    /**
     *
     * @param      message
     *                 the target Message.
     * @return     <code>true</code> if the left operand is equal to the right
     *             operand, <code>false</code> if not.
     * @throws     IllegalArgumentException
     *                 if the specified <code>message</code> is
     *                 <code>null</code>.
     */
    public boolean evaluate(final Message message) {
        if (message == null) {
            throw new IllegalArgumentException("message is null");
        }
        Object _propertyValue =
            message.getObjectProperty(
                (String)((Identifier)leftOperand).getValue());
        return
            _propertyValue instanceof String &&
            rightOperand instanceof StringLiteral &&
            ((String)_propertyValue).
                equalsIgnoreCase(
                    (String)((StringLiteral)rightOperand).getValue());
    }

    /**
     * <p>
     *   Returns a string representation of this Equal comparison operator:
     *   <pre>
     *    &lt;leftOperand&gt; = &lt;rightOperand&gt;
     *   </pre>
     * </p>
     *
     * @return     a string represention of this Equal comparison operator.
     */
    public String toString() {
        return leftOperand + " = " + rightOperand;
    }
}
