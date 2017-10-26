package com.bplsoft.wfm.rest.dto;

import lombok.*;

@Getter
@Setter
@ToString(exclude = "id")
@EqualsAndHashCode(exclude = {"age", "name"})
@NoArgsConstructor
@AllArgsConstructor
public class HelloDTO {
    Long id;
    Integer age;
    String name;
}
