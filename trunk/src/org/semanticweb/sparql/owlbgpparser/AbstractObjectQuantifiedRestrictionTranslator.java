package org.semanticweb.sparql.owlbgpparser;

import org.semanticweb.sparql.owlbgp.model.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.ObjectPropertyExpression;

public abstract class AbstractObjectQuantifiedRestrictionTranslator extends AbstractObjectRestrictionTranslator {

    public AbstractObjectQuantifiedRestrictionTranslator(OWLRDFConsumer consumer) {
        super(consumer);
    }
    protected ClassExpression translateRestriction(String mainNode) {
        String fillerObject=getResourceObject(mainNode, getFillerTriplePredicate(), true);
        ObjectPropertyExpression prop=translateOnProperty(mainNode);
        ClassExpression desc=translateToClassExpression(fillerObject);
        return createRestriction(prop, desc);
    }
    protected abstract String getFillerTriplePredicate();
    protected abstract ClassExpression createRestriction(ObjectPropertyExpression property,ClassExpression filler);
}
