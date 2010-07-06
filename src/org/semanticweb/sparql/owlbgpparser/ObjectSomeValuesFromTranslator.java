package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.ObjectSomeValuesFrom;

public class ObjectSomeValuesFromTranslator extends AbstractObjectQuantifiedRestrictionTranslator {

    public ObjectSomeValuesFromTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(ObjectPropertyExpression property,ClassExpression filler) {
        return ObjectSomeValuesFrom.create(property, filler);
    }
    protected String getFillerTriplePredicate() {
        return OWLRDFVocabulary.OWL_SOME_VALUES_FROM.getIRI();
    }
}