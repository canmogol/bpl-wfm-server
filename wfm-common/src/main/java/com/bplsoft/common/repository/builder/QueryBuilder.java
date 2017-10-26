package com.bplsoft.common.repository.builder;


import com.bplsoft.common.model.Model;

import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * acm
 */
public class QueryBuilder<T extends Model> {

    private final Class<T> clazz;
    private final EntityManagerFactory entityManagerFactory;

    private final ParamMap<String, Param<String, Object>> paramMap = new ParamMap<>();
    private final List<String> fieldNames = new ArrayList<>();


    public QueryBuilder(Class<T> entityClass, EntityManagerFactory entityManagerFactory) {
        this.clazz = entityClass;
        this.entityManagerFactory = entityManagerFactory;
        for (Method m : entityClass.getMethods()) {
            if (m.getName().startsWith("set")) {
                String fieldName = m.getName().substring(3);
                fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
                fieldNames.add(fieldName);
            }
        }
    }

    public QueryBuilder<T> where(String field, Object value) {
        return where(field, ParamRelation.EQ, value);
    }

    public QueryBuilder<T> where(String field, ParamRelation relation, Object value) {
        // this parameter's and/or will be ignored so it is set to true
        return addParam(field, relation, true, value);
    }

    public QueryBuilder<T> and(String field, Object value) {
        return and(field, ParamRelation.EQ, value);
    }

    public QueryBuilder<T> and(String field, ParamRelation relation, Object value) {
        return addParam(field, relation, true, value);
    }

    public QueryBuilder<T> or(String field, Object value) {
        return or(field, ParamRelation.EQ, value);
    }

    public QueryBuilder<T> or(String field, ParamRelation relation, Object value) {
        return addParam(field, relation, false, value);
    }

    public Optional<T> findOne() {
        final List<T> ts = find();
        if (ts.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(ts.get(0));
        }
    }

    public List<T> find() {
        // will use criteria builder
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

        // select from entity
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> from = criteriaQuery.from(clazz);
        criteriaQuery.select(from);

        // will store all predicates
        List<Predicate> predicates = new ArrayList<>();
        List<Boolean> andOrList = new ArrayList<>();

        // parameter list
        List<ParameterExpression<Object>> parameterExpressionList = new ArrayList<>();
        List<Object> parameterList = new ArrayList<>();

        // for rest of the param list
        for (final Param<String, Object> param : paramMap.getParamList()) {
            // if model does not have this field than do not add the parameter as a criteria
            if (!fieldNames.contains(param.getKey())) {
                continue;
            }
            // add and or for query builder
            if (param.getValueSecondary() != null && param.getValueSecondary().equals(false)) {
                andOrList.add(Boolean.FALSE);
            } else {
                andOrList.add(Boolean.TRUE);
            }
            // create a parameter expression with value's type
            ParameterExpression parameterExpression = null;
            switch (param.getRelation()) {
                case EQ:
                    parameterExpression = criteriaBuilder.parameter(param.getValue().getClass());
                    predicates.add(criteriaBuilder.equal(from.get(param.getKey()), parameterExpression));
                    break;
                case LIKE:
                    parameterExpression = criteriaBuilder.parameter(param.getValue().getClass());
                    predicates.add(criteriaBuilder.like(from.<String>get(param.getKey()), parameterExpression));
                    break;
                case NE:
                    parameterExpression = criteriaBuilder.parameter(param.getValue().getClass());
                    predicates.add(criteriaBuilder.notEqual(from.<Number>get(param.getKey()), parameterExpression));
                    break;
                case BETWEEN:
                    parameterExpression = criteriaBuilder.parameter(param.getValue().getClass());
                    predicates.add(
                        criteriaBuilder.between(
                            from.<Comparable>get(param.getKey()),
                            (Comparable) param.getValue(), (Comparable) param.getValueSecondary()
                        )
                    );
                    continue;
                case GT:
                    parameterExpression = criteriaBuilder.parameter(param.getValue().getClass());
                    predicates.add(criteriaBuilder.gt(from.<Number>get(param.getKey()), parameterExpression));
                    break;
                case GE:
                    parameterExpression = criteriaBuilder.parameter(param.getValue().getClass());
                    predicates.add(criteriaBuilder.ge(from.<Number>get(param.getKey()), parameterExpression));
                    break;
                case LT:
                    parameterExpression = criteriaBuilder.parameter(param.getValue().getClass());
                    predicates.add(criteriaBuilder.lt(from.<Number>get(param.getKey()), parameterExpression));
                    break;
                case LE:
                    parameterExpression = criteriaBuilder.parameter(param.getValue().getClass());
                    predicates.add(criteriaBuilder.le(from.<Number>get(param.getKey()), parameterExpression));
                    break;
            }
            // then add the value(with its new type) to parameterList and the parameter expression to its list
            parameterExpressionList.add(parameterExpression);
            parameterList.add(param.getValue());
        }

        // where    and|x=1     and|y=2     or|z=3
        // set predicates if any
        if (predicates.size() > 0) {
            List<Predicate> predicateList = new ArrayList<>();
            Predicate predicate;
            for (int i = 0; i < predicates.size(); i = i + 2) {
                if (i + 1 < predicates.size()) {
                    Boolean andOr = andOrList.get(i + 1);
                    if (andOr) {
                        predicate = criteriaBuilder.and(predicates.get(i), predicates.get(i + 1));
                    } else {
                        predicate = criteriaBuilder.or(predicates.get(i), predicates.get(i + 1));
                    }
                    predicateList.add(predicate);
                } else {
                    Boolean andOr = andOrList.get(i);
                    if (andOr) {
                        predicate = criteriaBuilder.and(predicates.get(i));
                    } else {
                        predicate = criteriaBuilder.or(predicates.get(i));
                    }
                    predicateList.add(predicate);
                }
            }
            if (predicateList.size() > 0) {
                Predicate[] predicateArray = new Predicate[predicateList.size()];
                for (int i = 0; i < predicateList.size(); i++) {
                    predicateArray[i] = predicateList.get(i);
                }
                criteriaQuery.where(predicateArray);
            }
        }


        // create query
        TypedQuery<T> query = entityManagerFactory.createEntityManager().createQuery(criteriaQuery);

        /*
         * 1	0	25	0	25
         * 2	25	25	25	25
         * 3	50	25	50	25
         *
         */

        // set parameter values
        if (parameterList.size() > 0) {
            for (int i = 0; i < parameterList.size(); i++) {
                query.setParameter(parameterExpressionList.get(i), parameterList.get(i));
            }
        }

        // return the result list
        return query.getResultList();
    }

    private QueryBuilder<T> addParam(String field, ParamRelation relation, boolean andOr, Object value) {
        paramMap.addParam(new Param<>(field, value, andOr, relation));
        return this;
    }

}
