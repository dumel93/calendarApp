package com.calendar.app.util;

import lombok.experimental.UtilityClass;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@UtilityClass
public class SpecificationUtils {

    public String getLikeWithUpperCasePrefixAndSufix(String searched) {
        return "%" + addEscapeSignToWildcard(searched).toUpperCase() + "%";
    }

    public String addEscapeSignToWildcard(String searched) {
        CharSequence target = "%";
        CharSequence replacement = "\\%";
        return searched.replace(target, replacement);
    }
}
