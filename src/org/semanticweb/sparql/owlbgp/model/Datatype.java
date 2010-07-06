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

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.sparql.owlbgp.model.Variable.VarType;


public class Datatype extends AbstractExtendedOWLObject implements DataRange {
    private static final long serialVersionUID = -5589507335866233523L;

    public static Set<Datatype> OWL2_DATATYPES=new HashSet<Datatype>();
    static {
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("rdf")+"XMLLiteral"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("owl")+"real"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("owl")+"rational"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"string"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"integer"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"long"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"int"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"short"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"byte"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"decimal"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"float"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"boolean"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"double"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"nonPositiveInteger"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"negativeInteger"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"nonNegativeInteger"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"unsignedLong"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"unsignedInt"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"positiveInteger"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"base64Binary"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"hexBinary"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"anyURI"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"QName"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"normalizedString"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"token"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"Name"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"NCName"));
        
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"NMToken"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"dateTime"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"dateTimeStamp"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"unsignedShort"));
        OWL2_DATATYPES.add(Datatype.create(Prefixes.s_semanticWebPrefixes.get("xsd")+"unsignedByte"));
    }
    
    protected static InterningManager<Datatype> s_interningManager=new InterningManager<Datatype>() {
        protected boolean equal(Datatype object1,Datatype object2) {
            return object1.m_iri==object2.m_iri;
        }
        protected int getHashCode(Datatype object) {
            return object.m_iri.hashCode();
        }
    };
    
    public static final Datatype RDFS_LITERAL=create("http://www.w3.org/2000/01/rdf-schema#Literal");
    
    protected final String m_iri;
   
    protected Datatype(String iri) {
        m_iri=iri.intern();
    }
    public String getIRIString() {
        return m_iri;
    }
    public String toString(Prefixes prefixes) {
        return prefixes.abbreviateIRI(m_iri);
    }
    protected Object readResolve() {
        return s_interningManager.intern(this);
    }
    public static Datatype create(String iri) {
        return s_interningManager.intern(new Datatype(iri));
    }
    public String getIdentifier() {
        return m_iri;
    }
    public <O> O accept(ExtendedOWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }
    protected OWLObject convertToOWLAPIObject(OWLAPIConverter converter) {
        return converter.visit(this);
    }
    public Set<Variable> getVariablesInSignature(VarType varType) {
        return new HashSet<Variable>();
    }
    public Set<Variable> getUnboundVariablesInSignature(VarType varType) {
        return new HashSet<Variable>();
    }
}
