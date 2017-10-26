package com.bplsoft.wfm.rest.dto;

import lombok.Builder;
import lombok.Singular;
import lombok.ToString;

import java.util.Set;

@Builder
@ToString
public class HelloAnotherDTO {
    Long id;
    Integer age;
    String name;
    @Singular
    private Set<String> phoneNumbers;
}
