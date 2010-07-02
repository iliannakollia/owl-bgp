/* Copyright 2010 by the Oxford University Computing Laboratory
   
   This file is part of OWL-BGP.

   OWL-BGP is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   OWL-BGP is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.
   
   You should have received a copy of the GNU Lesser General Public License
   along with OWL-BGP.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.semanticweb.sparql.owlbgp.model;

import java.util.HashSet;
import java.util.Set;


public class ObjectComplementOf extends AbstractExtendedOWLObject implements ClassExpression {
    private static final long serialVersionUID = -4754922633877659444L;

    protected final ClassExpression m_classExpression;
   
    protected ObjectComplementOf(ClassExpression classExpression) {
        m_classExpression=classExpression;
    }
    public String toString(Prefixes prefixes) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("ObjectComplementOf(");
        buffer.append(m_classExpression.toString(prefixes));
        buffer.append(")");
        return buffer.toString();
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    protected static InterningManager<ObjectComplementOf> s_interningManager=new InterningManager<ObjectComplementOf>() {
        protected boolean equal(ObjectComplementOf object1,ObjectComplementOf object2) {
            return object1.m_classExpression.equals(object2.m_classExpression);
        }
        protected int getHashCode(ObjectComplementOf object) {
            return -object.m_classExpression.hashCode();
        }
    };
    public static ObjectComplementOf create(ClassExpression classExpression) {
        return s_interningManager.intern(new ObjectComplementOf(classExpression));
    }
    public String getIdentifier() {
        return null;
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    public Set<Variable> getVariablesInSignature() {
        Set<Variable> variables=new HashSet<Variable>();
        variables.addAll(m_classExpression.getVariablesInSignature());
        return variables;
    }
}
