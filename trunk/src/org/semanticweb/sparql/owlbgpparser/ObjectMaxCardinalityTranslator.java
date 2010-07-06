package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.Clazz;
import org.semanticweb.sparql.owlbgp.model.ObjectMaxCardinality;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public class ObjectMaxCardinalityTranslator extends AbstractObjectCardinalityTranslator {

    public ObjectMaxCardinalityTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(ObjectPropertyExpression prop, int cardi) {
        return ObjectMaxCardinality.create(cardi, prop, Clazz.THING);
    }
    protected ClassExpression createRestriction(ObjectPropertyExpression prop,int cardi,ClassExpression filler) {
        return ObjectMaxCardinality.create(cardi, prop, filler);
    }
    protected String getCardinalityTriplePredicate() {
        return OWLRDFVocabulary.OWL_MAX_CARDINALITY.getIRI();
    }
    protected String getQualifiedCardinalityTriplePredicate() {
        return OWLRDFVocabulary.OWL_MAX_QUALIFIED_CARDINALITY.getIRI();
    }
}
