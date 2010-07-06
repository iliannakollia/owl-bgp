package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.DataPropertyExpression;
import org.semanticweb.sparql.owlbgp.model.DataRange;
import org.semanticweb.sparql.owlbgp.model.DataSomeValuesFrom;

public class DataSomeValuesFromTranslator extends AbstractDataQuantifiedRestrictionTranslator {

    public DataSomeValuesFromTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression createRestriction(DataPropertyExpression prop, DataRange filler) {
        return DataSomeValuesFrom.create(prop, filler);
    }
    protected String getFillerTriplePredicate() {
        return OWLRDFVocabulary.OWL_SOME_VALUES_FROM.getIRI();
    }
}
