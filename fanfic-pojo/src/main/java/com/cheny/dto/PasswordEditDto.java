package com.cheny.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PasswordEditDto {
    @NotNull
    private String prePassword;
    @NotNull
    private String newPassword;
}
