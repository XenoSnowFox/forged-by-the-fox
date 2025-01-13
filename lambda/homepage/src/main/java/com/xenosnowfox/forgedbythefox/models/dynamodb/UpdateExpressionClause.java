package com.xenosnowfox.forgedbythefox.models.dynamodb;

public enum UpdateExpressionClause {
    /**
     * SET - modifying or adding item attributes.
     * <p>
     * See: <a href='https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.UpdateExpressions.html#Expressions.UpdateExpressions.SET'>Expressions.UpdateExpressions.SET</a>
     */
    SET,

    /**
     * REMOVE — deleting attributes from an item
     * <p>
     *     See: <a href='https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.UpdateExpressions.html#Expressions.UpdateExpressions.REMOVE'>Expressions.UpdateExpressions.REMOVE</a>
     */
    REMOVE,

    /**
     * ADD — updating numbers and sets
     * <p>
     *     See: <a href='https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.UpdateExpressions.html#Expressions.UpdateExpressions.ADD'>Expressions.UpdateExpressions.ADD</a>
     */
    ADD,

    /**
     * DELETE — removing elements from a set
     * <p>
     *     See: <a href='https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.UpdateExpressions.html#Expressions.UpdateExpressions.DELETE'>Expressions.UpdateExpressions.DELETE</a>
     */
    DELETE
}
