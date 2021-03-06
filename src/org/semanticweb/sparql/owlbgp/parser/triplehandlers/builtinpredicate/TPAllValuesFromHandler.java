/* Copyright 2010-2012 by the developers of the OWL-BGP project. 

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
   along with OWL-BGP. If not, see <http://www.gnu.org/licenses/>.
 */

package  org.semanticweb.sparql.owlbgp.parser.triplehandlers.builtinpredicate;

import org.semanticweb.sparql.owlbgp.model.Identifier;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.DataAllValuesFrom;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ObjectAllValuesFrom;
import org.semanticweb.sparql.owlbgp.model.dataranges.DataRange;
import org.semanticweb.sparql.owlbgp.model.properties.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.parser.TripleConsumer;
import org.semanticweb.sparql.owlbgp.parser.Vocabulary;
import org.semanticweb.sparql.owlbgp.parser.triplehandlers.TripleHandler;

public class TPAllValuesFromHandler extends TripleHandler {

    public TPAllValuesFromHandler(TripleConsumer consumer) {
        super(consumer);
    }

    public void handleTriple(Identifier subject, Identifier predicate, Identifier object) {
        Identifier propID=consumer.getObject(subject, Vocabulary.OWL_ON_PROPERTY,true);
        ObjectPropertyExpression ope=consumer.getOPE(propID);
        if (ope!=null) {
            consumer.translateClassExpression(object);
            ClassExpression ce=consumer.getCE(object);
            consumer.mapClassIdentifierToClassExpression(subject, ObjectAllValuesFrom.create(ope, ce));
        } else {
            DataPropertyExpression dpe=consumer.getDPE(propID);
            if (dpe!=null) {
                DataRange dr=consumer.getDR(object);
                if (dr!=null) {
                    consumer.mapClassIdentifierToClassExpression(subject, DataAllValuesFrom.create(dpe, dr));
                } else {
                    // TODO: error handling
                    throw new RuntimeException("error");
                }
            } else {
                // TODO: error handling
                throw new RuntimeException("error");
            }
        }
    }
}
